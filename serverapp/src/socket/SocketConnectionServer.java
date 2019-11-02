package socket;

import cmd.CmdRunner;
import cmd.CommandPromptWIN;
import cmd.GatherSystemInformation;
import database.Database;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;

//todo connect user to CMD
//todo check if connection was from logged user(name+password)
public class SocketConnectionServer {

    private int port;
    private int numOfConnections = 0;

    public void startSocketServer() {

        ServerSocket server;
        try {
            server = new ServerSocket(0);
            port = server.getLocalPort();
            sendPortToDatabase();
            System.out.println("Socket created and PORT updated in DB");

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
        System.out.println("SERVER OVER");
    }

    //TODO currently reads messages sent from Python client
    private void handleConnection(Socket clientSocket){

        String fromClient = null;

        numOfConnections++;
        System.out.println("got connection on port :" + port + clientSocket);

        try {
            BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);

            System.out.println("send username+password");
            String username = in.readLine();
            String password = in.readLine();
            System.out.println("name:"+username);
            System.out.println("pass"+password);
            if(!correctLogin(username,password)){
                System.out.println("incorrect username or password");
                return;
            }
            else
                System.out.println("login correct");

            while (true) {
                if (!clientSocket.isConnected()) {
                    in.close();
                    out.close();
                    clientSocket.close();
                    break;
                }
                System.out.println("waiting for message");
                fromClient = in.readLine();
                System.out.println(clientSocket.getInetAddress().toString()+'|'+ clientSocket.getPort()+"|sent: " + fromClient);
                if(fromClient == null){
                    in.close();
                    out.close();
                    clientSocket.close();
                    System.out.println("DISCONNECT?");
                    break;
                }
                else if (fromClient.equals("EXIT2")) {
                    in.close();
                    out.close();
                    clientSocket.close();
                    System.out.println("socket closed");
                    break;
                }
                else if(fromClient.equals("CONN")){
                    ArrayList<String> msg = new ArrayList<>();
                    msg.add(String.valueOf(numOfConnections));
//                    out.println(numOfConnections);
                    sendMessageToWebApp(out, msg);
//                    System.out.println(numOfConnections);
                }
                else if(fromClient.equals("CHANGE")){
                    System.out.println("CHANGE time_between_...");
                }
                else {
                    CmdRunner cmdRunner = new CmdRunner(in, out, fromClient);
//                    CommandLineHandler cld = new CommandLineHandler(in, out, fromClient);
//                    cld.run();
                    cmdRunner.run();
//                    CommandPromptWIN cmd = new CommandPromptWIN();
//                    cmd.runCommand(fromClient, true);
//                    ArrayList<String> response = (cmd.getStderr().size() == 0) ? cmd.getStdout() : cmd.getStderr();
                    System.out.println("OVER");
//                    sendMessageToWebApp(out, response);
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

    private void sendPortToDatabase(){
        Database db = new Database();
        ArrayList<Object> values = new ArrayList<>();
        values.add(port);
        values.add(new GatherSystemInformation().getServerName());

        //todo remove print and add executeStatement
        System.out.println("PORT UPDATED TO DB:"+values);

        db.executeStatementNoReturnObjects(Database.updateServerPort,values);
    }

    //todo finish
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
