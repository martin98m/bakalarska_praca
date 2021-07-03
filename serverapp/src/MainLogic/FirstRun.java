package MainLogic;

import cmd.GatherSystemInformation;
import database.Database;
import database.ServerInformation;

class FirstRun {
    public static int global = 0;

    void firstRun(){
        Database db = new Database();

        GatherSystemInformation g = new GatherSystemInformation();

        //SERVER wasnt found in database/doesnt exist/this is new server
        if(!db.server_exists_in_db(g.getServerName())) {
            System.out.println("Server does not exist in database");
//            todo get OS info
            //server_port is added when socket is created, delay is 5 by default
            ServerInformation serverInformation =
                    new ServerInformation(g.getServerName(),null,g.getSystemOs(),g.getServerIP(), 0, 5);
            db.sendServerInfoToDatabase(serverInformation);
        }
        //server was found in DB
        else {

            int delay = db.get_server_data_delay(g.getServerName());
            System.out.println("Delay set to " + delay + " minutes");
            MainLogic.sleepBetweenMeasurement = delay*60000;//*60000 is for millis

        }

    }
}