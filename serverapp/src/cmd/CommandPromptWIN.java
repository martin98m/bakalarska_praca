package cmd;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class CommandPromptWIN{

    private ArrayList<String> arrayList = null;

    public void runCommand(String command, boolean needsCmd){

        Process process = null;
        BufferedReader br = null;
        arrayList = new ArrayList<>();
        if(needsCmd) command = "cmd /c " + command;
        try {
            process = Runtime.getRuntime().exec(command);
            process.waitFor();
//            System.out.println("eXIT: "+process.exitValue());
        }catch (IOException | InterruptedException e){
            e.printStackTrace();
        }

        br = new BufferedReader(new InputStreamReader(process.getInputStream()));

        String line = "";
        while (true) {
            try {
                if (((line = br.readLine()) == null)) break;
            } catch (IOException e) {
                e.printStackTrace();
            }
            arrayList.add(line);
        }
    }

    public ArrayList<String> getArrayList() {
        return arrayList;
    }
}
