import socket


from server_management.SocketConnectionToServer.AESModule import AESModule


class ServerConnection:
    def __init__(self, host, port):
        self.host = host
        self.port = port
        self.sock = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
        self.connected = False
        self.crypt = AESModule()

    def connect(self):
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

    def send_msg(self, message):
        enc_msg = self.crypt.encode_data(message)
        self.sock.send(enc_msg)
        return 'changed'

    def send_message_with_response(self, message):

        enc_msg = self.crypt.encode_data(message)
        self.sock.send(enc_msg)

        lines = []
        print("X")
        enc_msg = self.sock.recv(1024)
        print(enc_msg)
        dec_msg = self.crypt.decode_data(enc_msg)
        print(dec_msg)
        return dec_msg

        # while True:
        #     try:
        #         read_num = self._read_num()
        #     except socket.error:
        #         SERVER HAS NOT SEND ANYTHING FOR 2sec
                # break
            # print(read_num)
            # if read_num == -1:
            #     return lines
            # line = self.sock.recv(read_num + 2)
            # print(line)
            # try:
            #     line = line.decode('utf-8')
            # except UnicodeDecodeError:
            #     return lines
            # else:
            #     line = line.replace('\r', '')
                # line = line.replace('\n', '')
                # lines.append(line)
        #
        # return lines
