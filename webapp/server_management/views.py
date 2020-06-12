# Create your views here.
import ast

from django.contrib.auth.decorators import login_required
from django.shortcuts import render, redirect
from django.views import generic

from server_management.SocketConnectionToServer.SocketManager import SocketManager
from server_management.forms import CommandForm, CommandSave
from .models import ServerInfo, ServerData, UserCommands

login_url = '/accounts/login'
# sm = SocketManager()


def index(request):
    return render(request, 'server_management/channel_demo.html')

def room(request, room_name):
    return render(request, 'server_management/room.html', {
        'room_name': room_name
    })

class ServerInfoView(generic.ListView):
    template_name = 'server_management/server_list_with_info.html'
    context_object_name = 'data_from_db'

    # model = ServerInfo.objects.all()
    # queryset = ServerInfo.objects.all()
    # ServerView.model | ServerView.queryset | def get_queryset(self)
    # are for getting data from database if needed to use on this view

    def get_queryset(self):
        return ServerInfo.objects.all().order_by('server_name')


@login_required(login_url=login_url)
def data_view(request, server_name):
    template_name = 'server_management/server_data.html'

    context_server_data = 'server_data'
    context_server_info = 'server_info'
    s_data = ServerData.objects.all().filter(server_name=server_name)
    s_info = ServerInfo.objects.all().filter(server_name=server_name)
    print(s_data)
    print(s_info)

    return render(request, template_name, {context_server_data: s_data, context_server_info: s_info})


# todo get cookie with username/password/id

@login_required(login_url=login_url)
def server_call(request, server_name):

    print(request.user.has_perm('perm1'))
    print(request.user.get_group_permissions())

    if request.user.has_perm('server_management.view_serverdata'):
        print('OK MAS PERM')
    else:
        return redirect('/')

    used_commands = None
    cookie_string = None
    if 'used_commands' in request.COOKIES:
        cookie_string = request.COOKIES['used_commands']
        # print(cookie_string.split('-'))
        used_commands = cookie_string.split('-')
        for x in used_commands:
            if len(x) is 0:
                used_commands.remove(x)

        # print(used_commands)

    template_name = 'server_management/server_call.html'

    context_server_name = 'server_name'
    fill = ({context_server_name: server_name})

    context_global_commands = 'global_commands'
    context_user_commands = 'user_commands'
    context_used_commands = 'used_commands'

    context_cmd_history = 'cmd_history'
    context_server_connection = 'connection'

    server_info = ServerInfo.objects.get(server_name__exact=server_name)

    connection = SocketManager().get_socket(request.user.username, server_name)

    cmd_content = None
    if 'cmd_history' in request.COOKIES:
        cmd_content = request.COOKIES['cmd_history']
        cmd_content = ast.literal_eval(cmd_content)

    if request.method == 'POST':

        form_a = CommandForm(request.POST)
        form_b = CommandSave(request.POST)
        if form_a.is_valid():
            print('POST1')
            if connection is None:
                connection = SocketManager()\
                    .add_socket(request.user.username, server_name, server_info.server_ip, server_info.server_port)

            print('CONN>', connection, '\n', 'CONS>', connection.is_connected())
            if connection.is_connected() is False:
                connection.connect()

            data = form_a.cleaned_data
            if connection.is_connected():
                resp = connection.send_message_with_response(data['command'])
                print('RESP>', resp)
                print('cmd>', cmd_content)
                if cmd_content is not None:
                    cmd_content = cmd_content + resp
                else:
                    cmd_content = resp

            used_commands.append(data['command'])
            print(used_commands)
            cookie_string = cookie_string + data['command'] + '-'
            print(cookie_string)

            fill.update({context_cmd_history: cmd_content})

        elif form_b.is_valid():
            print('POST2')
            data = form_b.cleaned_data

            print(data['target'], data['commandSave'])
            UserCommands.objects\
                .create(username=request.user.username, target=data['target'], commandsave=data['commandSave'])

        else:
            print('POST3')
            if connection is not None and connection.is_connected() is True:
                connection.disconnect()

    global_commands = UserCommands.objects.all().filter(command_type='global')
    fill.update({context_global_commands: global_commands})

    user_commands = UserCommands.objects.all().filter(username_id=0, command_type='user')
    fill.update({context_user_commands: user_commands})
    if connection is None:
        fill.update({context_server_connection: False})
    elif connection.is_connected():
        fill.update({context_server_connection: True})
    else:
        fill.update({context_server_connection: False})

    fill.update({context_used_commands: used_commands})

    response = render(request, template_name, fill)
    response.set_cookie('cmd_history', cmd_content)
    if cookie_string is None:
        response.set_cookie('used_commands', '')
    else:
        response.set_cookie('used_commands', cookie_string)

    return response


@login_required(login_url=login_url)
def main_menu(request):
    template_name = 'server_management/main_menu.html'

    context_server_info = 'server_info'
    context_server_data_latest = 'server_data_latest'
    context_server_conn = 'server_conn'

    s_info = ServerInfo.objects.values('server_name')

    rest = SocketManager().get_connections_for(request.user.username)
    res = []
    if rest is not None:
        for x in rest:
            res.append(x)

    s_data_latest = ServerData.objects.raw(
        '''SELECT t.id, t.server_name_id, t.CPU_usage, t.RAM_usage, t.date, t.time 
        FROM server_management_serverdata t
        INNER JOIN (
            SELECT server_name_id, max(date+time) as MaxDate
            from server_management_serverdata
            group by server_name_id
        ) tm ON t.server_name_id = tm.server_name_id AND t.date+t.time = tm.MaxDate
UNION
SELECT NULL as id, server_name, NULL as CPU_usage, NULL as RAM_usage, NULL as date, NULL as time FROM server_management_serverinfo
WHERE server_name NOT in
      (SELECT t.server_name_id FROM server_management_serverdata t
        INNER JOIN (
        SELECT server_name_id, max(date+time) as MaxDate
        from server_management_serverdata
        group by server_name_id
        ) tm ON t.server_name_id = tm.server_name_id AND t.date+t.time = tm.MaxDate
    )''')

    # print(s_data_latest)
    return render(request, template_name, {context_server_info: s_info, context_server_data_latest: s_data_latest, context_server_conn: res})
