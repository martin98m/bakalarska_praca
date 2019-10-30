package cmd;

import database.Database;
import database.ServerInfoDat;

public abstract class OsInfoGathering{

    CommandPromptWIN cmd = null;

    String serverName;
    int CPU;
    int RAM;
    int totalRAM;
    ServerInfoDat sid = null;

    public void gatherInformation(){
        this.serverName = getServerName();
        this.CPU = getServerCPU();
        int[] ram = getServerRAM();
        this.totalRAM = ram[0];
        this.RAM = ram[1];

        sid = new ServerInfoDat(serverName,CPU,RAM,totalRAM);

        System.out.println("GATHERED INFO :");
        System.out.println(sid.getString());
    };

    public String getServerName(){
        return null;
    };
    public int getServerCPU() {
        return 0;
    };
    public int[] getServerRAM() {
        return new int[]{0};
    };

    public void sendDataToDatabase(){
        if (sid == null) System.out.println("SID je null [cmd.OsInfoGathering.java:50]");

        Database db = new Database();
        db.sendDataToDatabase(sid);
    };

}
