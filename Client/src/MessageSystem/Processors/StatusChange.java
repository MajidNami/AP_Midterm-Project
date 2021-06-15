package MessageSystem.Processors;

import MessageSystem.MessageReader;
import Utils.Data;
import Utils.Enums.GameStatus;

public class StatusChange implements MessageProcessor{

    private final MessageReader mr;

    public StatusChange(MessageReader mr){
        this.mr = mr;
    }

    @Override
    public void start() {
        Data.getInstance().getGame().setStatus(GameStatus.valueOf(mr.getMessage()));
    }
}
