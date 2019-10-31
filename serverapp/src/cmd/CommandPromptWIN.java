package cmd;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class CommandPromptWIN{

    private ArrayList<String> stdout = null;
    private ArrayList<String> stderr = null;

    public void runCommand(String command, boolean needsCmd){

        System.out.println("RUNNING COMMAND:"+command);
        Process process;
        BufferedReader out;
        BufferedReader err;

        stdout = new ArrayList<>();
        stderr = new ArrayList<>();
        if(needsCmd) command = "cmd /c " + command;
        try {
//todo handle error
            process = Runtime.getRuntime().exec(command);
            out = new BufferedReader(new InputStreamReader(process.getInputStream()));
            err = new BufferedReader(new InputStreamReader(process.getErrorStream()));
            String line = "";
            while (true) {
                try {
                    if (((line = out.readLine()) == null)) break;
                } catch (IOException e) {
                    e.printStackTrace();
                }
//                System.out.println(line);
                stdout.add(line);
            }
            while (true) {
                line = "";
                try {
                    line = err.readLine();
                    if (line  == null) break;
                } catch (IOException e) {
                    e.printStackTrace();
                }
//                System.out.println(line);
                stderr.add(line);
            }
            process.waitFor();
//            System.out.println("eXIT: "+process.exitValue());
        } catch (InterruptedException | IOException e) {
            e.printStackTrace();
        }

        System.out.println("COMMAND OVER");
    }

    public ArrayList<String> getStdout() {
        return stdout;
    }
    public ArrayList<String> getStderr(){
        return stderr;
    }
}
