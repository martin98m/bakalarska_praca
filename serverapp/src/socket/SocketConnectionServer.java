package socket;

import MainLogic.MainLogic;
import cmd.Console;
import cmd.GatherSystemInformation;
import database.Database;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;

public class SocketConnectionServer {

    private static AES aes;

    public void startSocketServer() {
        aes = new AES();
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
//            BufferedReader bis = new BufferedReader(new
//                    InputStreamReader(clientSocket.getInputStream()));

            PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);

            Console console = new Console(out);
            console.start();

            try {
                while (clientSocket.isConnected()) {

                    String is_command = receive_message(in);
                    String dec_msg = receive_message(in);

                    System.out.println(console);

                    if (is_command.equals("False")){

                        switch (dec_msg) {
                            case "RESTART": {
                                MainLogic.data_gathering.interrupt();
                                //MainLogic.data_gathering.start();
                                //send_message(dec_msg + "!", out);
                                break;
                            }
                            case "RELOAD": {
//                                console.stop();
//                                console = new Console(out);
                                console.stop();
                                console.start();
                                System.out.println("NEW CONSOLE");
                                //cmd = new CmdRunner(null, out, "x");
                                //send_message(dec_msg + "!", out);
                                break;
                            }
                        }

                    }else {
                        console.executeCommand(dec_msg);
                    }
                }
            }catch (SocketException e){
                System.out.println("Socked died");
                e.printStackTrace();
                System.exit(0);
            }
        } catch (IOException e) {
            e.printStackTrace();
        };
    }

    public static void send_message(String message, PrintWriter out){
        String enc_msg = aes.encrypt(message);
        System.out.println("Sending: " + message);
        out.print(enc_msg);
        out.flush();
    }

    private boolean command = true;

    public String receive_message(BufferedInputStream in) throws IOException{
        byte[] bytes = new byte[1024];
        int result = 0;

        if (command)
             result = in.read(bytes,0, 24);
        else
            result = in.read(bytes);

        command = !command;
        System.out.println("Rec:" + result);
        String enc_msg = new String(bytes).substring(0,result);

        return aes.decrypt(enc_msg);
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
