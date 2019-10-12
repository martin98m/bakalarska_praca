package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Database {

    private final String url = "jdbc:postgresql://localhost:5432/server_usage";
    private final String username = "postgres";
    private final String password = "postgres";

    protected static final String getMainData = "SELECT * FROM server_info";
    protected static final String insertData = "INSERT INTO server_info VALUES (?,?,?,?,?)";

    private Connection DBconnection = null;

    public Connection connect(){

        try {
            //Connects to database using ^^ parameters
            DBconnection = DriverManager.getConnection(url,username,password);
            System.out.println("DATABASE connection successful");
        } catch (SQLException e){
            System.out.println(e.getMessage());
        }

        return DBconnection;
    }

    public void disconnect(){
        try {
            this.DBconnection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Connection getDBconnection() {
        return DBconnection;
    }
}
