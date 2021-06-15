package Server.MessageSystem;

import Server.MessageSystem.Processors.MessageProcessorFactory;
import Utils.Models.Player;

public class MessageAnalyzer extends Thread{

    private final MessageReader mr;
    private final Player player;

    /**
     * This class is a child of Thread class and is used to analyze the received messages from the clients.
     * @param mr : The MessageReader class which contains the information of the received message.
     * @param pl : The player.
     */
    public MessageAnalyzer(MessageReader mr, Player pl){
        this.mr = mr;
        this.player = pl;
        this.start();
    }

    @Override
    public void run(){
        new MessageProcessorFactory(this.player).getProcessor(this.mr).start();
    }
}
