package Server.RolesActions;

import Server.MessageSystem.Sender;
import Utils.Enums.MessageType;
import Utils.Enums.Roles;
import Utils.GameData;
import Utils.Models.Player;

public class Inspector extends RoleAction{

    public Inspector(Player player){
        this.action = new Action(player);
        this.player = player;
        this.player.setLastNightAction(this.action);
    }

    @Override
    public void startTheAct() {
        new Sender(this.player).send(MessageType.roleAction,"You have 30 seconds to inspect a player by typing their username :");
        this.getPlayer(1);
        this.action.setResult(action.getSubject1().getRole() == Roles.doctorLecter || action.getSubject1().getRole() == Roles.simpleMafia);
        new Sender(this.player).send(MessageType.roleAction,this.action.getResult() ? "WOW! Player " + this.action.getSubject1().getUserName() + " is a mafia!" :
                "Ooops! Player " + this.action.getSubject1() + " is not mafia!");
    }
}
