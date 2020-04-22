import java.io.IOException;
import java.net.SocketException;
import java.util.*;

public class Game {
    public static boolean isDay = true;
    public ArrayList<User> users = new ArrayList<User>();
    public User gameHost;
    public ArrayList<String> playerTypes = new ArrayList<String>(Arrays.asList("Villager", "Vampire"));
    public Server server;
    public Game(User gameHost, Server server) throws NumberFormatException, IOException { // User gameHost
        ArrayList<String> types = new ArrayList<String>(); //["Villager", "Villager","Villager","Vampire","Vampire"];
        this.server = server;
        gameHost.myThread.sendMessage("You are the Host");
        String decidedTypes = "There will be ";
        for (String type : playerTypes) {
            //HOSTA TEK TEK KACAR TANE OLACAGINI SOR INPUT AL
            gameHost.myThread.sendMessage("How many " + type+"s do you want?" );
            boolean isValid = false;
            int count= 0;
            while(!isValid){
                
                try {
                    String input  ;
                    if((input = gameHost.myThread.readFromUser()) == null){
                        this.server.broadcast("HOST HAS LEFT THE GAME!!");
                        gameHost.removeUser();
                        if(Server.userThreads.size() != 0){
                            gameHost = Server.threadDictionary.keys().nextElement().user;
                            gameHost.myThread.sendMessage("You are the New Host");
                            gameHost.myThread.sendMessage("How many " + type+"s do you want?" );
                            continue;
                        }
                        else{
                            System.out.println("NO ONE LEFT. CLOSING THE GAME!");
                            System.exit(0);
                            break;
                        }
                    }
                        
                    count = Integer.parseInt(input);
                    isValid = true;
                    gameHost.myThread.sendMessage("Okay, "+count + " is valid.");

                    
                } catch (Exception e) {
                    gameHost.myThread.sendMessage("Please enter an Integer");
                }
            }

           
            decidedTypes += count + " " + type+"s, " ;
            for (int i = 0; i < count; i++) 
                types.add(type);
        }
        //System.out.println(types);
        //this.taker(types.toString()); //TODO CIRKIN YAZDIRIYOR
        this.informer(decidedTypes.substring(0, decidedTypes.length()-2) + ".");

        
    } 
    public  void informer(String a){
        System.out.println("Game is saying " +a);
        this.server.broadcast("SERVER> " + a);
    }
    public void analyze(String komut){
        System.out.println("kadir is yapacak");
    }
  
    }
    


