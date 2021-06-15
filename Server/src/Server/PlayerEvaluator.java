package Server;

import Server.MessageSystem.Sender;
import Utils.ConfigsAndData;
import Utils.Enums.MessageType;
import Utils.GameData;
import Utils.Models.Player;

public class PlayerEvaluator extends Thread{

    private final Player player;

    /**
     * This class is used to evaluate the serve situation and finally decide if a new player can join the server or not.
     * @param pl : The new player.
     */
    public PlayerEvaluator(Player pl){
        this.player = pl;
        this.start();
    }

    @Override
    public void run(){
        if (GameData.getInstance().getGame().getPlayersConnected() < ConfigsAndData.getInstance().getPlayersNumber())
            GameData.getInstance().getGame().addAPlayer(this.player);
        else
            new Sender(this.player).send(MessageType.error,"Sorry but the server is full !");
    }
}
