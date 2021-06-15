package MessageSystem.Processors;

import MessageSystem.MessageReader;

public class Conformation implements MessageProcessor{

    private final MessageReader mr;

    public Conformation(MessageReader mr){
        this.mr = mr;
    }

    @Override
    public void start() {
        System.out.println("Press enter to confirm the match.");
    }
}
