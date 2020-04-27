package PlayerTypes;
public class Vampire extends BadPlayer {
    public Vampire(){
        System.out.println("Vampire created");
    }
    @Override
    public String toString() {
        return "Vampire";    
    }
}