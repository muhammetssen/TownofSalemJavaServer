
import java.io.*;
import java.net.*;
import java.util.*;
 
public class UserThread extends Thread {
    private Socket socket;
    private Server server;
    private PrintWriter writer;
    private String userName; 
    

    public UserThread(Socket socket, Server server){
        this.socket = socket;
        this.server = server;
    }

    public void run() {
        try{
            InputStream input = socket.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(input));
            
            writer = new PrintWriter(socket.getOutputStream(), true);
            //printUsers(); //Something like a greetings message from server

            this.userName = reader.readLine();
            String serverMessage = "New user connected " + this.userName;
            server.broadcast(serverMessage, this);
            
            String clientMessage = "";
            do {
                clientMessage = reader.readLine();
                if(clientMessage.equals(""))
                    continue;
                serverMessage =  this.userName + "> " + clientMessage;
                server.broadcast(serverMessage, this);
            } while (!clientMessage.equalsIgnoreCase("Bye"));
            
            //User is leaving the server
            server.broadcast(this.userName +" has left.", this);
            server.threadDictionary.get(this).removeUser();
            reader.close();
            socket.close();

        }
        catch(IOException ex){
            System.out.println("UserThread Error"+ ex.getMessage());
            ex.printStackTrace();
        }
    }
    public String getUserName(){
        return this.userName;
    }
    void sendMessage(String message){
        writer.println(message);
    }
    void printUsers(){

        if(server.hasUsers()){
            writer.println("Connected users " + server.getUserNames());
        }
    }
}