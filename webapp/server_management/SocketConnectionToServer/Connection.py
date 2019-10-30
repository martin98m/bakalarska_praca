import socket


class ServerConnection:

    def __init__(self, host, port):
        self.host = host
        self.port = port
        self.sock = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
        try:
            self.sock.connect((host, port))
            self.sock.sendall(b"admin\n")
            self.sock.sendall(b"admin\n")
        except ConnectionError:
            print(ConnectionError)

    def send_msg(self, message):
        msg = message.rstrip() + '\n'
        self.sock.sendall(msg.encode('UTF-8'))

    def disconnect(self):
        self.sock.close()

# while True:
#     msg = input() + '\n'  # \n is importatnt when sending
#     sock.sendall(msg.encode('UTF-8'))
#     print("S "+msg)