
import java.io.*;
import java.net.*;
import java.text.SimpleDateFormat;
import java.util.*;
 
public class UserThread extends Thread {
    public Socket socket;
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
            System.out.println("New user connected from "+this.socket.getInetAddress() + " userName:" +this.userName);
            PrintStream log = new PrintStream(new FileOutputStream("logs.txt",true));
            log.append((new SimpleDateFormat("dd-MM-yyyy HH:mm:ss")).format(new Date()) + " "+ this.socket.getInetAddress()  +" username: "+this.userName   + " connected\n");
            log.close();
            server.broadcast(serverMessage, this);
            
            String clientMessage = "";
            do {
                clientMessage = reader.readLine();
                if(clientMessage.equals(""))
                    continue;
                serverMessage =  this.userName + "> " + clientMessage;
                server.broadcast(serverMessage, this);
            } while (!clientMessage.equalsIgnoreCase("Bay Bay"));
            
            //User is leaving the server
            server.broadcast(this.userName +" has left.", this);
            server.threadDictionary.get(this).removeUser();
            reader.close();
            writer.close();
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