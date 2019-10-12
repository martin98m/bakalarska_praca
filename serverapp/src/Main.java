import cmd.CommandPrompt;
import database.Database;
import database.ServerInfoDat;

import java.sql.*;
import java.util.ArrayList;

public class Main {

    private String memoryCards = "wmic MEMORYCHIP get BankLabel, DeviceLocator, MemoryType, TypeDetail, Capacity, Speed";
    private String totalMemory = "systeminfo | findstr /C:”Total Physical Memory”";
    private String aviableMemory = "systeminfo |find “Available Physical Memory”";
    private static String cpuUsage = "wmic cpu get loadpercentage";

    public static void main(String[] args){

        System.out.println("Hello world");
        CommandPrompt cmd = new CommandPrompt();
        try {
            cmd.runCommand(cpuUsage);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println("SIZE:"+cmd.getLinesInArray().size());
        for (String line:cmd.getLinesInArray()) {
            System.out.println(line);
        }


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
