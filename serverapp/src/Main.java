import cmd.GatherSystemInformation;
import database.Database;
import database.ServerInfoDat;
import socket.SocketConnectionServer;

import java.sql.*;
import java.util.ArrayList;

public class Main {

    private String memoryCards = "wmic MEMORYCHIP get BankLabel, DeviceLocator, MemoryType, TypeDetail, Capacity, Speed";

    public static void main(String[] args){

        System.out.println("Hello world");

        FirstRun fr = new FirstRun();
        fr.firstRun();
//        SocketConnectionServer server = new SocketConnectionServer();
//        server.startSocketServer();
//        GatherSystemInformation g = new GatherSystemInformation();
//        g.gatherInformation();

//        getDataFromDB();
    }

    private static void getDataFromDB(){
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
