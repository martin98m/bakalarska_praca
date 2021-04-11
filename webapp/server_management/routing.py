from django.urls import path

from server_management import consumer

websocket_urlpatterns = [
    path('server_call/ws/call/<str:server_name>', consumer.ServerCommunication),
    path('server_call/ws/call/<str:server_name>/control', consumer.ServerController),
    path('server_call/ws/command', consumer.CommandConsumer)
]
