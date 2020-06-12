from django.urls import re_path

from server_management import consumer

websocket_urlpatterns = [
    re_path(r'ws/demo/(?P<room_name>\w+)/$', consumer.ChatConsumer),
]