import cmd.GatherSystemInformation;
import database.Database;
import database.ServerInformation;

import java.util.ArrayList;

class FirstRun {

    void firstRun(){
        Database db = new Database();

        GatherSystemInformation g = new GatherSystemInformation();
        String serverName = g.getServerName();
        String serverIP = g.getServerIP();

        ArrayList<String> values = new ArrayList<>();
        values.add(serverName);

        ArrayList<String> result = null;
        result = db.executeStatementWithReturn(Database.serverExists,values,6);//6 is num of columns in db(server_info)

        //SERVER wasnt found in database/doesnt exist/this is new server
        if(result.size() == 0) {
            System.out.println("Server does not exist in database");
//            todo get OS info
            //server_port is added when socket is created, delay is 5 by default
            ServerInformation serverInformation =
                    new ServerInformation(serverName,serverName, new GatherSystemInformation().getSystemOs(), serverIP, 0, 5);
            db.sendServerInfoToDatabase(serverInformation);
        }
        //server was found in DB
        else {
            Main.sleepBetweenMeasurement = Integer.parseInt(result.get(5))*60000;//5 is for sleep_between_measures *60000 is for millis

            //if IPs dont match they will be updated
            if(!result.get(4).equals(serverIP)){
                values.clear();
                values.add(serverIP);
                values.add(serverName);
                db.executeStatementNoReturnStrings(Database.updateIP,values);
            }
        }
    }
}