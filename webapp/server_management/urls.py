from django.urls import path

from . import views

app_name = 'server_management'
urlpatterns = [
    path('server_info', views.ServerInfoView.as_view(), name='info'),
    path('server_data', views.ServerDataView.as_view(), name='data')
]

