package cmd;

public class GatherSystemInformation extends OsInfoGathering {

    public GatherSystemInformation(){
        cmd = new CommandPromptWIN();
    }

    @Override
    public String getServerName(){
        String serverNameCommand = "hostname";

        try {
            cmd.runCommand(serverNameCommand);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        //pc name is 1st line so .get(0)
        return cmd.getArrayList().get(0);
    }

    @Override
    public int getServerCPU(){
        String cpuUsage = "wmic cpu get loadpercentage";

        try {
            cmd.runCommand(cpuUsage);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        //3rd line of output is processor usage out of 100%
        return Integer.valueOf(cmd.getArrayList().get(2).trim());
    }

    @Override
    public int[] getServerRAM(){
//        String totalMemory = "systeminfo | findstr ”Total Physical Memory”";

        int[] ram = new int[2];
        String totalMem = "systeminfo";
        try {
            cmd.runCommand(totalMem);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        for (String line:cmd.getArrayList()) {
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

    private String ipconfig = "ipconfig";
    public String getServerIP(){

        String serverIP = null;
        try {
            cmd.runCommand(ipconfig);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        //todo line.contains() may not work all the time
        for (String line:cmd.getArrayList()) {
            if(line.contains("IPv4 Address. . . . .")){
//                System.out.println(line);
                line = line.substring(line.lastIndexOf(':')+2);
//                System.out.println(line);
                serverIP = line;
//                line = line.replaceAll("[a-zA-Z]")
            }
        }
        return serverIP;
    }

    private void testCase(){
        this.serverName = "test";
        this.CPU = 12;
        this.totalRAM = 4096;
        this.RAM = 1231;
    }
}
