package GameEngine;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.*;
import User.*;
import Main.*;
public class Game {
    public static boolean isDay = true;
    public ArrayList<User> users = new ArrayList<User>();
    public User gameHost;
    public ArrayList<String> playerTypes = new ArrayList<String>(Arrays.asList("Villager", "Vampire"));
    public ArrayList<String> types = new ArrayList<String>(); //["Villager", "Villager","Villager","Vampire","Vampire"];
    public Server server;
    public Game(User gameHost, Server server) throws NumberFormatException, IOException { // User gameHost
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
                    if((input = gameHost.myThread.readFromUser()) == ""){
                        this.server.broadcast("HOST HAS LEFT THE GAME!!");
                        System.exit(0);
                        /**TODO A random player will be chosen as host, look after hav'ng a host GUI */
                    }   
                    count = Integer.parseInt(input);
                    isValid = true;
                    gameHost.myThread.sendMessage("Okay, "+count+" " + type+"(s).");

                    
                } catch (Exception e) {
                    gameHost.myThread.sendMessage("Please enter an Integer");
                }
            }

           
            decidedTypes += count + " " + type+"s, " ;
            for (int i = 0; i < count; i++) 
                types.add(type);
        }
         //TODO CIRKIN YAZDIRIYOR
         this.server.broadcast("SERVER> " + decidedTypes.substring(0, decidedTypes.length()-2) + ".");  
    } 
    public void analyze(User sender,String command) throws IOException {
        if(command.length() <=1) return;
        
        switch (command.substring(1).toLowerCase().split(" ")[0]) {
            case "ready":
                System.out.println(sender.userName +" is ready");
                sender.isReady = true;
                boolean isGameReady = Server.threadDictionary.size() ==  this.types.size();
                int readyCount = 0;
                for (User user : Server.threadDictionary.values()){
                    isGameReady = isGameReady && user.isReady;
                    if(user.isReady) readyCount++;
                }
                sender.myThread.sendMessage("You are marked as Ready. Waiting for others.\t" + readyCount + "/" + Server.userThreads.size());
                if(readyCount > types.size()) server.broadcast("More players than host configured." + (readyCount-types.size()) +" player(s) must leave."  );
                if(isGameReady){
                    this.server.isEveryoneReady = true;
                    this.server.serverSocket.close();
                }
                break;
            case "pm":
                Scanner reader = new Scanner(command);
                reader.next(); // skipping pm
                if(!reader.hasNext()) return;
                String destUser = UserThread.capitalize(reader.next());
                System.out.println(destUser);
                if(!reader.hasNext()) return;
                String message = "PM "+ sender.userName +"> "+  reader.nextLine(); 
                System.out.println(message);
                if(!Server.userNameDictionary.keySet().contains(destUser)) return;
                sender.myThread.sendMessage(message);
                Server.userNameDictionary.get(destUser).myThread.sendMessage(message);
                break; 
            default:
                break;
        }
        //System.out.println("kadir is yapacak");
    }
  
    }
    


