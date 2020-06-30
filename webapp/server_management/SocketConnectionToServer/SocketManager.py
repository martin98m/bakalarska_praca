from server_management.SocketConnectionToServer.Connection import ServerConnection


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
        socket = None
        try:
            print(self.instance.list_of_connections)
            user_connections = self.instance.list_of_connections.get(user_name)
            print(user_connections)
            if user_connections is None:
                return None
            else:
                socket = user_connections.get(server_name)
        except KeyError:
            pass

        return socket

    # todo connect to different server host/port dont match
    def add_socket(self, name, server_name, host, port):
        socket = self.get_socket(name, server_name)
        if socket is None:
            socket = ServerConnection(host, port)
            self.instance.list_of_connections.update({name: {server_name: socket}})
        else:
            raise Exception('Error adding socket, name is already in use')

        return socket

    def remove_socket(self, name, server_name):
        print(self.instance.list_of_connections)
        try:
            socket = self.instance.list_of_connections[name].get(server_name)
            if socket is None:
                print("Removing Failed, socket does not exist")
            socket.disconnect()
            self.instance.list_of_connections[name].pop(server_name)
            if len(self.instance.list_of_connections[name]) == 0:
                del self.instance.list_of_connections[name]

        except AttributeError:
            print("error")
            pass

    def print_connections(self):
        for name in self.instance.list_of_connections:
            print(name)

    def get_connections_for(self, username):
        result = self.instance.list_of_connections.get(username)
        return result
