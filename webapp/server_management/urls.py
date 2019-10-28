from django.urls import path

from . import views
from server_management.views import ServerDataView, ShowServerListToConnect, ServerInfoView


app_name = 'server_management'
urlpatterns = [
    path('server_info/', ServerInfoView.as_view()),
    path('server_data/', ServerDataView.as_view()),
    path('server_call/', ShowServerListToConnect.as_view()),
    path('server_call/<str:server_name>/', views.connect_to_server)
]

