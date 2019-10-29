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
    fill = {'par1': server_name, 'form':form}

    if request.method == 'POST':
        form = CommandForm(request.POST)
        if form.is_valid():
            server = ServerConnection('localhost', 53593)
            # server.send_msg()
            print("VALID FORM")
            data = form.cleaned_data
            server.send_msg(data['command'])
            server.disconnect()
            fill.update({'cmd': data['command']})
            return render(request, 'server_management/server_connection.html', fill)

    return render(request, 'server_management/server_connection.html', fill)

