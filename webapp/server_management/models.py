from django.db import models
import django.contrib.auth as user_model


class ServerInfo(models.Model):
    server_name = models.CharField(max_length=255, primary_key=True, default=None, null=False)
    server_alias = models.CharField(max_length=255, default=None, null=True)
    server_os = models.CharField(max_length=255, default=None, null=False)
    server_ip = models.CharField(max_length=16, default=None, null=True)
    server_port = models.IntegerField(default=None, null=True)
    data_collection_delay_minutes = models.IntegerField(default=None, null=False)


class ServerData(models.Model):
    id = models.AutoField(primary_key=True)
    server_name = models.ForeignKey(ServerInfo, on_delete=models.CASCADE, default=None)
    cpu_usage = models.IntegerField(default=None)
    ram_usage = models.IntegerField(default=None)
    ram_capacity = models.IntegerField(default=None)
    date = models.DateField()
    time = models.TimeField()


class UserCommands(models.Model):
    username = models.ForeignKey(user_model.get_user_model(), on_delete=models.CASCADE)
    description = models.TextField(default=None)
    # command_type - command only for user or global
    command_type = models.CharField(max_length=255, default=None)
    # command that is saved
    command = models.CharField(max_length=255, default=None)
