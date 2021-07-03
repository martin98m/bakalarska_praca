package database;

import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;
import java.sql.*;

public class Database {

//    private static final String db_url = "jdbc:postgresql://balarama.db.elephantsql.com:5432/bcksfnyd";
    private static final String db_url = "jdbc:postgresql://tai.db.elephantsql.com/ypjqiede";
//    private static final String db_url = "jdbc:postgresql://localhost:5432/bp_server_management";
    private static final String db_username = "ypjqiede";
//    private static final String db_username = "postgres";
    private static final String db_password = "Lns8nJ1fx3WafeaCxSINXlvu78BIU39b";
//    private static final String db_password = "postgres";

    private static final String server_exists = "SELECT * FROM server_management_serverinfo WHERE server_name = ?";
    private static final String server_data_delay = "SELECT data_collection_delay_minutes FROM server_management_serverinfo WHERE server_name = ?";
    private static final String update_ip = "UPDATE server_management_serverinfo SET server_ip = ? WHERE server_name = ?";
    private static final String server_data = "INSERT INTO server_management_serverdata(server_name_id, cpu_usage, ram_usage, ram_capacity, date, time) VALUES (?,?,?,?,?,?)";
    private static final String server_info = "INSERT INTO server_management_serverinfo(server_name, server_alias, server_os, server_ip, server_port, data_collection_delay_minutes) VALUES(?,?,?,?,?,?);";
    private static final String server_port = "UPDATE server_management_serverinfo SET server_port = ? WHERE server_name = ?";

    private Connection dbConnection = null;

    private boolean connect(){

        try {
            //Connects to database using ^^ parameters
            dbConnection = DriverManager.getConnection(db_url, db_username, db_password);
            System.out.println("DATABASE connection successful");
        } catch (SQLException e){
            System.out.println(e.getMessage());
            return false;
        }

        return true;
    }

    //disconnects from database
    private boolean disconnect(){

        try {
            this.dbConnection.close();
            System.out.println("DATABASE disconnection successful");
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
        dbConnection = null;

        return true;
    }

    //sends Main Data to database
    public void sendDataToDatabase(ServerMeasuredData dataPackage){
        boolean connection;

        do {
            connection = connect();
            try {
                PreparedStatement ps = this.dbConnection.prepareStatement(server_data);
                ps.setString(1, dataPackage.getServerName());
                ps.setInt(2, dataPackage.getCpu());
                ps.setInt(3, dataPackage.getRAM());
                ps.setInt(4, dataPackage.getRAM_max());
                ps.setDate(5, dataPackage.getDate());
                ps.setTime(6, dataPackage.getTime());
                ps.execute();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }while (!connection);

        do{
            connection = disconnect();
        }while (!connection);
    }

    public void sendServerInfoToDatabase(ServerInformation serverInformation){

        boolean connection;

        do {

            connection = connect();

            try {
                PreparedStatement ps = this.dbConnection.prepareStatement(server_info);
                ps.setString(1, serverInformation.getServer_name());
                ps.setString(2, serverInformation.getServer_alias());
                ps.setString(3, serverInformation.getOs());
                ps.setString(4, serverInformation.getServer_ip());
                ps.setInt(5, serverInformation.getServer_port());
                ps.setInt(6, serverInformation.getData_collection_delay_minutes());

                ps.execute();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }while (!connection);
        do{
            connection = disconnect();
        }while (!connection);
    }

    public void updateServerPort(String server_name, int port){
        boolean connection;

        do {
            connection = connect();

            try {
                PreparedStatement ps = this.dbConnection.prepareStatement(server_port);
                ps.setInt(1, port);
                ps.setString(2, server_name);
                ps.execute();
            } catch (SQLException e) {
                e.printStackTrace();
            }

        }while (!connection);
        do {
            connection = disconnect();
        }while (!connection);
    }

    public boolean server_exists_in_db(String server_name){

        boolean connection;
        boolean ret_value = false;

        do{
            connection = connect();

            try {
                PreparedStatement ps = this.dbConnection.prepareStatement(server_exists);
                ps.setString(1,server_name);
                ResultSet rs = ps.executeQuery();
                if (rs.next())
                    if (rs.getString(1).equals(server_name)) ret_value = true;
            } catch (SQLException e) {
                e.printStackTrace();
            }

        }while (!connection);

        do{
            connection = disconnect();
        }while (!connection);

        return ret_value;
    }

    public int get_server_data_delay(String server_name){

        boolean connection;
        int ret_val = 5;

        do{
            connection = connect();

            try {
                PreparedStatement ps = this.dbConnection.prepareStatement(server_data_delay);
                ps.setString(1,server_name);
                ResultSet rs = ps.executeQuery();
                if (rs.next())
                    ret_val = rs.getInt(1);
            } catch (SQLException e) {
                e.printStackTrace();
                ret_val = 5;
            }

        }while (!connection);

        do{
            connection = disconnect();
        }while (!connection);

        return ret_val;
    }

    public void update_server_ip(String server_name, String server_ip){
        boolean connection;
        do {
            connection = connect();

            try {
                PreparedStatement ps = this.dbConnection.prepareStatement(update_ip);
                ps.setString(1, server_ip);
                ps.setString(2,server_name);
                ResultSet rs = ps.executeQuery();
            } catch (SQLException e) {
                e.printStackTrace();
            }

        }while (!connection);

        do{
            connection = disconnect();
        }while (!connection);

    }

    public PublicKey getWebAppPublicKey(){
        connect();

        PublicKey publicKey = null;
        try {
            PreparedStatement ps = this.dbConnection.prepareStatement(
                    "SELECT public_key FROM server_rsa WHERE server_name = 'WebApp'"
            );
            ResultSet rs = ps.executeQuery();
            byte[] publicInBytes = rs.getBytes(1);

            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            X509EncodedKeySpec publicKeySpec = new X509EncodedKeySpec(publicInBytes);
            publicKey = keyFactory.generatePublic(publicKeySpec);

        } catch (SQLException | NoSuchAlgorithmException | InvalidKeySpecException e) {
            e.printStackTrace();
        }

        disconnect();
        return publicKey;
    }

    public void updatePublicKeyToDatabase(byte[] publicKey, String serverName){
        connect();

        PreparedStatement ps = null;
        try {
            ps = this.dbConnection.prepareStatement(
                    "UPDATE server_info SET public_key = ? WHERE server_name = ?"
            );
            ps.setBytes(1, publicKey);
            ps.setString(2, serverName);
            ps.execute();

        } catch (SQLException e) {
            e.printStackTrace();
        }

        disconnect();
    }
}
