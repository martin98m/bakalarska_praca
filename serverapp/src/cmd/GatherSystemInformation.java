package cmd;

import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

public class GatherSystemInformation extends OsInfoGathering {

    public GatherSystemInformation(){
        cmd = new CommandPromptWIN();
    }

    //todo usage per user

    @Override
    public String getServerName(){
        String serverNameCommand = "hostname";

        cmd.runCommand(serverNameCommand, false);

        //pc name is 1st line so .get(0)
        return cmd.getStdout().get(0);
    }

    @Override
    public int getServerCPU(){
        String cpuUsage = "wmic cpu get loadpercentage";

        cmd.runCommand(cpuUsage, false);

        //3rd line of output is processor usage out of 100%
        return Integer.parseInt(cmd.getStdout().get(2).trim());
    }

    @Override
    public int[] getServerRAM(){
//        String totalMemory = "systeminfo | findstr ”Total Physical Memory”";

        int[] ram = new int[2];
        String totalMem = "systeminfo";

        cmd.runCommand(totalMem, false);

        for (String line:cmd.getStdout()) {
            if(line.contains("Total Physical Memory")) {
                System.out.println(line);
                line = line.replaceAll("[^0-9]","").trim();
//                if (line.charAt(2) == 729)
//                    line = line.replaceAll(String.valueOf(line.charAt(2)),"");
                System.out.println("["+line+"]");
                ram[0] = Integer.parseInt(line);
            }
            if(line.contains("Available Physical Memory")){
                line = line.replaceAll("[^0-9]","").trim();
                ram[1] =  ram[0] - Integer.parseInt(line);
            }
        }
        return ram;
    }


    public String getServerIP(){
        String ipconfig = "ipconfig";
        String serverIP = null;

        /*try(final DatagramSocket socket = new DatagramSocket()){
            socket.connect(InetAddress.getByName("8.8.8.8"), 10002);
            serverIP = socket.getLocalAddress().getHostAddress();
        } catch (SocketException | UnknownHostException e) {
            e.printStackTrace();
        }*/

        cmd.runCommand(ipconfig,false);

        //todo line.contains() may not work all the time
        for (String line : cmd.getStdout()) {
            if(line.contains("192.168.0.")){
//                if (line.contains("192.168.0.1")) break;
                line = line.substring(line.lastIndexOf(':')+2);
                serverIP = line;
                break;
            }
        }
        System.out.println(serverIP);
        return serverIP;
    }

    public String getSystemOs(){
        String command = "systeminfo";
        String system_os = null;

        cmd.runCommand(command, false);
        for(String line: cmd.getStdout()){
            if(line.contains("OS Name:")){

                line = line.substring(line.lastIndexOf(':')+1);
                line = line.trim();
                system_os = line;
            }
        }

        return system_os;
    }
}
