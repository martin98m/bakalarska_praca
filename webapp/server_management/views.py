# Create your views here.
import ast

from django.contrib.auth import logout
from django.contrib.auth.decorators import login_required
from django.http import HttpResponse
from django.shortcuts import render
from django.views import generic

from server_management.SocketConnectionToServer.SocketManager import SocketManager
from server_management.forms import CommandForm
from .models import ServerInfo, ServerData, UserCommands

login_url = '/server_management/accounts/login'


def logout_view(request):
    logout(request)
    return HttpResponse(status=201)


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

    return render(request, template_name, {context_server_data: s_data, context_server_info: s_info})


# todo get cookie with username/password/id

@login_required(login_url=login_url)
def server_call(request, server_name):

    template_name = 'server_management/server_call.html'
    form = CommandForm()
    fill = {'par1': server_name, 'form': form}

    server_info = ServerInfo.objects.get(server_name__exact=server_name)
    fill.update({'server': server_info.server_ip, 'port': server_info.server_port})

    print('hello')
    user_commands = UserCommands.objects.all().filter(username=request.user.username)
    fill.update({'commands': user_commands})
    print(user_commands)

    print(server_info.server_name, server_info.server_ip,
          server_info.server_port, server_info.data_collection_delay_minutes)
    old = None
    navrat = None
    manager = SocketManager()
    conn = manager.get_socket(request.user.username, server_name)
    if conn is None:
        print("Connection is not enstablished")
    else:
        print("Connection established")

    if 'old_data' in request.COOKIES:
        old = request.COOKIES['old_data']
        old = ast.literal_eval(old)
        fill.update({'old_data': old})

    if request.method == 'POST':
        form = CommandForm(request.POST)
        if form.is_valid():
            manager = SocketManager()
            connection = manager.get_socket(request.user.username, server_name)
            if connection is None:
                connection = manager.add_socket(request.user.username, server_name, server_info.server_ip, server_info.server_port)

            # manager.print_connections()
            if connection.is_connected() is False:
                connection.connect()

            data = form.cleaned_data
            if data['command'] == 'CHANGE':
                resp = connection.send_msg(data['command'])
            else:
                resp = connection.send_message_with_response(data['command'])

            fill.update({'cmd': data['command']})
            fill.update({'response': resp})
            navrat = render(request, template_name, fill)
            if old is None:
                old = resp
            else:
                old = old + resp

            navrat.set_cookie('old_data', old)
        else:
            manager = SocketManager()
            manager.remove_socket(request.user.username, server_name)

    else:
        navrat = render(request, template_name, fill)

    return navrat


@login_required(login_url=login_url)
def main_menu(request):
    template_name = 'server_management/main_menu.html'

    if request.method == 'POST':
        print('MAIN MENU POST')
        server_name = request.POST.get('server_name')
        print(server_name)
        SocketManager().remove_socket(request.user.username, server_name)

    context_server_info = 'server_info'
    context_server_data_latest = 'server_data_latest'
    context_server_conn = 'server_conn'

    s_info = ServerInfo.objects.values('server_name')

    rest = SocketManager().get_connections_for(request.user.username)
    res = []
    if rest is not None:
        for x in rest:
            res.append(x)

    s_data_latest = ServerData.objects.raw('''SELECT t.server_name, t.CPU_usage, t.RAM_usage, t.date, t.time FROM server_data t
INNER JOIN (
    SELECT server_name, max(date+time) as MaxDate
    from server_data
    group by server_name
    ) tm ON t.server_name = tm.server_name AND t.date+t.time = tm.MaxDate
UNION
SELECT server_name, NULL as CPU_usage, NULL as RAM_usage, NULL as date, NULL as time FROM server_info
WHERE server_name NOT in
      (SELECT t.server_name FROM server_data t
        INNER JOIN (
        SELECT server_name, max(date+time) as MaxDate
        from server_data
        group by server_name
        ) tm ON t.server_name = tm.server_name AND t.date+t.time = tm.MaxDate
    )''')

    # print(s_info)
    # Person.objects.raw('SELECT * FROM some_other_table', translations=name_map)
    # print(s_data_latest[1])

    return render(request, template_name, {context_server_info: s_info, context_server_data_latest: s_data_latest, context_server_conn: res})
