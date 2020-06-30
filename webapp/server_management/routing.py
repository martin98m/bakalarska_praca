from django.urls import re_path, path

from server_management import consumer

websocket_urlpatterns = [
    path('server_call/<str:server_name>/ws/call', consumer.ChatConsumer)
]
