import java.sql.*;
import java.util.ArrayList;

public class Main {

    private String memoryCards = "wmic MEMORYCHIP get BankLabel, DeviceLocator, MemoryType, TypeDetail, Capacity, Speed";
    private String totalMemory = "systeminfo | findstr /C:”Total Physical Memory”";
    private String aviableMemory = "systeminfo |find “Available Physical Memory”";
    private String cpuUsage = "wmic cpu get loadpercentage";

    public static void main(String[] args) {

        Database database = new Database();

        Connection c = database.connect();

        ArrayList<ServerInfoDat> al = new ArrayList<>();
        try {
            ResultSet rs = c.prepareStatement(Database.getMainData).executeQuery();
            while(rs.next()){
                al.add(new ServerInfoDat(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        for (ServerInfoDat s: al) {
            System.out.println(s.getString());
        }
    }
}
