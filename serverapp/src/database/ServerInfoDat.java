package database;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Time;
import java.text.SimpleDateFormat;

public class ServerInfoDat {

    private String serverName;
    private int cpu;
    private int RAM;
    private Date date;
    private Time time;

    public ServerInfoDat(String serverName, int cpu, int RAM, Date date,Time time){
        this.serverName = serverName;
        this.cpu = cpu;
        this.RAM = RAM;
        this.date = date;
        this.time = time;
    }
    public ServerInfoDat(String serverName,int cpu, int RAM){
        this.serverName = serverName;
        this.cpu = cpu;
        this.RAM = RAM;
        Date date = Date.valueOf(
                new SimpleDateFormat("yyyy-MM-dd").format(new java.util.Date()));
        Time time = Time.valueOf(
                new SimpleDateFormat("HH:mm:ss").format(new java.util.Date()));
        this.date = date;
        this.time = time;
    }

    public ServerInfoDat(ResultSet rs){
        try {
            this.serverName = rs.getString(1);
            this.cpu = rs.getInt(2);
            this.RAM = rs.getInt(3);
            this.date = rs.getDate(4);
            this.time = rs.getTime(5);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public String getServerName() {
        return serverName;
    }

    public int getCpu() {
        return cpu;
    }

    public int getRAM() {
        return RAM;
    }

    public Date getDate() {
        return date;
    }
    public Time getTime(){
        return time;
    }

    public String getString(){
        return this.getServerName()+","+getCpu()+","+getRAM()+","+getDate()+","+getTime();
    }

}
