import cmd.CommandPrompt;
import cmd.OsInfoGathering;
import database.ServerInfoDat;

import java.sql.Time;
import java.sql.Date;
import java.text.SimpleDateFormat;

public class GatherSystemInformation extends OsInfoGathering {

    public GatherSystemInformation(){
        cmd = new CommandPrompt();
    }

    /*public void gatherInformation(){
        System.out.println("GATHERING INFO");
        getServerName();
        getServerCPU();
        getServerRAM();

//        testCase();

        Date date = Date.valueOf(
                new SimpleDateFormat("yyyy-MM-dd").format(new java.util.Date()));
        Time time = Time.valueOf(
                new SimpleDateFormat("HH:mm:ss").format(new java.util.Date()));

        ServerInfoDat sid = new ServerInfoDat(serverName,CPU,RAM,date,time);

        System.out.println("GATHERED INFO :");
        System.out.println(sid.getString());
        System.out.println("INFO GATHERING ENDED");
//        Database db = new Database();
//        db.sendDataToDatabase(sid);
    }*/

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
