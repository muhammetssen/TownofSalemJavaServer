
import java.io.*;
import java.net.*;
import java.text.SimpleDateFormat;
import java.util.*;
 
public class UserThread extends Thread {
    public Socket socket;
    public Server server;
    public PrintWriter writer;
    private String userName; 
    public BufferedReader reader;
    public boolean ready =false;
    

    public UserThread(Socket socket, Server server){
        this.socket = socket;
        this.server = server;
    }

    public void run() {
        try{
            InputStream input = socket.getInputStream();
            this.reader = new BufferedReader(new InputStreamReader(input));
            
            this.writer = new PrintWriter(socket.getOutputStream(), true);
            this.userName = this.reader.readLine();
            String serverMessage = "New user connected " + this.userName;
            System.out.println("New user connected from "+this.socket.getInetAddress() + " userName:" +this.userName);
            PrintStream log = new PrintStream(new FileOutputStream("logs.txt",true));
            log.append((new SimpleDateFormat("dd-MM-yyyy HH:mm:ss")).format(new Date()) + " "+ this.socket.getInetAddress()  +" username: "+this.userName   + " connected\n");
            log.close();

            server.broadcast(serverMessage);
            /*synchronized(this.server){
                this.ready = true;
                this.server.notifyAll();
            }*/
            if(this.server.game == null)
                this.server.game = new Game(Server.threadDictionary.get(this),this.server); 
                String clientMessage = "";
            
            do {
                clientMessage = this.reader.readLine();
                if(clientMessage.equals(""))
                    continue;
                if(clientMessage.charAt(0) == '!'){
                    server.game.analyze(clientMessage);
                }
                else{
                serverMessage =  this.userName + "> " + clientMessage;
                server.broadcast(serverMessage);
                }
            } while (!clientMessage.equalsIgnoreCase("Bay Bay"));
            
            //User is leaving the server
            server.broadcast(this.userName +" has left." );
            Server.threadDictionary.get(this).removeUser();
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
        this.writer.println(message);
    }

   
}