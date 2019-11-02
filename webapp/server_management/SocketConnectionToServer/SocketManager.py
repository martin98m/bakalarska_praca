from server_management.SocketConnectionToServer.Connection import ServerConnection


class SocketManager:
    class __SocketManager:
        def __init__(self):
            # print('Creting new SocketManager')
            # self.list_of_connection = {'name': 10}
            self.list_of_connections = {}

    instance = None

    def __init__(self):
        # print('Calling SocketManager')
        if self.instance is None:
            SocketManager.instance = SocketManager.__SocketManager()

    def get_socket(self, name):
        try:
            socket = self.instance.list_of_connections.get(name)
        except KeyError:
            raise Exception('Socket does not exist')

        return socket

    # todo connect to different server host/port dont match
    def add_socket(self, name, host, port):
        socket = ServerConnection(host, port)

        exist = None
        # exist = self.instance.list_of_connection.get(name)
        if exist is not None:
            raise Exception('Error adding socket, name is already in use')
        self.instance.list_of_connections[name] = socket
        return self.get_socket(name)

    def remove_socket(self, name):
        socket = self.instance.list_of_connections.get(name)
        if socket is None:
            raise Exception("Removing Failed, socket does not exist")
        print('RemovingSocket: ' + socket)
        socket.disconnect()
        self.instance.list_of_connections.pop(name)

    def num_of_connections(self):
        return len(self.instance.list_of_connections)

    def print_connections(self):
        print("PRINTING SOCKETS")
        for name, socket in self.instance.list_of_connections.items():
            print(name, socket)
