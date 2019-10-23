import cmd.GatherSystemInformation;
import database.Database;
import socket.SocketConnectionServer;

import java.sql.*;
import java.util.ArrayList;

public class Main {

    private String memoryCards = "wmic MEMORYCHIP get BankLabel, DeviceLocator, MemoryType, TypeDetail, Capacity, Speed";

    public static void main(String[] args){

        System.out.println("Hello world");


//        getDataFromDB();

        FirstRun fr = new FirstRun();
        fr.firstRun();

        Thread connection = new Thread(){
            @Override
            public void run() {
                SocketConnectionServer server = new SocketConnectionServer();
                server.startSocketServer();
            }
        };

        //todo get sleep timer from DB
        int sleepTimer = 60000;
        Thread collectInfo = new Thread(){
            @Override
            public void run() {

                do {
                    try {
                        GatherSystemInformation g = new GatherSystemInformation();
                        g.gatherInformation();
                        g.sendDataToDatabase();
                        //todo check sleep timer from DB
                        //todo in WA ask if user wants to start immediate System test
                        Thread.sleep(sleepTimer);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }while (2>1);
            }
        };

        connection.start();
        collectInfo.start();


//        getDataFromDB();
    }

    private static void getDataFromDB(){
        Database database = new Database();

        ArrayList<String> values = new ArrayList<>();
        ResultSet rs = database.executeStatementWithReturn(Database.getMainData,values);

        try {

            while (rs.next()) {
                System.out.println(
                        rs.getString(1)+"\t"+
                                rs.getInt(2)+"\t"+
                                rs.getInt(3)+"\t"+
                                rs.getDate(4)+"\t"+
                                rs.getTime(5)
                        );
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
    }
}
