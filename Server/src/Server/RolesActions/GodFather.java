package Server.RolesActions;

import Server.MessageSystem.Sender;
import Utils.Enums.Event;
import Utils.Enums.MessageType;
import Utils.GameData;
import Utils.Models.Player;

public class GodFather extends RoleAction{

    private final Action[] actions;

    public GodFather(Player player,Action[] actions){
        this.action = new Action(player);
        this.player = player;
        this.actions = actions;
        this.player.setLastNightAction(this.action);
    }

    @Override
    public void startTheAct() {
        StringBuilder msg = new StringBuilder("It is your turn godfather!\n" +
                "Your teammates have voted for following players :\n");
        for (Action act : actions)
            msg.append(act.getPlayer().getUserName()).append(" :").append(act.getSubject2().getUserName()).append('\n');
        new Sender(player).send(MessageType.roleAction,msg + "You have 30 seconds to pick a player to die (type the username) :");
        this.getPlayer(1);
        new Sender(this.player).send(MessageType.info,"Easyyyyyy! You just shot " + this.action.getSubject1().getUserName());
        GameData.getInstance().addNightEvent(Event.MafiaShot,this.action);
    }
}
