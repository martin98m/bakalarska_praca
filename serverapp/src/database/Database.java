package database;

import java.sql.*;
import java.util.ArrayList;

public class Database {

    private final String url = "jdbc:postgresql://localhost:5432/server_usage";
    private final String username = "postgres";
    private final String password = "postgres";

    public static final String getMainData = "SELECT * FROM server_data";
    public static final String insertData = "INSERT INTO server_info VALUES (?,?,?,?,?)";
    public static final String serverExists = "SELECT * FROM server_info WHERE server_name = ?";
    public static final String insertServer = "INSERT INTO server_info VALUES (?,?)";
    public static final String updateIP = "UPDATE server_info SET server_ip = ? WHERE server_name = ?";

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

    //todo
    public boolean sendDataToDatabase(ServerInfoDat dataPackage){
        connect();




        disconnect();
        return false;
    }

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
