package Server.MessageSystem.Processors;

import Server.MessageSystem.MessageReader;
import Utils.GameData;
import Utils.Models.Player;

public class Exit implements MessageProcessor{

    private final Player pl;
    private final MessageReader mr;

    public Exit (MessageReader mr, Player pl){
        this.mr = mr;
        this.pl = pl;
    }

    @Override
    public void start() {
        GameData.getInstance().performPlayerExit(pl);
    }
}
