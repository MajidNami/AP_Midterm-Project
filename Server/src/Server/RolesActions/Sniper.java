package Server.RolesActions;

import Server.MessageSystem.Sender;
import Utils.Enums.Event;
import Utils.Enums.MessageType;
import Utils.GameData;
import Utils.Models.Player;

public class Sniper extends RoleAction{

    public Sniper(Player player){
        this.action = new Action(player);
        this.player = player;
        this.player.setLastNightAction(this.action);
    }

    @Override
    public void startTheAct() {
        new Sender(this.player).send(MessageType.roleAction,"Do you want to shoot? [yes/no]");
        this.waitForNewMessage();
        if (this.player.getLastRoleActionMessage() != null){
            if (this.player.getLastRoleActionMessage().toLowerCase().startsWith("y")) {
                new Sender(this.player).send(MessageType.info, "Choose a player by typing their username :");
                this.getPlayer(1);
            } else{
                new Sender(this.player).send(MessageType.info, "Very well.");
                this.action.setSubject1(null);
            }
        }else
            this.action.setSubject1(null);
        GameData.getInstance().addNightEvent(Event.CityShot,this.action);
    }
}
