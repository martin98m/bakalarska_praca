# Create your views here.
from django.shortcuts import render
from django.views import generic

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


def server_call(request, server_name):
    template_name = 'server_management/server_connection.html'
    form = CommandForm()
    fill = {'par1': server_name, 'form': form}
    server_info = ServerInfo.objects.get(server_name__exact=server_name)
    print(server_info.server_name, server_info.server_ip,
          server_info.server_port, server_info.time_between_next_data_collection)

    if request.method == 'POST':
        form = CommandForm(request.POST)
        if form.is_valid():
            server = ServerConnection(server_info.server_ip, server_info.server_port)
            print("VALID FORM")
            data = form.cleaned_data
            # server.send_msg(data['command'])
            resp = server.send_message_with_response(data['command'])
            server.disconnect()
            # print(resp)
            fill.update({'cmd': data['command']})
            fill.update({'response': resp})
            # print(fill)
            return render(request, 'server_management/server_connection.html', fill)

    return render(request, 'server_management/server_connection.html', fill)

