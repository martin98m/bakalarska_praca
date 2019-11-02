# Create your views here.
import ast

from django.shortcuts import render
from django.views import generic
from django.views.generic import TemplateView

from server_management.SocketConnectionToServer.SocketManager import SocketManager
from server_management.SocketConnectionToServer.Connection import ServerConnection
from server_management.forms import CommandForm
from .models import ServerInfo, ServerData


class ServerInfoView(generic.ListView):
    template_name = 'server_management/server_list_with_info.html'
    context_object_name = 'data_from_db'

    # model = ServerInfo.objects.all()
    # queryset = ServerInfo.objects.all()
    # ServerView.model | ServerView.queryset | def get_queryset(self)
    # are for getting data from database if needed to use on this view

    def get_queryset(self):
        return ServerInfo.objects.all().order_by('server_name')


class ServerDataView(generic.ListView):
    template_name = 'server_management/server_usage_data.html'
    context_object_name = 'server_data_from_db'

    def get_queryset(self):
        return ServerData.objects.all()


# todo get cookie with username/password/id
def server_call(request, server_name):
    template_name = 'server_management/server_connection.html'
    form = CommandForm()
    fill = {'par1': server_name, 'form': form}
    server_info = ServerInfo.objects.get(server_name__exact=server_name)
    fill.update({'server': server_info.server_ip, 'port': server_info.server_port})
    print(server_info.server_name, server_info.server_ip,
          server_info.server_port, server_info.time_between_next_data_collection)
    old = None
    if 'old_data' in request.COOKIES:
        old = request.COOKIES['old_data']
        old = ast.literal_eval(old)
        fill.update({'old_data': old})

    if request.method == 'POST':
        form = CommandForm(request.POST)
        if form.is_valid():
            manager = SocketManager()
            # print(manager)
            # manager.print_connections()
            connection = manager.get_socket(request.COOKIES['name'])
            # connection = None
            if connection is None:
                connection = manager.add_socket(request.COOKIES['name'], server_info.server_ip, server_info.server_port)

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
            navrat = render(request, 'server_management/server_connection.html', fill)
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
        navrat = render(request, 'server_management/server_connection.html', fill)

    return navrat

    #         print("VALID FORM")

    #           print(data)

    #           resp = server.send_msg(data['command'])
    #           server.disconnect()
    # print(fill)



