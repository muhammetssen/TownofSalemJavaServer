import java.net.*;
import java.io.*;

public class Client {
    private String hostIP;
    private int port;
    private String userName;
    public Client(String hostIP, int port){
        this.hostIP = hostIP;
        this.port= port;
    }
    public void execute() {
        try{
            Socket socket = new Socket(hostIP, port);
            System.out.println("Connected to "+hostIP+":"+port);
            
            new ReadThread(socket, this).start();
            new WriteThread(socket, this).start();
        }
        catch(UnknownHostException ex){
            System.out.println("Server not found" + ex.getMessage());
        }
        catch(IOException ex){
            System.out.println("I/O error" + ex.getMessage());
        }
    }
    public String getUserName(){
        return this.userName;
        
    }
    public void setUserName(String userName) {
        if(userName != null)
            this.userName = userName;
    }

    public static void main(String[] args) {
        if(args.length < 2){
            System.out.println("Syntax: java Client IP Port ");
            return;
        }
        String hostname = args[0];
        int port  = Integer.parseInt(args[1]);
        Client client = new Client(hostname, port);
        client.execute();
    }


}