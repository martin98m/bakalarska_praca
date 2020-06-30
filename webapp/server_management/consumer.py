import json

from channels.generic.websocket import WebsocketConsumer

from server_management.SocketConnectionToServer.SocketManager import SocketManager
from server_management.models import ServerInfo

sm = SocketManager()


class ChatConsumer(WebsocketConsumer):

    def get_server_info(self, server_name):
        s_info = ServerInfo.objects.all().filter(server_name=server_name)
        return s_info

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
        s_info = self.get_server_info(text_data_json['server'])

        try:
            info = text_data_json['info']
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
                    print("Socket already in use")

                soc.connect()

                if soc.is_connected():
                    self.send(text_data=json.dumps(
                        {'info': 'True'}
                    ))
                else:
                    self.send(text_data=json.dumps(
                        {'info': 'False'}
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
                        {'info': 'DISCONNECTED'}
                    ))
            elif info == "RESTART":
                soc = sm.get_socket(
                    user_name=str(self.user),
                    server_name=str(s_info[0].server_name)
                )
                print(soc)
                print("XXX")
                print(soc)
                soc.restart_communication()
                self.send(text_data=json.dumps(
                    {'info': 'RESTARTED'}
                ))
        except KeyError:
            pass

        try:
            message = text_data_json['message']
            # self.send(text_data=json.dumps(
            #     {'response': message}
            # ))
            soc = sm.get_socket(str(self.user), s_info[0].server_name)
            if soc is None:
                soc = soc = sm.add_socket(
                    name=str(self.user),
                    server_name=text_data_json['server'],
                    host=s_info[0].server_ip,
                    port=s_info[0].server_port
                )
            if not soc.is_connected():
                soc.connect()

            msg_back = soc.send_message_with_response(message=message, is_command=True)

            for line in msg_back:
                self.send(text_data=json.dumps(
                    {'response': line}
                ))
        except KeyError:
            pass

