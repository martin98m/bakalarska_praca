import cmd.CommandPromptWIN;
import cmd.GatherSystemInformation;
import database.Database;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class FirstRun {

    //TODO server should add itself to database if it isnt there already
    //todo learn server ip
    public void firstRun(){
        Database db = new Database();
        db.connect();

        GatherSystemInformation g = new GatherSystemInformation();
        String serverName = g.getServerName();
        String serverIP = g.getServerIP();

        String serverExists = "SELECT * FROM server_info WHERE serverName = ?";
        String insertServer = "INSERT INTO server_info VALUES (?,?)";
        //todo update ip sql
//        String updateServer = "UPDATE "
        try {
            PreparedStatement ps = db.getDBconnection().prepareStatement(serverExists);
            ps.setString(1,serverName);
            System.out.println("PS: "+ps);
            ResultSet rs = ps.executeQuery();
            //todo update IP of server if not matched
            if(rs.next()){
                if(!rs.getString(2).equals(serverIP))

                System.out.println("zaznam existuje IP: "+rs.getString(2));
            }else {
                //todo add to database
                System.out.println("zaznam neexistuje");
                ps = db.getDBconnection().prepareStatement(insertServer);
                ps.setString(1,serverName);
                ps.setString(2,serverIP);
                ps.execute();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }
}
