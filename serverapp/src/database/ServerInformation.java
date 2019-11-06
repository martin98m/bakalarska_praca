package database;

public class ServerInformation {

    private String server_name;
    private String server_alias;
    private String os;
    private String server_ip;
    private int server_port;
    private int data_collection_delay_minutes;

    public ServerInformation(String server_name, String server_alias, String os, String server_ip, int server_port, int data_collection_delay_minutes){
        this.server_name = server_name;
        this.server_alias = server_alias;
        this.os = os;
        this.server_ip = server_ip;
        this.server_port = server_port;
        this.data_collection_delay_minutes = data_collection_delay_minutes;
    }

    String getServer_name() {
        return server_name;
    }

    String getServer_alias() {
        return server_alias;
    }

    String getOs() {
        return os;
    }

    String getServer_ip() {
        return server_ip;
    }

    int getServer_port() {
        return server_port;
    }

    int getData_collection_delay_minutes() {
        return data_collection_delay_minutes;
    }


}
