package cmd;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class CommandPrompt {

    private Process process = null;
    private BufferedReader bufferedReader = null;


    public void runCommand(String command) throws InterruptedException {
        try {
            process = Runtime.getRuntime().exec(command);
        }catch (IOException e){
            e.printStackTrace();
        }
        process.waitFor();
        bufferedReader = new BufferedReader(new InputStreamReader(process.getInputStream()));
    }

    public BufferedReader getBufferedReader() {
        return bufferedReader;
    }

    public String getFirstLine(){
        try {
            return bufferedReader.readLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public ArrayList<String> getLinesInArray(){
        ArrayList<String> al = new ArrayList<>();

        BufferedReader br = bufferedReader;

        String line = "";
        while (true) {
            try {
                if (((line = br.readLine()) == null)) break;
            } catch (IOException e) {
                e.printStackTrace();
            }
            al.add(line);
        }
        return al;
    }
}
