package Main;
import java.io.*;
import java.net.*;

import User.*;
import GameEngine.*;
 
public class UserThread extends Thread {
    public Socket socket;
    public Server server;
    public PrintWriter writer;
    public String userName; 
    public BufferedReader reader;
    public boolean ready =false;
    public User user;
    

    public UserThread(Socket socket, Server server){
        this.socket = socket;
        this.server = server;

    }
    public String readFromUser(){
        try {
            String message = this.reader.readLine();       
            return message;     
        } catch (Exception e) {
            System.out.println("User cannot be reached.");
            return "";
        }
        
    }
    public static String capitalize(String name){
        name = name.replaceAll(" ", "");
        if(name.length() >1)
            return name.substring(0,1).toUpperCase() + name.substring(1).toLowerCase();
        else if (name.length() == 1)
            return name.toUpperCase();
        return "";
    }

    public void run() {
        try{
            InputStream input = socket.getInputStream();
            this.reader = new BufferedReader(new InputStreamReader(input));
            this.writer = new PrintWriter(socket.getOutputStream(), true);
			this.sendMessage("Please enter an username");
			do {
                String userName = capitalize(this.readFromUser());
				if(Server.userNames.contains(userName)||"".equals(userName) || " ".equals(userName)){
                    this.sendMessage("Please enter a valid username");
                    continue;
                }
                this.userName=userName;
                this.user.userName = userName;
                this.user.userName = this.userName;
                Server.userNames.add(userName);
                Server.userNameDictionary.put(userName, this.user);
			}while(this.userName==null);
            
            System.out.println("New user connected from "+this.socket.getInetAddress() + " userName: " +this.userName);
            Server.logger(this.socket.getInetAddress()  +" username: "+this.userName   + " connected");
            String serverMessage = "New user connected " + this.userName;
            server.broadcast(serverMessage);
         
            if(this.server.game == null && this.user instanceof Host )
                this.server.game = new Game(Server.threadDictionary.get(this),this.server); 
            else
                sendMessage("Waiting for the Host to start to game.");

                String clientMessage = "";
                int tries = 0;
            do {
                clientMessage = this.readFromUser();
                if(clientMessage.equals(""))     {
                    System.out.println(this.userName + " cannot be connected. Try:" + ++tries +"/"+3);
                    Thread.sleep(2000);
                    if(tries >= 3) break; //  Bay Bay User.
                    continue;
                }
                if(clientMessage.charAt(0) == '!')
                    server.game.analyze(this.user,clientMessage);
                else{
                serverMessage =  this.userName + "> " + clientMessage;
                server.broadcast(serverMessage);
                }
            } while (!clientMessage.equalsIgnoreCase("Bay Bay") );
            
            //User is leaving the server
            server.broadcast(this.userName +" has left." );
            this.user.removeUser();
            reader.close();
            writer.close();
            socket.close();
            this.interrupt();
            

        }
        catch(IOException ex){
            System.out.println("UserThread Error"+ ex.getMessage());
            ex.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    public String getUserName(){
        return this.userName;
    }
    public void sendMessage(String message){
        this.writer.println(message);
    }

   
}