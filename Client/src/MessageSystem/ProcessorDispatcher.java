package MessageSystem;

import MessageSystem.Processors.MessageProcessor;
import Utils.Data;

public class ProcessorDispatcher extends Thread{

    private final MessageProcessor mp;

    /**
     * This class acts like a container and runs a MessageProcessor.
     * @param mp : The MessageProcessor to run.
     */
    public ProcessorDispatcher(MessageProcessor mp){
        this.mp = mp;
        if (this.mp != null){
            Data.getInstance().setActionInProgress(this);
            this.start();
        }
    }

    @Override
    public void run(){
        mp.start();
    }
}
