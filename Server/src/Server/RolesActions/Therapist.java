package Server.RolesActions;

import Server.MessageSystem.Sender;
import Utils.Enums.Event;
import Utils.Enums.MessageType;
import Utils.GameData;
import Utils.Models.Player;

public class Therapist extends RoleAction{

    public Therapist(Player player){
        this.action = new Action(player);
        this.player = player;
        this.player.setLastNightAction(this.action);
    }

    @Override
    public void startTheAct() {
        new Sender(this.player).send(MessageType.roleAction,"It's your turn therapist! Please mute a player by typing their username :");
        this.getPlayer(1);
        new Sender(this.player).send(MessageType.info,"You just muted " + this.action.getSubject1().getUserName() + " !!");
        GameData.getInstance().addNightEvent(Event.Therapist,this.action);
    }
}
