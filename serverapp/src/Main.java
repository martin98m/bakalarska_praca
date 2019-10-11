import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Main {

    public static void main(String[] args) {
	// write your code here
        System.out.println("Hello world");

        Database database = new Database();

        Connection c = database.connect();


        String command = "SELECT * FROM server_info";
        try {
            ResultSet rs = c.prepareStatement(command).executeQuery();
            while(rs.next())
                System.out.println(rs.getString(1)+" "+
                        rs.getString(2)+" "+
                        rs.getString(3)+" "+
                        rs.getString(4));
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
