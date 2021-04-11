package cmd;

import socket.SocketConnectionServer;

import java.io.*;

public class Console {

    private Process process;
    private BufferedReader stdout;
    private BufferedReader stderr;
    private BufferedWriter stdin;

    private PrintWriter out;

    private Thread readSTD;
    private Thread readERR;

    public Console(PrintWriter out){
        this.out = out;
    }

    public void startConsole() throws IOException {

        this.process = Runtime.getRuntime().exec("cmd");

        this.defaultSetup();
    }

    public void startProcess(String process) throws IOException{
        this.process = Runtime.getRuntime().exec(process);

        this.defaultSetup();
    }

    private void defaultSetup(){
        this.stdout =
                new BufferedReader(new InputStreamReader(this.process.getInputStream()));
        this.stderr =
                new BufferedReader(new InputStreamReader(this.process.getErrorStream()));
        this.stdin =
                new BufferedWriter(new OutputStreamWriter(this.process.getOutputStream()));

        this.readSTD  = new Thread(() -> read(stdout));
        this.readERR  = new Thread(() -> read(stderr));
        this.readSTD.start();
        this.readERR.start();
    }

    public void stop(){
        this.readERR.interrupt();
        this.readSTD.interrupt();
        System.out.println(this.process);
        this.process.destroy();
        System.out.println(this.process);
    }


    public void executeCommand(String command){
        System.out.println(this.process);
        try {
            stdin.write(command + '\n');

            stdin.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void read(BufferedReader buffer) {
        String line = "";
        int count = 0;
        while (this.process.isAlive()) {
            try {
                line = buffer.readLine();
                System.out.println(line);
                if (line == null )
                    break;
                count += line.length();
            } catch (IOException e) {
                e.printStackTrace();
                break;
            }
            if (line.length() == 0)
                continue;
//            System.out.println(line);
//            System.out.println(process.isAlive());
//            System.out.println(Arrays.toString(line.getBytes()));
//            if (line.equals(this.controlValue)) {
//                SocketConnectionServer.send_message("OVER", this.out);
//                continue;
//            }
            System.out.println(count);
            SocketConnectionServer.send_message(line, this.out);
        }
    }
/*
    public static void main(String[] args) throws IOException {
        Console s = new Console(null);
        s.start();
        Scanner scanner = new Scanner(System.in);
        while (true) {
            String str = scanner.nextLine();
            s.executeCommand(str);
        }
    }*/
}
