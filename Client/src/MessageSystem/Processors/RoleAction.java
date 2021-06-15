package MessageSystem.Processors;

import MessageSystem.MessageReader;
import Utils.Data;

public class RoleAction implements MessageProcessor{

    private final MessageReader mr;

    public RoleAction(MessageReader mr){
        this.mr = mr;
    }

    @Override
    public void start() {
        if (!mr.getMessage().isEmpty())
            System.out.println(mr.getMessage());
        Data.getInstance().getGame().setPlayerRoleActionTime(true);
    }
}
