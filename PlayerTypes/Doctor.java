package PlayerTypes;
import Main.Server;
import User.User;
public class Doctor extends GoodPlayer  {
    public Doctor(){

    }
    public boolean protect(User selectedUser){
        if(!Server.userNames.contains(selectedUser.userName)) return false;
        selectedUser.playerType.isProtected = true;
        return true;
    }

    @Override
    public String toString() {
        return "Doctor";
    }

}