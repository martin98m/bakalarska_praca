package socket;

import cmd.CmdRunner;
import cmd.GatherSystemInformation;
import database.Database;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;

//todo check if connection was from logged user(name+password)
public class SocketConnectionServer {

    private int port;
    private int numOfConnections = 0;

    public void startSocketServer() {

        ServerSocket server;
        try {
            //creates new socket- par 0 means it will get any port that is available
            server = new ServerSocket(0);
            port = server.getLocalPort();
            new Database().updateServerPort(new GatherSystemInformation().getServerName(), port);

            while (true){
                System.out.println(server.getLocalSocketAddress()+"waiting for connections on port:"+port);
                Socket client = server.accept();
                System.out.println("client connected");
                Thread socketConnection = new Thread(() -> handleConnection(client));
                socketConnection.start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //TODO currently reads messages sent from Python client
    private void handleConnection(Socket clientSocket){

        String fromClient = null;

        numOfConnections++;
        System.out.println("got connection on port :" + port + clientSocket);

        try {
            BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);

            String username = in.readLine();
            String password = in.readLine();
            if(!correctLogin(username,password)) return;

            while (true) {
                if (!clientSocket.isConnected()) {
                    in.close();
                    out.close();
                    clientSocket.close();
                    break;
                }
                fromClient = in.readLine();
//                System.out.println(clientSocket.getInetAddress().toString()+'|'+ clientSocket.getPort()+"|sent: " + fromClient);
                if(fromClient == null){
                    in.close();
                    out.close();
                    clientSocket.close();
                    System.out.println("DISCONNECT?");
                    break;
                }
                else {
                    //user is connected to command line on server
                    CmdRunner cmdRunner = new CmdRunner(in, out, fromClient);
                    cmdRunner.run();
                }
            }
        } catch (SocketException e){
            //todo
            //cmndRunner.close()
            System.out.println("Client disconnected");
            Thread.currentThread().interrupt();
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("HANDLE EXITING");
        numOfConnections--;
    }

    private boolean correctLogin(String username, String password){

        Database db = new Database();
        ArrayList<String> values = new ArrayList<>();
        values.add(username);
        values.add(password);
        ArrayList result = db.executeStatementWithReturn(Database.userLogin, values, 2);
        return result.size() != 0;
    }

    private void sendMessageToWebApp(PrintWriter out,ArrayList<String> message){
        //sends how many lines will be send
        out.println(message.size());
        int i = 1;
        for (String msg : message) {
            //1. sends length of line to read
            out.println(msg.length());
//            System.out.println(i+"|"+msg);
            //2. sends line
            out.println(msg);
            i++;
        }
    }
}
