from django.urls import path

from . import views
from server_management.views import ServerDataView, ServerInfoView


app_name = 'server_management'
urlpatterns = [
    path('', ServerInfoView.as_view()),
    path('server_data/', ServerDataView.as_view()),
    path('server_call/<str:server_name>/', views.server_call)
]

