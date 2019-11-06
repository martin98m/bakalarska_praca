# Create your views here.
import ast

from django.contrib.auth import logout
from django.contrib.auth.decorators import login_required
from django.http import HttpResponse
from django.shortcuts import render
from django.views import generic

from server_management.SocketConnectionToServer.SocketManager import SocketManager
from server_management.forms import CommandForm
from .models import ServerInfo, ServerData

login_url = '/server_management/accounts/login'


def logout_view(request):
    print('CORRECT LOGOUT')
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
    template_name = 'server_management/server_usage_data.html'
    context_object_name = 'raw_data'
    data = ServerData.objects.all().filter(server_name=server_name)
    return render(request, template_name, {context_object_name: data})

# class ServerDataView(generic.ListView):
#     template_name = 'server_management/server_usage_data.html'
#     context_object_name = 'server_data_from_db'
#
    # def get_queryset(self):
    #     return ServerData.objects.all()


# todo get cookie with username/password/id

@login_required(login_url=login_url)
def server_call(request, server_name):
    print('LOGIN:' + request.user.username)
    template_name = 'server_management/server_connection.html'
    form = CommandForm()
    fill = {'par1': server_name, 'form': form}
    server_info = ServerInfo.objects.get(server_name__exact=server_name)
    fill.update({'server': server_info.server_ip, 'port': server_info.server_port})
    print(server_info.server_name, server_info.server_ip,
          server_info.server_port, server_info.time_between_next_data_collection)
    old = None
    navrat = None

    if 'old_data' in request.COOKIES:
        old = request.COOKIES['old_data']
        old = ast.literal_eval(old)
        fill.update({'old_data': old})

    if request.method == 'POST':
        print('POOOOOOST')
        form = CommandForm(request.POST)
        if form.is_valid():
            print('VALID POOOST')
            manager = SocketManager()
            # print(manager)
            # manager.print_connections()
            connection = manager.get_socket(request.user.username)
            # connection = None
            if connection is None:
                connection = manager.add_socket(request.user.username, server_info.server_ip, server_info.server_port)

            manager.print_connections()
            if connection.is_connected() is False:
                connection.connect()

            data = form.cleaned_data
            if data['command'] == 'CHANGE':
                resp = connection.send_msg(data['command'])
            else:
                resp = connection.send_message_with_response(data['command'])
            print(resp)
            # server.send_msg(data['command'])

            fill.update({'cmd': data['command']})
            fill.update({'response': resp})
            navrat = render(request, template_name, fill)
            # print('OLD')
            # print(type(old))
            # print('NEW')
            # print(resp)
            # print(type(resp))
            if old is None:
                old = resp
            else:
                old = old + resp

            navrat.set_cookie('old_data', old)
        else:
            print("AAAAAAAAAAAAA")
            manager = SocketManager()
            manager.remove_socket(request.user.username)

    else:
        navrat = render(request, template_name, fill)

    return navrat

