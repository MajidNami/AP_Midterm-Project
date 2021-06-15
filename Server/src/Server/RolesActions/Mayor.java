package Server.RolesActions;

import Server.MessageSystem.Sender;
import Utils.Enums.MessageType;
import Utils.Models.Player;

public class Mayor extends RoleAction{

    public Mayor(Player player){
        this.action = new Action(player);
        this.player = player;
        this.player.setLastNightAction(this.action);
    }

    @Override
    public void startTheAct() {
        new Sender(this.player).send(MessageType.roleAction,"It's time to shine mayor! Do you want to cancel the voting? [yes/no] (You have 30 sec)");
        this.waitForNewMessage();
        if (this.player.getLastRoleActionMessage() != null){
            if (this.player.getLastRoleActionMessage().toLowerCase().startsWith("y")) {
                new Sender(this.player).send(MessageType.info, "Alright then! voting be will be cancelled.");
                this.action.setResult(true);
            } else {
                //Commented out because this part was not mentioned in the project summary pdf.
                /*new Sender(this.player).send(MessageType.roleAction, "Okay. Do you want to hang someone without voting? [yes/no]");
                this.waitForNewMessage();
                if (this.player.getLastRoleActionMessage() != null){
                    if (this.player.getLastRoleActionMessage().toLowerCase().startsWith("y")) {
                        new Sender(this.player).send(MessageType.roleAction, "Type the username :");
                        this.getPlayer(1);
                    }else
                        this.action.setSubject1(null);
                }else
                    this.action.setSubject1(null);*/
                new Sender(this.player).send(MessageType.info,"The voting won't be cancelled.");
            }
        }else{
            this.action.setResult(false);
            this.action.setSubject1(null);
        }
    }
}
