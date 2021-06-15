package MessageSystem;

import MessageSystem.Processors.MessageProcessorFactory;
import Utils.Data;

public class MessageReceiver extends Thread{

    /**
     * This class is a child of the Thread class and is initiated upon the connection to the server.
     * This class is responsible for (Until the player is alive) receiving messages from the server.
     */
    public MessageReceiver(){
        this.start();
    }

    @Override
    public void run(){
        try {
            while (!Data.getInstance().getConnection().isClosed())
                new ProcessorDispatcher(
                        new MessageProcessorFactory(
                                new MessageReader(Data.getInstance().getDis())
                        ).getProcessor()
                );
        }catch (Exception ex){
            System.out.println("Exception occurred in msg receiver system!");
            System.out.println("Server has been disconnected!");
        }
    }
}
