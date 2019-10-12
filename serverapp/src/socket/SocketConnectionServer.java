package socket;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;

public class SocketConnectionServer {

    private int port = 9999;
    private ServerSocket server = null;

    public void startSocketServer(){

        try {
            server = new ServerSocket(port);
//            System.out.println("wait for connection on port:"+port);
            //TODO make new process
            int i = 0;
            while (i < 1){
                System.out.println("wait for connection on port:"+port);
                handleCommunication();
                i++;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    //TODO currently read messages sent from Python client
    private void handleCommunication(){

        Socket client = null;

        String fromClient = null;
//        String toClient = null;

        try {
            client = server.accept();
            System.out.println("got connection on port :" + port + client);

            BufferedReader in = new BufferedReader(new InputStreamReader(client.getInputStream()));
            PrintWriter out = new PrintWriter(client.getOutputStream(), true);

            boolean run = true;
            while (run) {
                if (!client.isConnected()) {
                    client.close();
                    break;
                }
                fromClient = in.readLine();
                System.out.println("received: " + fromClient);

                if (fromClient.equals("EXIT")) {
                    in.close();
                    out.close();
                    client.close();
                    run = false;
                    System.out.println("socket closed");
                }
            }
        } catch (SocketException e){
            System.out.println("Client disconnected");
        } catch (IOException e) {
            e.printStackTrace();
        }
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
