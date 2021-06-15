package MessageSystem.Processors;

import MessageSystem.MessageReader;
import Utils.Data;

public class Death implements MessageProcessor{

    private final MessageReader mr;

    public Death(MessageReader mr){
        this.mr = mr;
    }

    @Override
    public void start() {
        System.out.println(mr.getMessage());
        System.out.println("Do you want to leave the game [yes/no] :");
        Data.getInstance().getGame().setStatus(null);
    }
}
