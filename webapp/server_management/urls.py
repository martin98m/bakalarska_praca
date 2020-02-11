from django.urls import path, include

from server_management.views import ServerInfoView
from . import views

app_name = 'server_management'
urlpatterns = [
    # path('', ServerInfoView.as_view()),  # main menu
    path('server_data/<str:server_name>', views.data_view),  # server_data + server_info
    path('server_call/<str:server_name>/', views.server_call),  # server_call, connection with server
    # path('accounts/logout/', views.logout),
    path('accounts/', include('django.contrib.auth.urls')),  # idk
    # path('main_menu', views.main_menu)  # real main menu
    path('', views.main_menu)  # real main menu
]
