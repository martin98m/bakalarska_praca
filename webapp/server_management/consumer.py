# chat/consumers.py
import json
import time

from channels.generic.websocket import WebsocketConsumer

from server_management.SocketConnectionToServer.SocketManager import SocketManager

sm = SocketManager()


class ChatConsumer(WebsocketConsumer):
    def connect(self):
        self.accept()

    def disconnect(self, close_code):
        pass

    def receive(self, text_data):
        text_data_json = json.loads(text_data)

        print('AAAAAAAAAAA' + text_data_json['message'])
        message = text_data_json['message']

        soc = sm.get_socket('mato', 'server')
        if soc is None:
            soc = soc = sm.add_socket('mato', 'server', '127.0.0.1', 5556)
        if soc.is_connected() is False:
            soc.connect()
        msg_back = soc.send_message_with_response(message)

        # i = 0
        # while i < 5:
        #     i = i + 1
        #     time.sleep(5)
        self.send(text_data=json.dumps(
                {'message': msg_back}
            # {'message': text_data_json['message']}
            ))
