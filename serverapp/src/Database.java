import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Database {

    private final String url = "jdbc:postgresql://localhost:5432/server_usage";
    private final String username = "postgres";
    private final String password = "postgres";

    public Connection connect(){
        Connection DBconnection = null;

        try {
            //Connects to database using ^^ parameters
            DBconnection = DriverManager.getConnection(url,username,password);
            System.out.println("DATABASE connection successful");
        } catch (SQLException e){
            System.out.println(e.getMessage());
        }

        return DBconnection;
    }


}
