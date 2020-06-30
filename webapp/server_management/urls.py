from django.urls import path

from . import views
from django.contrib.auth import views as auth_views

app_name = 'server_management'

urlpatterns = [
    path('', views.main_menu, name='main_menu_url'),
    path('server_data/<str:server_name>', views.data_view, name='server_data_url'),
    path('server_call/<str:server_name>', views.server_call, name='server_call_url'),

    path('accounts/logout',
         auth_views.LogoutView.as_view(template_name='server_management/server_management_logout.html'),
         name='logout_url'),
    path('accounts/login',
         auth_views.LoginView.as_view(template_name='server_management/server_management_login.html'),
         name='login_url')
]
