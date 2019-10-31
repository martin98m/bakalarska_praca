package cmd;

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
                line = line.replaceAll("[a-zA-Z,:]","").trim();
                ram[0] = Integer.parseInt(line);
            }
            if(line.contains("Available Physical Memory")){
                line = line.replaceAll("[a-zA-Z,:]","").trim();
                ram[1] =  ram[0] - Integer.parseInt(line);
            }
        }
        return ram;
    }


    public String getServerIP(){
        String ipconfig = "ipconfig";
        String serverIP = null;

        cmd.runCommand(ipconfig,false);

        //todo line.contains() may not work all the time
        for (String line:cmd.getStdout()) {
            if(line.contains("IPv4 Address. . . . .")){
                line = line.substring(line.lastIndexOf(':')+2);
                serverIP = line;
            }
        }
        return serverIP;
    }
}
