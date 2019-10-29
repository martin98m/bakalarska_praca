package database;

import java.sql.*;
import java.util.ArrayList;

public class Database {

    //todo update later for production
    private final String url = "jdbc:postgresql://localhost:5432/server_usage";
    private final String username = "postgres";
    private final String password = "postgres";

    public static final String getMainData = "SELECT * FROM server_data";
    public static final String insertData = "INSERT INTO server_data VALUES (?,?,?,?,?,?)";
    public static final String serverExists = "SELECT * FROM server_info WHERE server_name = ?";
    public static final String insertServerToDB = "INSERT INTO server_info VALUES (?,?,?,?)";
    public static final String updateIP = "UPDATE server_info SET server_ip = ? WHERE server_name = ?";
    public static final String updateServerInfo = "UPDATE server_info SET ? = ? WHERE ? = ?";
    public static final String checkLogin = "SELECT * FROM user_login WHERE username = ? AND password = ?";
    public static final String getSleepTime = "SELECT time_between_next_data_collection FROM server_info WHERE server_name = ?";
    public static final String updateServerPort = "UPDATE server_info SET server_port = ? WHERE server_name = ?";

    private Connection dbConnection = null;

    private void connect(){

        try {
            //Connects to database using ^^ parameters
            dbConnection = DriverManager.getConnection(url,username,password);
            System.out.println("DATABASE connection successful");
        } catch (SQLException e){
            System.out.println(e.getMessage());
        }
    }

    //disconnects from database
    private void disconnect(){
        try {
            this.dbConnection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        dbConnection = null;
    }

    public Connection getDbConnection() {
        return dbConnection;
    }

    //sends Main Data to database
    public void sendDataToDatabase(ServerInfoDat dataPackage){
        connect();
        System.out.println(dataPackage.getString());
        try {
            PreparedStatement ps = getDbConnection().prepareStatement(insertData);
//            ps.setString(1,"server_data");
            ps.setString(1,dataPackage.getServerName());
            ps.setInt(2,dataPackage.getCpu());
            ps.setInt(3,dataPackage.getRAM());
            ps.setInt(4,dataPackage.getRAM_max());
            ps.setDate(5,dataPackage.getDate());
            ps.setTime(6,dataPackage.getTime());
//            System.out.println(ps);
            ps.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        disconnect();
    }

    //executes statement and returns data in formated Array of strings, based on expected number of columns
    //number of expected columns means num of results in array - >{name,ip,port} -> exp ==4 -> {name,ip,port,name}
    public ArrayList<String> executeStatementWithReturn(String ps, ArrayList<String> values,int expectedRetCol){

        connect();

        ArrayList<String> arrayList = new ArrayList<>();
        ResultSet rs = null;

        try {
            PreparedStatement preparedStatement = getDbConnection().prepareStatement(ps);
            int i = 1;
            for (String value:values) {
                preparedStatement.setString(i,value);
                i++;
            }
            rs = preparedStatement.executeQuery();

            if(!rs.next()) return arrayList;

            i = 1;
            String data;
            while(i<=expectedRetCol){
                data = rs.getString(i);
                System.out.println(data);
                arrayList.add(data);
                i++;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        disconnect();
        return arrayList;
    }

    //executes statement and returns raw data in ResultSet
    public ResultSet executeStatementWithReturn(String ps, ArrayList<String> values) {

        connect();
        ResultSet resultSet = null;

        PreparedStatement preparedStatement = null;
        try {
            preparedStatement = getDbConnection().prepareStatement(ps);
            int i = 1;
            for (String value:values) {
                preparedStatement.setString(i,value);
                i++;
            }
            resultSet = preparedStatement.executeQuery();

        } catch (SQLException e) {
            e.printStackTrace();
        }

        disconnect();
        return resultSet;
    }

    //executes statement but doesnt return any result
    public void executeStatementNoReturnObjects(String ps, ArrayList<Object> values){

        connect();

        try {
            PreparedStatement preparedStatement = getDbConnection().prepareStatement(ps);
            int i = 1;
            for (Object value:values) {
                if(value instanceof String)
                    preparedStatement.setString(i, (String) value);
                else if(value instanceof Integer)
                    preparedStatement.setInt(i, (int) value);
                i++;
            }
            System.out.println(preparedStatement);
            preparedStatement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        disconnect();
    }
    public void executeStatementNoReturnStrings(String ps, ArrayList<String> values){
        connect();

        try {
            PreparedStatement preparedStatement = getDbConnection().prepareStatement(ps);
            int i = 1;
            for (String value:values) {
                preparedStatement.setString(i, (String) value);
                i++;
            }
            System.out.println(preparedStatement);
            preparedStatement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        disconnect();
    }
}
