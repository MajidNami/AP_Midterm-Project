package Server.RolesActions;

import Server.MessageSystem.Sender;
import Utils.Enums.Event;
import Utils.Enums.MessageType;
import Utils.GameData;
import Utils.Models.Player;

public class CityDoctor extends RoleAction{

    public CityDoctor(Player player){
        this.action = new Action(player);
        this.player = player;
        this.player.setLastNightAction(this.action);
    }

    @Override
    public void startTheAct() {
        new Sender(this.player).send(MessageType.roleAction,"It's your turn doctor! Pick some one to save by typing their username :");
        this.getPlayer(1);
        new Sender(this.player).send(MessageType.info,"Great! You saved " + this.action.getSubject1().getUserName());
        GameData.getInstance().addNightEvent(Event.CitySave,this.action);
    }
}
