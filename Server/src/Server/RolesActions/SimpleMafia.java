package Server.RolesActions;

import Server.MessageSystem.Sender;
import Utils.Enums.MessageType;
import Utils.GameData;
import Utils.Models.Player;

public class SimpleMafia extends RoleAction{

    public SimpleMafia(Player player){
        this.action = new Action(player);
        this.player = player;
        this.player.setLastNightAction(this.action);
    }

    @Override
    public void startTheAct() {
        boolean actEnded = false;
        new Sender(player).send(MessageType.roleAction,"Vote for someone to be killed by god father (Type the username) :");
        this.getPlayer(1);
        new Sender(this.player).send(MessageType.info,"Your vote for " + this.action.getSubject1().getUserName() + " has been submitted!");
    }
}
