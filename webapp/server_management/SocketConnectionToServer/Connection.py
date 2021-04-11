import socket

from server_management.SocketConnectionToServer.AESModule import AESModule


class ServerConnection:
    def __init__(self, host, port):
        self.host = host
        self.port = port
        self.sock = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
        self.sock.settimeout(5)
        self.connected = False
        self.crypt = AESModule()

    def connect(self):
        if self.connected is True:
            return

        try:
            # print(self.host, '|', self.port)
            self.sock.connect((self.host, self.port))
            self.connected = True
            print('SOCKET?CONNECT?SUCCESSFUL')
        except ConnectionError:
            self.connected = False
            print(ConnectionError)
        except socket.error:
            self.connected = False
            print('SOCKET_TIMEOUT_ERROR')

    def disconnect(self):
        self.sock.close()
        self.connected = False

    def is_connected(self):
        return self.connected

    def send_msg(self, message, is_command):
        if not self.connected:
            return

        enc_msg = self.crypt.encode_data(message)
        command = self.crypt.encode_data(str(is_command))
        self.sock.send(command)
        self.sock.send(enc_msg)

    def restart_communication(self):
        self.send_msg(message="RELOAD", is_command=False)

    def restart_data_gathering(self):
        self.send_msg(message="RESTART", is_command=False)

    def send_message_with_response(self, message, is_command):
        if not self.connected:
            return

        enc_msg = self.crypt.encode_data(message)
        command = self.crypt.encode_data(str(is_command))

        self.sock.send(command)
        self.sock.send(enc_msg)

        lines = []
        try:
            while True:
                enc_msg = self.sock.recv(8192)
                # print(len(enc_msg))
                # print(enc_msg)

                if len(enc_msg) == 0:
                    break
                try:
                    dec_msg = self.crypt.decode_data(enc_msg)
                    print(dec_msg)

                    lines.append(dec_msg)
                except ValueError:
                    print("smth wrong")

        except socket.timeout:
            pass

        return lines

    def send_message_new(self, message, is_command):
        if not self.connected:
            return

        enc_msg = self.crypt.encode_data(message)
        command = self.crypt.encode_data(str(is_command))
        self.sock.send(command)
        self.sock.send(enc_msg)

    def receive_message_new(self):
        lines = []

        try:
            enc_msg = self.sock.recv(8192)

            if len(enc_msg) == 0:
                return lines

            dec_msg = self.crypt.decode_data(enc_msg)
            print(dec_msg)
            lines.append(dec_msg)

        except socket.timeout:
            raise socket.timeout

        return lines

