package Server.MessageSystem;

import Utils.GameData;
import Utils.Logger;
import Utils.Models.Player;

public class MessageReceiver extends Thread{

    private final Player player;
    private boolean playerAlive = true;

    /**
     * This class is child of thread class is responsible for receiving the clients messages.
     * @param pl : The player to listen to.
     */
    public MessageReceiver(Player pl){
        this.player = pl;
        this.start();
    }

    @Override
    public void run(){
        try {
            while (!player.getClientSocket().isClosed() && playerAlive){
                MessageReader mr = new MessageReader(player.getDis()).read();
                player.addAMessage(mr);
                new MessageAnalyzer(mr,this.player);
            }
        }catch (Exception ex){
            Logger.log(ex);
            System.out.println("Player "+  this.player.getUserName() + " has been disconnected");
            GameData.getInstance().performPlayerExit(this.player.getUserName());
        }
    }

    /**
     * This method causes this receiver to shut down.
     */
    public void close(){
        this.playerAlive = false;
        this.interrupt();
    }
}
