import cmd.GatherSystemInformation;
import database.Database;

import java.util.ArrayList;

public class FirstRun {

    public void firstRun(){
        Database db = new Database();

        GatherSystemInformation g = new GatherSystemInformation();
        String serverName = g.getServerName();
        String serverIP = g.getServerIP();

        ArrayList<String> values = new ArrayList<>();
        values.add(serverName);

        ArrayList<String> result = null;
        result = db.executeStatementWithReturn(Database.serverExists,values,4);

        values.clear();
        //SERVER wasnt found in database/doesnt exist/this is new server
        if(result.size() == 0) {
            System.out.println("DB result == 0");
            values.add(serverName);
            values.add(serverIP);
            values.add(null);//PORT will be added when SocketConnections starts
            values.add("60000");//default value - time between measuring system usage
            db.executeStatementNoReturn(Database.insertServerToDB,values);
        }
        //server was found in DB
        else {
            System.out.println("Zaznam existuje:" + result);
            Main.sleepBetweenMeasurement = Integer.parseInt(result.get(3));

            if(!result.get(1).equals(serverIP)){        //if IPs dont match they will be updated
                values.add(serverIP);
                values.add(serverName);
                db.executeStatementNoReturn(Database.updateIP,values);
            }
        }
    }
}