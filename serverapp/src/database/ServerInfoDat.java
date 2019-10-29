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
    private int RAM_max;
    private Date date;
    private Time time;

    public ServerInfoDat(String serverName, int cpu, int RAM,int RAM_max, Date date,Time time){
        this.serverName = serverName;
        this.cpu = cpu;
        this.RAM = RAM;
        this.RAM_max = RAM_max;
        this.date = date;
        this.time = time;
    }

    public ServerInfoDat(String serverName,int cpu, int RAM, int RAM_max){
        this.serverName = serverName;
        this.cpu = cpu;
        this.RAM = RAM;
        this.RAM_max = RAM_max;
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
            this.RAM_max = rs.getInt(4);
            this.date = rs.getDate(5);
            this.time = rs.getTime(6);
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
    public int getRAM_max(){return RAM_max;}
    public Date getDate() {
        return date;
    }
    public Time getTime(){
        return time;
    }

    public String getString(){
        return this.getServerName()+","+getCpu()+","+getRAM()+","+getRAM_max()+","+getDate()+","+getTime();
    }

}