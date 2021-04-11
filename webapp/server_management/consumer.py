import json

from channels.generic.websocket import WebsocketConsumer

from server_management.SocketConnectionToServer.SocketManager import SocketManager
from server_management.models import ServerInfo, UserCommands

import django.contrib.auth.models as user_model


sm = SocketManager()


class ServerController(WebsocketConsumer):

    def connect(self):
        self.user = self.scope["user"]
        if self.user.is_authenticated:
            self.accept()
        else:
            pass

    def disconnect(self, close_code):
        pass

    def receive(self, text_data):
        text_data_json = json.loads(text_data)
        s_info = ServerInfo.objects.all().filter(server_name=text_data_json['server'])
        info = text_data_json['info']

        try:
            if info == "CONNECT":
                soc = None
                try:
                    soc = sm.add_socket(
                        name=str(self.user),
                        server_name=s_info[0].server_name,
                        host=s_info[0].server_ip,
                        port=s_info[0].server_port
                    )
                except Exception:
                    soc = sm.get_socket(
                        user_name=self.user,
                        server_name=s_info[0].server_name
                    )

                soc.connect()

                if soc.is_connected():
                    print("CONNECT SUCC")
                    self.send(text_data=json.dumps(
                        {'connected': 'True'}
                    ))
                else:
                    print("CONNECT FAIL")
                    self.send(text_data=json.dumps(
                        {'connected': 'False'}
                    ))
            elif info == "DISCONNECT":
                soc = sm.remove_socket(
                    name=str(self.user),
                    server_name=str(s_info[0].server_name)
                )
                if sm.get_socket(
                        user_name=self.user,
                        server_name=s_info[0].server_name
                ) is None:
                    self.send(text_data=json.dumps(
                        {'connected': 'DISCONNECTED'}
                    ))
            elif info == "RESTART":
                soc = sm.get_socket(
                    user_name=str(self.user),
                    server_name=str(s_info[0].server_name)
                )
                soc.restart_communication()
                self.send(text_data=json.dumps(
                    {'connected': 'RESTARTED'}
                ))
            elif info == "REFRESH":
                soc = sm.get_socket(
                    user_name=str(self.user),
                    server_name=str(s_info[0].server_name)
                )
                soc.restart_data_gathering()
        except KeyError:
            pass


class ServerCommunication(WebsocketConsumer):

    def connect(self):
        if self.scope["user"].is_authenticated:
            self.accept()
        else:
            pass

    def receive(self, text_data):

        user_name = str(self.scope["user"])
        text_data_json = json.loads(text_data)
        s_info = ServerInfo.objects.all().filter(server_name=text_data_json['server'])

        print(s_info)

        try:
            message = text_data_json['message']
            soc = sm.get_socket(user_name=user_name, server_name=s_info[0].server_name)
            if soc is None:
                soc = soc = sm.add_socket(
                    name=user_name,
                    server_name=text_data_json['server'],
                    host=s_info[0].server_ip,
                    port=s_info[0].server_port
                )
            if not soc.is_connected():
                soc.connect()

            if soc.is_connected():
                self.send(text_data=json.dumps(
                    {'connected': 'True'}
                ))

            msg_back = soc.send_message_with_response(message=message, is_command=True)

            for line in msg_back:
                self.send(text_data=json.dumps(
                    {'response': line}
                ))
        except KeyError:
            pass


class CommandConsumer(WebsocketConsumer):

    def connect(self):
        if self.scope["user"].is_authenticated:
            self.accept()
        else:
            pass

    def receive(self, text_data):
        text_data_json = json.loads(text_data)
        print(text_data_json)

        user = user_model.User.objects.get(username=str(self.scope["user"]))
        print(user)

        if text_data_json["info"] == "DELETE":
            UserCommands.objects.filter(
                username=user,
                command=text_data_json["command"],
                command_type=text_data_json["type"]).delete()
        elif text_data_json["info"] == "ADD":

            UserCommands.objects.create(
                username=user,
                command_type=text_data_json["type"],
                command=text_data_json["command"]
            )
