package User;
import java.io.FileNotFoundException;
import Main.*;
import PlayerTypes.*;
public class User {
    public UserThread myThread;
    public String userName;
    public PlayerType playerType;
    
    public User(UserThread myThread){
        this.myThread = myThread;
        Server.userThreads.add(myThread);
        Server.threadDictionary.put(myThread, this);
        
    }
    
    public void removeUser() throws FileNotFoundException {
        System.out.print(this.userName+ " is removing... ");
        Server.userNames.remove(this.userName);
        Server.threadDictionary.remove(this.myThread);
        Server.userThreads.remove(this.myThread);
        System.out.println("\t done");
        Server.logger(this.myThread.socket.getInetAddress() +" username: "+this.userName  + " disconnected");
    }
    
}