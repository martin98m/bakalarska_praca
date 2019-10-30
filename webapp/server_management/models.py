from django.db import models

# Create your models here.


class ServerData(models.Model):
    server_name = models.CharField(max_length=255, primary_key=True)
    cpu_usage = models.IntegerField()
    ram_capacity = models.IntegerField()
    ram_max = models.IntegerField()
    date = models.DateField()
    time = models.TimeField()

    class Meta:
        managed = False
        db_table = 'server_data'


class ServerInfo(models.Model):
    server_name = models.CharField(unique=True, max_length=255, primary_key=True)
    server_ip = models.CharField(max_length=16)
    server_port = models.IntegerField()
    time_between_next_data_collection = models.IntegerField()

    class Meta:
        managed = False
        db_table = 'server_info'


class UserLogin(models.Model):
    username = models.CharField(unique=True, max_length=255)
    password = models.CharField(max_length=255)

    class Meta:
        managed = False
        db_table = 'user_login'

