package Server.RolesActions;

import Server.MessageSystem.Sender;
import Utils.Enums.Event;
import Utils.Enums.MessageType;
import Utils.GameData;
import Utils.Models.Player;

public class DoctorLecter extends RoleAction{

    public DoctorLecter(Player player){
        this.action = new Action(player);
        this.player = player;
        this.player.setLastNightAction(this.action);
    }

    @Override
    public void startTheAct() {
        new Sender(this.player).send(MessageType.roleAction,"It's your turn (You have 30 secs)!\nPick a player to save (Type the username) :");
        this.getPlayer(1);
        new Sender(this.player).send(MessageType.info,"Great! You saved " + this.action.getSubject1().getUserName());
        new Sender(this.player).send(MessageType.roleAction,"Now vote for a player to be killed by the godfather :");
        this.getPlayer(2);
        new Sender(this.player).send(MessageType.info,"Your vote for " + this.action.getSubject2().getUserName() + " has been submitted!");
        GameData.getInstance().addNightEvent(Event.MafiaSave,this.action);
    }

}
