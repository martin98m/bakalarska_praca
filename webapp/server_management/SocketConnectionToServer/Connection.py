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
        return 'changed'

    def send_message_with_response(self, message):
        msg = message.rstrip() + '\n'
        self.sock.sendall(msg.encode('UTF-8'))
        num = self.read_num()

        # num = int(resp.decode('utf-8'))
        print("Xd")
        print(num)

        lines = []
        while len(lines) != num:
            read_num = self.read_num()
            print(read_num)
            line = self.sock.recv(read_num+2)
            print(line)
            line = line.decode('utf-8')
            # line = line.replace('\r', '')
            line = line.replace('\n', '')
            lines.append(line)

        return lines

    def read_num(self):
        print("READING NUMBER")
        number = []
        while True:
            a = self.sock.recv(1)
            print(a)
            if a == b'\n':
                print("n found")
                abc = "".join(number)
                print(abc)
                return int(abc)
            number.append(a.decode('utf-8'))

    def disconnect(self):
        self.sock.close()
