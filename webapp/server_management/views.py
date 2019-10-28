# Create your views here.
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

