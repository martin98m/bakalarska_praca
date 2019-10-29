package socket;

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

    private ServerSocket server = null;
    private int port;
    private int numOfConnections = 0;

    public void startSocketServer(){

        try {
            server = new ServerSocket(0);
            port = server.getLocalPort();
            sendPortToDatabse();
            System.out.println("Socket created and PORT updated in DB");

            while (true){
                System.out.println("waiting for connections on port:"+port);
                Socket client = server.accept();
                Thread socketConnection = new Thread(() -> handleConnection(client));
                socketConnection.start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //TODO currently reads messages sent from Python client
    private void handleConnection(Socket clientSocket){

        Socket client = clientSocket;
        String fromClient = null;

        numOfConnections++;
        System.out.println("got connection on port :" + port + client);

        try {
            BufferedReader in = new BufferedReader(new InputStreamReader(client.getInputStream()));
            PrintWriter out = new PrintWriter(client.getOutputStream(), true);

            System.out.println("send username+password");
            String username = in.readLine();
            String password = in.readLine();
//            if(!correctLogin(username,password))
//                return;
            System.out.println("login correct");
            boolean run = true;
            while (run) {
                if (!client.isConnected()) {
                    client.close();
                    break;
                }
                System.out.println("waiting for message");
                fromClient = in.readLine();
//                if(fromClient == null || fromClient.length() == 0) continue;
                System.out.println(client.getInetAddress().toString()+'|'+client.getPort()+"|sent: " + fromClient);
                if(fromClient == null){
                    numOfConnections--;
                    System.out.println("DISCONNECT?");
                    return;
                }
                if (fromClient.equals("EXIT2")) {
                    in.close();
                    out.close();
                    client.close();
                    run = false;
                    System.out.println("socket closed");
                }
                if(fromClient.equals("CONN")){
                    System.out.println(numOfConnections);
                }
            }
        } catch (SocketException e){
            System.out.println("Client disconnected");
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("HANDLE EXITING");
        numOfConnections--;
    }

    private void sendPortToDatabse(){
        Database db = new Database();
        ArrayList<Object> values = new ArrayList<>();
        values.add(Integer.valueOf(port));
        values.add(new GatherSystemInformation().getServerName());

        //todo remove print and add executeStatement
        System.out.println("PORT UPDATED TO DB:"+values);

        db.executeStatementNoReturnObjects(Database.updateServerPort,values);
    }

    //todo finish
    private boolean correctLogin(String username, String password){

        Database db = new Database();
//        db.executeStatementWithReturn(Database.)


        return false;
    }
/*
    public void startSocketServer(){
        String fromClient = null;
        String toClient = null;

        try {
            ServerSocket server = new ServerSocket(port);
            System.out.println("wait for connection on port:"+port);
            Socket client = server.accept();
            System.out.println("got connection on port :"+port);
            BufferedReader in = new BufferedReader(new InputStreamReader(client.getInputStream()));
            PrintWriter out = new PrintWriter(client.getOutputStream(),true);

            boolean run = true;
            while(run) {

                fromClient = in.readLine();
                System.out.println("received: " + fromClient);

                if(fromClient.equals("EXIT")) {
                    toClient = "EXIT";
                    out.println(toClient);
                    client.close();
                    run = false;
                    System.out.println("socket closed");
                }
            }
        }catch (SocketException e){
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
*/
}
