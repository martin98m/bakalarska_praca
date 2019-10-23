import cmd.GatherSystemInformation;
import database.Database;

import java.util.ArrayList;

public class FirstRun {

    //TODO server should add itself to database if it isnt there already
    //todo learn server ip
    public void firstRun(){
        Database db = new Database();

        GatherSystemInformation g = new GatherSystemInformation();
        String serverName = g.getServerName();
        String serverIP = g.getServerIP();

        String serverExists = "SELECT * FROM server_info WHERE serverName = ?";
        String insertServer = "INSERT INTO server_info VALUES (?,?)";
        //todo update ip sql
//        String updateServer = "UPDATE "

        ArrayList<String> values = new ArrayList<>();
        values.add(serverName);

        ArrayList<String> result = null;
        result = db.executeStatementWithReturn(Database.serverExists,values,2);

        values.clear();
        if(result.size() == 0) {
            System.out.println("DB result == 0");
            values.add(serverName);
            values.add(serverIP);
            db.executeStatementNoReturn(Database.insertServer,values);
        }
        else {
            System.out.println("Zaznam existuje:" + result);
            if(!result.get(1).equals(serverIP)){
                values.add(serverIP);
                values.add(serverName);
                db.executeStatementNoReturn(Database.updateIP,values);
            }
        }
    }
}
