package cmd;

import database.Database;
import database.ServerInfoDat;

import java.sql.Date;
import java.sql.Time;
import java.text.SimpleDateFormat;

public abstract class OsInfoGathering{

    protected CommandPromptWIN cmd = null;

    protected String serverName;
    protected int CPU;
    protected int RAM;
    protected int totalRAM;
    protected ServerInfoDat sid = null;

    public void gatherInformation(){
        System.out.println("GATHERING INFO");
        this.serverName = getServerName();
        this.CPU = getServerCPU();
        int[] ram = getServerRAM();
        this.totalRAM = ram[0];
        this.RAM = ram[1];

//        testCase();

        Date date = Date.valueOf(
                new SimpleDateFormat("yyyy-MM-dd").format(new java.util.Date()));
        Time time = Time.valueOf(
                new SimpleDateFormat("HH:mm:ss").format(new java.util.Date()));

        sid = new ServerInfoDat(serverName,CPU,RAM,date,time);

        System.out.println("GATHERED INFO :");
        System.out.println(sid.getString());
        System.out.println("INFO GATHERING ENDED");
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

    protected void sendDataToDatabase(){
        if (sid == null) System.out.println("SID je null [cmd.OsInfoGathering.java:50]");

        Database db = new Database();
        db.connect();
        db.sendDataToDatabase(sid);
        db.disconnect();
    };

}
