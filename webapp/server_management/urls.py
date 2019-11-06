from django.urls import path, include

from server_management.views import ServerInfoView
from . import views

app_name = 'server_management'
urlpatterns = [
    path('', ServerInfoView.as_view()),
    path('server_data/<str:server_name>', views.data_view),
    path('server_call/<str:server_name>/', views.server_call),
    # path('accounts/logout/', views.logout),
    path('accounts/', include('django.contrib.auth.urls'))
]


