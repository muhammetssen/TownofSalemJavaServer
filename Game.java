import java.io.IOException;
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
        for (String type : playerTypes) {
            //HOSTA TEK TEK KACAR TANE OLACAGINI SOR INPUT AL
            gameHost.myThread.server.broadcast("How many " + type );
            System.out.println("emre1");
            int count = Integer.parseInt(gameHost.myThread.reader.readLine());
            this.server.broadcast(count+"");
            System.out.println("emre2");
            for (int i = 0; i < count; i++) 
                types.add(type);
        }
        System.out.println(types);
        this.taker(types.toString()); //TODO CIRKIN YAZDIRIYOR


        
    } 
    public  void taker(String a){
        System.out.println("Game is saying " +a);
        this.server.broadcast("Server is sending " + a);
    }
    public void analyze(String komut){
        System.out.println("kadir is yapacak");
    }
  
    }
    


