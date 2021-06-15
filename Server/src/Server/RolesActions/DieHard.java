package Server.RolesActions;

import Server.MessageSystem.Sender;
import Utils.Enums.Event;
import Utils.Enums.MessageType;
import Utils.GameData;
import Utils.Models.Player;

public class DieHard extends RoleAction{

    public DieHard(Player player){
        this.action = new Action(player);
        this.player = player;
        this.player.setLastNightAction(this.action);
    }

    @Override
    public void startTheAct() {
        new Sender(this.player).send(MessageType.info,"It's your turn die hard! You have " + (2 - GameData.getInstance().getGame().getDieHardInquiry())
        + " inquiry left.");
        if (GameData.getInstance().getGame().getDieHardInquiry() < 2){
            new Sender(this.player).send(MessageType.roleAction,"Do you want inquiry? [yes/no]");
            this.waitForNewMessage();
            if (this.player.getLastRoleActionMessage() != null) {
                if (this.player.getLastRoleActionMessage().toLowerCase().startsWith("y")) {
                    new Sender(this.player).send(MessageType.info, "Hanged players will be announced next day.");
                    GameData.getInstance().getGame().addDieHarInquiry();
                    this.action.setResult(true);
                } else
                    new Sender(this.player).send(MessageType.info, "Very well.");
            }
        }
        GameData.getInstance().addNightEvent(Event.DieHardInquiry,this.action);
    }
}
