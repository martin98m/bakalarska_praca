from django.db import models


class ServerData(models.Model):
    server_name = models.CharField(max_length=255, primary_key=True)
    cpu_usage = models.IntegerField()
    ram_usage = models.IntegerField()
    ram_capacity = models.IntegerField()
    date = models.DateField()
    time = models.TimeField()

    class Meta:
        managed = False
        db_table = 'server_data'


class ServerInfo(models.Model):
    server_name = models.CharField(unique=True, max_length=255, primary_key=True)
    server_alias = models.CharField(max_length=255)
    os = models.CharField(max_length=255)
    server_ip = models.CharField(max_length=16)
    server_port = models.IntegerField()
    data_collection_delay_minutes = models.IntegerField()

    class Meta:
        managed = False
        db_table = 'server_info'


class UserLogin(models.Model):
    username = models.CharField(unique=True, max_length=255)
    password = models.CharField(max_length=255)

    class Meta:
        managed = False
        db_table = 'user_login'


class UserCommands(models.Model):
    username = models.CharField(max_length=255, primary_key=True)
    target = models.CharField(max_length=255)
    commandsave = models.CharField(max_length=255)

    class Meta:
        managed = False
        db_table = 'user_commands'
