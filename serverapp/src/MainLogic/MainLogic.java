package MainLogic;


import cmd.GatherSystemInformation;
import database.Database;
import socket.SocketConnectionServer;

public class MainLogic {

    public static int sleepBetweenMeasurement;

    public static Thread data_gathering;

    public static void start(){
        System.out.println("Starting server management application...");

        FirstRun fr = new FirstRun();

        data_gathering = new Thread(){
            @Override
            public void run() {
                while(true){
                    try {
                        GatherSystemInformation g = new GatherSystemInformation();
                        g.gatherInformation();
                        g.sendDataToDatabase();

                        int result = new Database().get_server_data_delay(g.getServerName());
                        sleepBetweenMeasurement = result * 60000;//*60000 is for millis

                        Thread.sleep(sleepBetweenMeasurement);//time set in First run
//                        setSleepTime(g.getServerName());
                    } catch (InterruptedException e) {
                        System.out.println("Data gathering stopped");
//                        e.printStackTrace();
                    }
                }
            }
        };

        //creates server socket for communication with webapp
        Thread connection = new Thread(){
            @Override
            public void run() {
                SocketConnectionServer server = new SocketConnectionServer();
                server.startSocketServer();
            }
        };

        fr.firstRun();
        data_gathering.start();
        connection.start();

    }
}
