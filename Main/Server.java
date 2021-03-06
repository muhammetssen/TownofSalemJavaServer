package Main;
import java.io.*;
import java.net.*;
import java.util.*;
import java.text.SimpleDateFormat;
import GameEngine.*;
import User.*;
import PlayerTypes.Vampire;
import PlayerTypes.Villager;


public class Server {
    private int port;
    public Game game;
    public ServerSocket serverSocket;

    public static ArrayList<UserThread> userThreads = new ArrayList<UserThread>();
    public static Hashtable<UserThread, User> threadDictionary = new Hashtable<UserThread, User>();    
    public static ArrayList<String> userNames = new ArrayList<String>(); 
    public static Hashtable<String, User> userNameDictionary = new Hashtable<String, User>();    
    
    public boolean isEveryoneReady = false;
    public Server(int port){
        this.port = port;    
    }  
    
public static void main(String[] args) throws Exception{
    if(args.length == 0){
        System.out.println("Please give a Port Number as argument. Syntax java Server <port>");
        System.exit(0);
    }
    /**Creating a new Server object */
    Server server = new Server(Integer.parseInt(args[0]));
    //If possible, create a ServerSocket object with given port
    try(ServerSocket serverSocket = new ServerSocket(server.port)){
        server.serverSocket = serverSocket;
        System.out.println("Server is starting on port " + server.port);
        Socket hostSocket = serverSocket.accept();
        UserThread hostThread = new UserThread(hostSocket,server);
        hostThread.start();
        Host hostUser = new Host(hostThread);
        hostThread.user = hostUser;
            
        while(!server.isEveryoneReady){
            try {
                Socket incomingSocket = serverSocket.accept(); //ClientSocket           
                UserThread newUserThread = new UserThread(incomingSocket, server);
                newUserThread.start();
                User newUser = new User(newUserThread);
                newUserThread.user = newUser;
            } catch (SocketException e) {
                System.out.println("Socket is closed");
            }
        }
        System.out.println("Everyone is ready!!");
        server.broadcast("Everyone is ready! The game is starting");
        server.game.distributeTypes();

    }
    catch(IOException ex){
        System.out.println("Server Error" + ex);
        ex.printStackTrace();
    }


}
public  void broadcast(String message) {
    for (UserThread destThread : userThreads) 
            destThread.sendMessage(message);
}
public boolean hasUsers(){
    return !userThreads.isEmpty();
}

public static void logger(String event) throws FileNotFoundException {
    PrintStream log = new PrintStream(new FileOutputStream("logs.txt",true));
    log.append((new SimpleDateFormat("dd-MM-yyyy HH:mm:ss")).format(new Date()) +" "+event +" \n");
    log.close();
    

}
}
