package database;

import java.sql.*;
import java.util.ArrayList;

public class Database {

    public static final String serverExists = "SELECT * FROM server_info WHERE server_name = ?";
    public static final String updateIP = "UPDATE server_info SET server_ip = ? WHERE server_name = ?";
    public static final String getSleepTime = "SELECT data_collection_delay_minutes FROM server_info WHERE server_name = ?";
    public static final String userLogin = "SELECT * FROM user_login WHERE  username = ? AND password = ?";

    private Connection dbConnection = null;

    private void connect(){
        //todo update later for production
        String url = "jdbc:postgresql://balarama.db.elephantsql.com:5432/bcksfnyd";
        String username = "bcksfnyd";
        String password = "Hz5DGV-kLNqRVaEtU7R8PqPy_qwGZ9G1";
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

    //sends Main Data to database
    public void sendDataToDatabase(ServerMeasuredData dataPackage){
        connect();

        try {
            PreparedStatement ps = this.dbConnection.prepareStatement(
                    "INSERT INTO server_data(server_name, cpu_usage, ram_usage, ram_capacity, date, time) VALUES (?,?,?,?,?,?)");
            ps.setString(1,dataPackage.getServerName());
            ps.setInt(2,dataPackage.getCpu());
            ps.setInt(3,dataPackage.getRAM());
            ps.setInt(4,dataPackage.getRAM_max());
            ps.setDate(5,dataPackage.getDate());
            ps.setTime(6,dataPackage.getTime());
            ps.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        disconnect();
    }

    public void sendServerInfoToDatabase(ServerInformation serverInformation){
        connect();

        try {
            PreparedStatement ps = this.dbConnection.prepareStatement(
                    "INSERT INTO server_info(server_name, server_alias, os, server_ip, server_port, data_collection_delay_minutes) VALUES(?,?,?,?,?,?);");
            ps.setString(1,serverInformation.getServer_name());
            ps.setString(2,serverInformation.getServer_alias());
            ps.setString(3,serverInformation.getOs());
            ps.setString(4,serverInformation.getServer_ip());
            ps.setInt(5,serverInformation.getServer_port());
            ps.setInt(6,serverInformation.getData_collection_delay_minutes());

            ps.execute();
        }catch (SQLException e){
            e.printStackTrace();
        }

        disconnect();
    }

    public void updateServerPort(String server_name, int port){
        connect();

        try {
            PreparedStatement ps = this.dbConnection.prepareStatement(
                    "UPDATE server_info SET server_port = ? WHERE server_name = ?"
            );
            ps.setInt(1, port);
            ps.setString(2, server_name);
            ps.execute();
        }catch (SQLException e){
            e.printStackTrace();
        }

        disconnect();
    }

    //executes statement and returns data in formated Array of strings, based on expected number of columns
    //number of expected columns means num of results in array - >{name,ip,port} -> exp ==4 -> {name,ip,port,name}
    //should return only 1 line of results
    public ArrayList<String> executeStatementWithReturn(String ps, ArrayList<String> values,int expectedReturnedColumns){
        connect();

        ArrayList<String> arrayList = new ArrayList<>();
        ResultSet rs = null;

        try {
            PreparedStatement preparedStatement = this.dbConnection.prepareStatement(ps);
            int i = 1;
            for (String value:values) {
                preparedStatement.setString(i, value);
                i++;
            }
            rs = preparedStatement.executeQuery();

            //if result set is empty - return empty array
            if(!rs.next()) return arrayList;

            i = 1;
            String data;
            while(i <= expectedReturnedColumns){
                data = rs.getString(i);
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
            preparedStatement = this.dbConnection.prepareStatement(ps);
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
            PreparedStatement preparedStatement = this.dbConnection.prepareStatement(ps);
            int i = 1;
            for (Object value:values) {
                if(value instanceof String)
                    preparedStatement.setString(i, (String) value);
                else if(value instanceof Integer)
                    preparedStatement.setInt(i, (int) value);
                else if(value == null)
                    preparedStatement.setNull(i, Types.INTEGER );
                i++;
            }
            preparedStatement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        disconnect();
    }

    public void executeStatementNoReturnStrings(String ps, ArrayList<String> values){
        connect();

        try {
            PreparedStatement preparedStatement = this.dbConnection.prepareStatement(ps);
            int i = 1;
            for (String value:values) {
                preparedStatement.setString(i, value);
                i++;
            }
            preparedStatement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        disconnect();
    }
}
