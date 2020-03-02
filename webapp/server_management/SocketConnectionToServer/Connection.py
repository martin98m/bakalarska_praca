import socket


# todo if new host/port are added dc from last
# todo if new host+port dont match discard old socket and add new

# todo add username + password to constructor
class ServerConnection:
    def __init__(self, host, port):
        self.host = host
        self.port = port
        self.sock = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
        self.sock.settimeout(1)  # reading ends after #1 sec of inactivity
        self.connected = False

    def get_sock(self):
        return self.sock

    def connect(self):
        try:
            print(self.host, '|', self.port)
            self.sock.connect((self.host, self.port))
            self.sock.sendall(b"admin\n")  # todo change
            self.sock.sendall(b"admin\n")
            self.connected = True
            print('SOCKET?CONNECT?SUCCESSFUL')
        except ConnectionError:
            self.connected = False
            print(ConnectionError)
        except socket.error:
            self.connected = False
            print('SOCKET_TIMEOUT_ERROR')

    def is_connected(self):
        return self.connected

    def send_msg(self, message):
        msg = message.rstrip() + '\n'
        self.sock.sendall(msg.encode('UTF-8'))
        return 'changed'

    def send_message_with_response(self, message):
        msg = message.rstrip() + '\n'
        self.sock.sendall(msg.encode('UTF-8'))
        # num = self.read_num()

        # num = int(resp.decode('utf-8'))
        # print(num)

        lines = []
        # while len(lines) != 10:
        cont = True
        while cont:
            try:
                read_num = self._read_num()
            except socket.error:
                cont = False
                break
            # print(read_num)
            line = self.sock.recv(read_num + 2)
            # print(line)
            line = line.decode('utf-8')
            # line = line.replace('\r', '')
            line = line.replace('\n', '')
            lines.append(line)

        return lines

    def _read_num(self):
        # print("READING NUMBER")
        number = []
        while True:
            a = self.sock.recv(1)
            # print(a)
            if a == b'\n':
                # print("n found")
                abc = "".join(number)
                # print(abc)
                return int(abc)
            number.append(a.decode('utf-8'))

    def disconnect(self):
        self.sock.close()
        self.connected = False
