
import java.io.*;
import java.net.*;
import java.util.*;
import java.text.SimpleDateFormat;

public class Server {
    Hashtable<UserThread, User> threadDictionary = new Hashtable<UserThread, User>();

    public int port;
    public class User{
        public UserThread myThread;
        public String userName;
        public User( UserThread myThread){
            this.myThread = myThread;
            userThreads.add(myThread);
            threadDictionary.put(myThread, this);
        }
        public void removeUser() throws FileNotFoundException{
            System.out.print(this.myThread.getUserName()+ " is removing");
            PrintStream log = new PrintStream(new FileOutputStream("logs.txt",true));
            log.append((new SimpleDateFormat("dd-MM-yyyy HH:mm:ss")).format(new Date()) + " "+ this.myThread.socket.getInetAddress() +" username: "+this.myThread.getUserName()  + " disconnected\n");
            log.close();
            

            //userNames.remove(this.userName);
            threadDictionary.remove(this.myThread);
            userThreads.remove(this.myThread);
            System.out.println("\t done");
        }
        public void setUserName(String userName){
            this.userName = userName;
            userNames.add(userName);
        }
    }
    private Set<UserThread> userThreads = new HashSet<>();
    private Set<String> userNames = new HashSet<>();

    public Set<UserThread> getUserThreads(){
        return userThreads;
    }
    public Server(int port){
        this.port = port;
    }
    public void execute(){
        try(ServerSocket serverSocket = new ServerSocket(this.port)){
            System.out.println("Server is starting on port " + port);
            while(true){
                Socket incomingSocket = serverSocket.accept();
                
                
        
                UserThread newUserThread = new UserThread(incomingSocket, this);
                User newUser = new User(newUserThread);
                //userThreads.add(newUser);
                newUser.myThread.start();
            }
        }
        catch(IOException ex){
            System.out.println("Server Error" + ex);
            ex.printStackTrace();//BU NE BILMIYORUM DENEYECEGIM
        }
    }
    
public static void main(String[] args) {
    if(args.length == 0){
        System.out.println("Please give a Port Number as argument. Syntax java Server <port>");
        System.exit(0);
    }
    Server server = new Server(Integer.parseInt(args[0]));
    server.execute();
}
public void broadcast(String message, UserThread excludeUser) {
    for (UserThread destThread : userThreads) {
            destThread.sendMessage(message);
        
    }
}
public boolean hasUsers(){
    return !this.userThreads.isEmpty();
}
public Set<String> getUserNames(){
    return this.userNames;
}
    
}
