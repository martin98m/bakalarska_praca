package cmd;

import cmd.CommandPromptWIN;
import cmd.OsInfoGathering;

public class GatherSystemInformation extends OsInfoGathering {


    public GatherSystemInformation(){
        cmd = new CommandPromptWIN();
    }

    protected void sendDataToDatabase() {
        super.sendDataToDatabase();
    }

    @Override
    protected void getServerName(){
        String serverNameCommand = "hostname";

        try {
            cmd.runCommand(serverNameCommand);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        //pc name is 1st line so .get(0)
        this.serverName = cmd.getArrayList().get(0);
    }

    @Override
    protected void getServerCPU(){
        String cpuUsage = "wmic cpu get loadpercentage";

        try {
            cmd.runCommand(cpuUsage);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        //3rd line of output is processor usage out of 100%
        this.CPU = Integer.valueOf(cmd.getArrayList().get(2).trim());
    }

    @Override
    protected void getServerRAM(){
//        String totalMemory = "systeminfo | findstr ”Total Physical Memory”";
        String totalMem = "systeminfo";
        try {
            cmd.runCommand(totalMem);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        for (String line:cmd.getArrayList()) {
            if(line.contains("Total Physical Memory")) {
                line = line.replaceAll("[a-zA-Z,:]","").trim();
                this.totalRAM = Integer.parseInt(line);
            }
            if(line.contains("Available Physical Memory")){
                line = line.replaceAll("[a-zA-Z,:]","").trim();
                this.RAM = this.totalRAM - Integer.parseInt(line);
            }
        }
    }

    private void testCase(){
        this.serverName = "test";
        this.CPU = 12;
        this.totalRAM = 4096;
        this.RAM = 1231;
    }
}
