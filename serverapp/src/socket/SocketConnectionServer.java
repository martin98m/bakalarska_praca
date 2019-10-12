package socket;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class SocketConnectionServer {

    private int port = 9999;

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

        }catch (Exception e){
            e.printStackTrace();
        }
    }

}
