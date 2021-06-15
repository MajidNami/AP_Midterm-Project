package Server.MessageSystem.Processors;

import Server.MessageSystem.MessageReader;
import Utils.Models.Player;

public class RoleAction implements MessageProcessor{

    private final Player pl;
    private final MessageReader mr;

    public RoleAction (MessageReader mr, Player pl){
        this.mr = mr;
        this.pl = pl;
    }

    @Override
    public void start() {
        this.pl.addARoleActionMessage(mr.getMessage());
        System.out.println("Role action received for " + this.pl + " (" + mr.getMessage() + ')');
    }
}
