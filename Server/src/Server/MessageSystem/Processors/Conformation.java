package Server.MessageSystem.Processors;

import Server.MessageSystem.MessageReader;
import Server.MessageSystem.Sender;
import Utils.Enums.MessageType;
import Utils.GameData;
import Utils.Models.Player;

public class Conformation implements MessageProcessor{

    private final Player pl;
    private final MessageReader mr;

    public Conformation (MessageReader mr, Player pl){
        this.mr = mr;
        this.pl = pl;
    }

    @Override
    public void start() {
        System.out.println("Conformation received for " + this.pl);
        new Sender().sendToAllExcept(this.pl, MessageType.info,"Player " + this.pl + " confirmed the game.");
        GameData.getInstance().addConfirmedPlayer();
    }
}
