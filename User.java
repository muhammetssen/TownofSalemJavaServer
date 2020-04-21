import java.io.FileNotFoundException;

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
        System.out.print(this.myThread.getUserName()+ " is removing... ");
        Server.threadDictionary.remove(this.myThread);
        Server.userThreads.remove(this.myThread);
        System.out.println("\t done");
        Server.logger(this.myThread.socket.getInetAddress() +" username: "+this.myThread.getUserName()  + " disconnected");
    }
    
}