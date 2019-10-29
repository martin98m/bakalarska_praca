import cmd.GatherSystemInformation;
import database.Database;
import socket.SocketConnectionServer;

import java.util.ArrayList;

public class Main {

    private String memoryCards = "wmic MEMORYCHIP get BankLabel, DeviceLocator, MemoryType, TypeDetail, Capacity, Speed";

    public static int sleepBetweenMeasurement;
    public static void main(String[] args){

        System.out.println("Starting server management application...");

        FirstRun fr = new FirstRun();
        fr.firstRun();

        Thread connection = new Thread(){
            @Override
            public void run() {
                SocketConnectionServer server = new SocketConnectionServer();
                server.startSocketServer();
            }
        };
        connection.start();

        Thread collectInfo = new Thread(){
            @Override
            public void run() {
                while(true){
                    try {
                        GatherSystemInformation g = new GatherSystemInformation();
                        g.gatherInformation();
                        g.sendDataToDatabase();
                        //todo in WA ask if user wants to start immediate System test
                        setSleepTime(g.getServerName());
                        Thread.sleep(sleepBetweenMeasurement);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        };

        collectInfo.start();

    }

    private static void setSleepTime(String serverName){
        Database db = new Database();
        ArrayList<String> values = new ArrayList<>();
        values.add(serverName);
        ArrayList<String> result = db.executeStatementWithReturn(Database.getSleepTime,values,1);
        sleepBetweenMeasurement = Integer.parseInt(result.get(0));
    }
}
