import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class Main {

    public static void main(String[] args) {
	// write your code here
        System.out.println("Hello world");

        Database database = new Database();

        Connection c = database.connect();

        ArrayList<ServerInfoDat> al = new ArrayList<>();
        String command = "SELECT * FROM server_info";
        try {
            ResultSet rs = c.prepareStatement(command).executeQuery();
            while(rs.next()){
                al.add(new ServerInfoDat(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        for (ServerInfoDat s: al) {
            System.out.println(s.getString());
        }
    }
}
