package MessageSystem.Processors;

import MessageSystem.MessageReader;
import Utils.Data;
import Utils.Enums.Roles;

public class Connection implements MessageProcessor{

    private final MessageReader mr;

    public Connection(MessageReader mr){
        this.mr = mr;
    }

    @Override
    public void start() {
        Data.getInstance().getGame().setPlayerRole(Roles.valueOf(mr.getMessage()));
    }
}
