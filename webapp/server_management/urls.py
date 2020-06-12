from django.urls import path

from . import views
from django.contrib.auth import views as auth_views

app_name = 'server_management'

urlpatterns = [
    path('server_data/<str:server_name>', views.data_view, name='server_data_url'),  # server_data + server_info
    path('server_call/<str:server_name>', views.server_call, name='server_call_url'),  # server_call, connection with server
    # path('accounts/logout/', views.logout),
    # path('accounts/', include('django.contrib.auth.urls')),
    # path('accounts/login', auth_views.LoginView.as_view(), name='login_url'),
    path('accounts/logout',
         auth_views.LogoutView.as_view(template_name='server_management/server_management_logout.html'), name='logout_url'),
    path('accounts/login', auth_views.LoginView.as_view(template_name='server_management/server_management_login.html'), name='login_url'),

    # path('main_menu', views.main_menu)  # real main menu
    path('', views.main_menu, name='main_menu_url'),  # real main menu
    path('demo', views.index, name='index'),
    path('demo/<str:room_name>/', views.room, name='room')
]
