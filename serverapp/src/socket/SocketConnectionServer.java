package socket;

import MainLogic.MainLogic;
import cmd.CmdRunner;
import cmd.GatherSystemInformation;
import database.Database;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;

public class SocketConnectionServer {

    private AES aes;

    public void startSocketServer() {
        this.aes = new AES();

        ServerSocket server;
        try {
            //creates new socket- par 0 means it will get any port that is available
            server = new ServerSocket(5556);
            int port = server.getLocalPort();
            new Database().updateServerPort(new GatherSystemInformation().getServerName(), port);

            while (true){
                System.out.println(server.getLocalSocketAddress()+"waiting for connections on port:"+port);
                Socket client = server.accept();
                System.out.println("Client connected");
                Thread socketConnection = new Thread(() -> handleConnection(client));
                socketConnection.start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void handleConnection(Socket clientSocket){
        try {
            BufferedInputStream in = new BufferedInputStream(clientSocket.getInputStream());
            PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);

            try {
                while (clientSocket.isConnected()) {
                    String dec_msg = receive_message(in);

                    System.out.println(dec_msg);
                    send_message(dec_msg + "!", out);
                    if (dec_msg.equals("RESTART")){
                        MainLogic.data_gathering.interrupt();
                        MainLogic.data_gathering.start();
                    }
                }
            }catch (SocketException e){
                System.out.println("Socked ded");
                e.printStackTrace();
                System.exit(0);
            }
        } catch (IOException e) {
            e.printStackTrace();
        };

    }

    public void send_message(String message, PrintWriter out){
        String enc_msg = aes.encrypt(message);
        System.out.println("S: " + enc_msg);
        out.print(enc_msg);
        out.flush();
    }

    public String receive_message(BufferedInputStream in) throws IOException{
        byte[] bytes = new byte[1024];
        int result = in.read(bytes);
        String enc_msg = new String(bytes).substring(0,result);

        return this.aes.decrypt(enc_msg);
    }
/*
    //TODO currently reads messages sent from Python client
    private void handleConnection(Socket clientSocket){

        String fromClient = null;

        System.out.println("got connection on port :" + port + clientSocket);

        try {
            BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);

            String username = in.readLine();
            String password = in.readLine();
//            if(!correctLogin(username,password)) return;
            System.out.println("WEB APP IS IN");

            while (true) {
                if (!clientSocket.isConnected()) {
                    in.close();
                    out.close();
                    clientSocket.close();
                    break;
                }
                fromClient = in.readLine();
//                fromClient = in.readLine();
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
//            Thread.currentThread().interrupt();
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("HANDLE EXITING");
    }
*/
}
