package database;

import java.sql.*;
import java.util.ArrayList;

public class Database {

    //todo update later for production
    private final String url = "jdbc:postgresql://localhost:5432/server_usage";
    private final String username = "postgres";
    private final String password = "postgres";

    public static final String getMainData = "SELECT * FROM server_data";
    public static final String insertData = "INSERT INTO ? VALUES (?,?,?,?,?)";
    public static final String serverExists = "SELECT * FROM server_info WHERE server_name = ?";
    public static final String insertServerToDB = "INSERT INTO server_info VALUES (?,?,?,?)";
    public static final String updateIP = "UPDATE server_info SET server_ip = ? WHERE server_name = ?";
    public static final String updateValue = "UPDATE ? SET ? = ? WHERE ? = ?";

    private Connection DBconnection = null;

    //todo change values later
    private void connect(){

        try {
            //Connects to database using ^^ parameters
            DBconnection = DriverManager.getConnection(url,username,password);
            System.out.println("DATABASE connection successful");
        } catch (SQLException e){
            System.out.println(e.getMessage());
        }
    }

    //disconnects from database
    private void disconnect(){
        try {
            this.DBconnection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        DBconnection = null;
    }

    public Connection getDBconnection() {
        return DBconnection;
    }

    //sends Main Data to database
    public void sendDataToDatabase(ServerInfoDat dataPackage){
        connect();

        try {
            PreparedStatement ps = getDBconnection().prepareStatement(insertData);
            ps.setString(1,"server_data");
            ps.setString(2,dataPackage.getServerName());
            ps.setInt(3,dataPackage.getCpu());
            ps.setInt(4,dataPackage.getRAM());
            ps.setDate(5,dataPackage.getDate());
            ps.setTime(6,dataPackage.getTime());
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
            PreparedStatement preparedStatement = getDBconnection().prepareStatement(ps);
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
            preparedStatement = getDBconnection().prepareStatement(ps);
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
    public void executeStatementNoReturn(String ps, ArrayList<String> values){

        connect();

        try {
            PreparedStatement preparedStatement = getDBconnection().prepareStatement(ps);
            int i = 1;
            for (String value:values) {
                preparedStatement.setString(i,value);
                i++;
            }
            preparedStatement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        disconnect();
    }

}
