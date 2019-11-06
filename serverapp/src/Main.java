import cmd.GatherSystemInformation;
import database.Database;
import socket.SocketConnectionServer;

import java.util.ArrayList;

public class Main {

    public static int sleepBetweenMeasurement;
    public static void main(String[] args){

        System.out.println("Starting server management application...");

        //checks if ip is correct + sets time_between_system_checks + system test
        FirstRun fr = new FirstRun();
        fr.firstRun();

        //creates server socket for communication with webapp
        Thread connection = new Thread(){
            @Override
            public void run() {
                SocketConnectionServer server = new SocketConnectionServer();
                server.startSocketServer();
            }
        };
        connection.start();

        //system check in set interval + uploads data to database
        Thread collectInfo = new Thread(){
            @Override
            public void run() {
                while(true){
                    try {
                        Thread.sleep(sleepBetweenMeasurement);//time set in First run
                        GatherSystemInformation g = new GatherSystemInformation();
                        g.gatherInformation();
                        g.sendDataToDatabase();
//                        setSleepTime(g.getServerName());
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        };
//        collectInfo.start();

    }

    private static void setSleepTime(String serverName){
        Database db = new Database();
        ArrayList<String> values = new ArrayList<>();
        values.add(serverName);
        ArrayList<String> result = db.executeStatementWithReturn(Database.getSleepTime,values,1);
        sleepBetweenMeasurement = Integer.parseInt(result.get(0));
    }
}
