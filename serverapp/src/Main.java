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


        getDataFromDB();
//        FirstRun fr = new FirstRun();
//        fr.firstRun();
//        SocketConnectionServer server = new SocketConnectionServer();
//        server.startSocketServer();
//        GatherSystemInformation g = new GatherSystemInformation();
//        g.gatherInformation();

//        getDataFromDB();
    }

    private static void getDataFromDB(){
        Database database = new Database();

        ArrayList<String> values = new ArrayList<>();
        ResultSet rs = database.executeStatementWithReturn(Database.getMainData,values);

        try {

            while (rs.next()) {
                System.out.println(
                        rs.getString(1)+"|"+
                                rs.getInt(2)+"|"+
                                rs.getInt(3)+"|"+
                                rs.getDate(4)+"|"+
                                rs.getTime(5)
                        );
            }
        }catch (SQLException e){
            e.printStackTrace();
        }


    }
}
