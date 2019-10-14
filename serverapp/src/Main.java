import cmd.GatherSystemInformation;
import database.Database;

import java.sql.*;
import java.util.ArrayList;

public class Main {

    private String memoryCards = "wmic MEMORYCHIP get BankLabel, DeviceLocator, MemoryType, TypeDetail, Capacity, Speed";

    public static void main(String[] args){

        System.out.println("Hello world");


//        getDataFromDB();
//        FirstRun fr = new FirstRun();
//        fr.firstRun();
//        SocketConnectionServer server = new SocketConnectionServer();
//        server.startSocketServer();
        GatherSystemInformation g = new GatherSystemInformation();
        g.gatherInformation();
        g.sendDataToDatabase();

        getDataFromDB();
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
