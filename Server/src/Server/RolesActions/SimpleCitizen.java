package Server.RolesActions;

import Server.MessageSystem.Sender;
import Utils.Enums.MessageType;
import Utils.GameData;
import Utils.Models.Player;

public class SimpleCitizen extends RoleAction{

    public SimpleCitizen(Player player){
        this.action = new Action(player);
        this.player = player;
        this.player.setLastNightAction(this.action);
    }

    @Override
    public void startTheAct() {
        new Sender(this.player).send(MessageType.info,"You have nothing to do in the night so stay asleep!");
    }
}
