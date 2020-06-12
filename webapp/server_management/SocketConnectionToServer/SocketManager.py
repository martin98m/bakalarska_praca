import psycopg2

from server_management.SocketConnectionToServer.Connection import ServerConnection
from Crypto.PublicKey import RSA
# from crypto.Cipher import PKCS1_OAEP
# import binascii


class SocketManager:
    class __SocketManager:
        def __init__(self):
            self.list_of_connections = {}
            print('SOCKET MANAGER STARTED !!!!!!!!!!!!!!')
            # self.private_key = RSA.generate(1024)
            # pub_key = self.private_key.publickey()
            # print('PUB: ', pub_key)
            # conn = psycopg2.connect(
            #     host="postgres://bcksfnyd:Hz5DGV-kLNqRVaEtU7R8PqPy_qwGZ9G1@balarama.db.elephantsql.com:5432/",
            #     database="bcksfnyd",
            #     user="bcksfnyd",
            #     password="Hz5DGV-kLNqRVaEtU7R8PqPy_qwGZ9G1")

    instance = None

    def __init__(self):
        if self.instance is None:
            SocketManager.instance = SocketManager.__SocketManager()

    # returns socked based on user_name and server_name
    def get_socket(self, user_name, server_name):
        try:
            user_connections = self.instance.list_of_connections.get(user_name)
            if user_connections is None:
                return None
            else:
                socket = user_connections.get(server_name)
        except KeyError:
            raise Exception('Socket does not exist')

        return socket

    # todo connect to different server host/port dont match
    def add_socket(self, name, server_name, host, port):
        socket = ServerConnection(host, port)

        exist = None
        if exist is not None:
            raise Exception('Error adding socket, name is already in use')
        self.instance.list_of_connections.update({name: {server_name: socket}})
        return self.get_socket(name, server_name)

    def remove_socket(self, name, server_name):
        socket = self.instance.list_of_connections.get(name).get(server_name)
        if socket is None:
            print("Removing Failed, socket does not exist")
        print('RemovingSocket: ')
        socket.disconnect()
        print("DISCONNECTED SOCKET")
        self.instance.list_of_connections[name].pop(server_name)

    def print_connections(self):
        # print(self.instance.list_of_connections['admin'])
        for name in self.instance.list_of_connections.items():
            print(name)

    def get_connections_for(self, username):
        return self.instance.list_of_connections.get(username)
        # print(self.instance.list_of_connections['admin'])
        # for name in self.instance.list_of_connections.get(username):
        #     print(name)
