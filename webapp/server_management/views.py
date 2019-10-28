# Create your views here.
from django.http import Http404, HttpResponse
from django.views import generic

from .models import ServerInfo, ServerData


class ServerInfoView(generic.ListView):
    template_name = 'server_management/info.html'
    context_object_name = 'data_from_db'
    # model = ServerInfo.objects.all()
    # queryset = ServerInfo.objects.all()
    # ServerView.model | ServerView.queryset | def get_queryset(self)
    # are for getting data from database if needed to use on this view

    def get_queryset(self):
        return ServerInfo.objects.all().order_by('server_name')


class ServerDataView(generic.ListView):
    template_name = 'server_management/data.html'
    context_object_name = 'server_data_from_db'

    def get_queryset(self):
        return ServerData.objects.all()


class ShowServerListToConnect(generic.ListView):
    template_name = 'server_management/server_list.html'
    context_object_name = 'server_list'

    def get_queryset(self):
        return ServerInfo.objects.all()


def connect_to_server(request, server_name):
    try:
        server = ServerInfo.objects.get(pk=server_name)
    except ServerInfo.DoesNotExist:
        raise Http404("Server does not exist")

    return HttpResponse('Hey '+server_name+'<br>Your ip is:'+server.server_ip)

