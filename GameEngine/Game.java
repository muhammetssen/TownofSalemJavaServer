package GameEngine;
import java.io.IOException;
import java.util.*;
import User.*;
import Main.*;
import PlayerTypes.*;

public class Game {
    public static boolean isDay = true;
    public User gameHost;
    public ArrayList<String> playerTypes = new ArrayList<String>(Arrays.asList("Villager", "Vampire","Doctor"));
    public ArrayList<String> types = new ArrayList<String>();   
    public Server server;

    public Game(User gameHost, Server server) throws NumberFormatException, IOException { // User gameHost
        this.server = server;
        gameHost.myThread.sendMessage("You are the Host");
        String decidedTypes = "There will be ";
        for (String type : playerTypes) {
            // HOSTA TEK TEK KACAR TANE OLACAGINI SOR INPUT AL
            gameHost.myThread.sendMessage("How many " + type + "s do you want?");
            boolean isValid = false;
            int count = 0;
            while (!isValid) {
                try {
                    String input;
                    if ((input = gameHost.myThread.readFromUser()) == "") {
                        this.server.broadcast("HOST HAS LEFT THE GAME!!");
                        System.exit(0);
                        /** TODO A random player will be chosen as host, look after hav'ng a host GUI */
                    }
                    count = Integer.parseInt(input);
                    isValid = true;
                    gameHost.myThread.sendMessage("Okay, " + count + " " + type + "(s).");

                } catch (Exception e) {
                    gameHost.myThread.sendMessage("Please enter an Integer");
                }
            }
            decidedTypes += count + " " + type + "s, ";
            for (int i = 0; i < count; i++)
                types.add(type);
        }
        // TODO CIRKIN YAZDIRIYOR
        this.server.broadcast("SERVER> " + decidedTypes.substring(0, decidedTypes.length() - 2) + ".");
    }

    public void analyze(User sender, String command) throws IOException {
        if (command.length() <= 1)
            return;
        Scanner reader = new Scanner(command);
        reader.next();
        switch (command.substring(1).toLowerCase().split(" ")[0]) {
            case "ready":
                System.out.println(sender.userName + " is ready");
                sender.isReady = true;
                boolean isGameReady = Server.threadDictionary.size() == this.types.size();
                int readyCount = 0;
                for (User user : Server.threadDictionary.values()) {
                    isGameReady = isGameReady && user.isReady;
                    if (user.isReady)
                        readyCount++;
                }
                sender.myThread.sendMessage("You are marked as Ready. Waiting for others.\t" + readyCount + "/"
                        + Server.userThreads.size());
                if (readyCount > types.size())
                    server.broadcast("More players than host configured." + (readyCount - types.size())
                            + " player(s) must leave.");
                if (isGameReady) {
                    this.server.isEveryoneReady = true;
                    this.server.serverSocket.close();
                }
                break;
            case "pm":
                if (!reader.hasNext()) {
                    sender.myThread.sendMessage("Invalid usage of pm. Correct usage is \"!pm USERNAME MESSAGE\"");
                    return;
                }
                String destUser = UserThread.capitalize(reader.next());
                if (!reader.hasNext())
                    return;
                String message = "PM " + sender.userName + "> " + reader.nextLine();
                if (!Server.userNameDictionary.keySet().contains(destUser)) {
                    sender.myThread.sendMessage(destUser + " cannot be found");
                    return;
                }
                sender.myThread.sendMessage(message);
                Server.userNameDictionary.get(destUser).myThread.sendMessage(message);
                break;
            case "vote":
                break;
            case "protect":
                if(sender.playerType.hasUsedPowerThisTurn){
                    sender.myThread.sendMessage("You cannot protect someone else too!");
                    break;
                }
                if(!(sender.playerType instanceof Doctor) ){
                    sender.myThread.sendMessage("You are not a doctor. You cannot protect someone.");
                    break;
                }
                String selectedUserName = UserThread.capitalize( reader.next());
                Doctor doctor = (Doctor) sender.playerType;
                if(doctor.protect(Server.userNameDictionary.get(selectedUserName))){
                    sender.myThread.sendMessage("You have protected "+selectedUserName);
                    sender.playerType.hasUsedPowerThisTurn = true;
                }
                else sender.myThread.sendMessage(selectedUserName + " cannot be found. Please try again.");
                break;
            case "kill":
            
                break;
            default:
                sender.myThread.sendMessage("Such a command does not exist. Please try again.");
                break;
        }
        // System.out.println("kadir is yapacak");
    }

    public void distributeTypes() throws ClassNotFoundException, InstantiationException, IllegalAccessException {
        Random random = new Random();
        ArrayList<String> typesTemp = new ArrayList<String>(this.types);
        for (User user : Server.userNameDictionary.values()) {
            int index = random.nextInt(typesTemp.size());
            Class<?> wantedClass = Class.forName("PlayerTypes." + typesTemp.get(index));
            user.playerType = (PlayerType) wantedClass.newInstance();
            typesTemp.remove(index);
            user.myThread.sendMessage("You are a " + user.playerType);
        }        
    }



}
    


