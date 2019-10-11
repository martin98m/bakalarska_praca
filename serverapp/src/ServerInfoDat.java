import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

public class ServerInfoDat {

    private String serverName;
    private int cpu;
    private int RAM;
    private Date date;

    public ServerInfoDat(String serverName, int cpu, int RAM, Date date){
        this.serverName = serverName;
        this.cpu = cpu;
        this.RAM = RAM;
        this.date = date;
    }
    public ServerInfoDat(ResultSet rs){
        try {
            this.serverName = rs.getString(1);
            this.cpu = rs.getInt(2);
            this.RAM = rs.getInt(3);
            this.date = rs.getDate(4);
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

    public String getString(){
        return this.getServerName()+"|"+getCpu()+"|"+getRAM()+"|"+getDate();
    }

}
