package cmd;

import java.io.*;
import java.net.SocketException;

public class CmdRunner implements Runnable {

    private Process process;
    private final BufferedReader in;
    private final PrintWriter out;
    private String firstCommand;

    public CmdRunner(BufferedReader in, PrintWriter out, String command){
        this.in = in;
        this.out = out;
        this.firstCommand = command;
        try {
            this.process = Runtime.getRuntime().exec("cmd");
        }catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public Process getProcess() {
        return process;
    }

    @Override
    public void run() {
        BufferedReader stdout =
                new BufferedReader(new InputStreamReader(process.getInputStream()));
        BufferedReader stderr =
                new BufferedReader(new InputStreamReader(process.getErrorStream()));
        BufferedWriter stdin =
                new BufferedWriter(new OutputStreamWriter(process.getOutputStream()));

        try {
            stdin.write(firstCommand+'\n');
            stdin.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }

        Thread write = new Thread(() -> {
            String cmnd;
            while (process.isAlive()) {
                try {
                    cmnd = in.readLine();
                    if(cmnd == null) {
                        System.out.println("ERROR reading null");
                        System.out.println(in.ready());
                        process.destroy();
                        break;
                    }
                    stdin.write(cmnd + "\n");
                    stdin.flush();
                    Thread.sleep(10);
                } catch (SocketException e){
                    process.destroy();
                   break;
                } catch (IOException | InterruptedException e){
                    e.printStackTrace();
                    break;
                }
            }
        });

        Thread readSTD  = new Thread(() -> readFrom(stdout));

        Thread readERR = new Thread(() -> readFrom(stderr));

        System.out.println(readERR.getName());
        System.out.println(readSTD.getName());
        System.out.println(write.getName());
            readERR.start();
            readSTD.start();
            write.start();

        try {
            process.waitFor();

            System.out.println("ENDING:");
            System.out.println(readSTD.isAlive());
            System.out.println(readERR.isAlive());
            System.out.println(write.isAlive());
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("CMD skoncil");
    }

    private void readFrom(BufferedReader buffer){
        String line = "";
        while (this.process.isAlive()){
            try {
                line = buffer.readLine();
                if(line == null) break;
                sendLineToWebApp(line);
            }catch (IOException e){
                e.printStackTrace();
                break;
            }
            System.out.println(line+'\n');
        }
    }

    private void sendLineToWebApp(String message){
        //sends how many lines will be send
//        out.println(message.length());
//        int i = 1;
//        for (String msg : message) {
            //1. sends length of line to read
            out.println(message.length());
//            System.out.println(i+"|"+msg);
            //2. sends line
            out.println(message);
//            i++;
//        }
    }
}
