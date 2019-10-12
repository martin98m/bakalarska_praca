import cmd.CommandPrompt;
import database.Database;
import database.ServerInfoDat;

import java.sql.*;
import java.util.ArrayList;

public class Main {

    private String memoryCards = "wmic MEMORYCHIP get BankLabel, DeviceLocator, MemoryType, TypeDetail, Capacity, Speed";

    public static void main(String[] args){

        System.out.println("Hello world");

        GatherSystemInformation g = new GatherSystemInformation();
        g.gatherInformation();
        if(2>1) return;


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
