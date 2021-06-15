package MessageSystem.Processors;

import MessageSystem.MessageReader;

public class Info implements MessageProcessor{

    private final MessageReader mr;

    public Info(MessageReader mr){
        this.mr = mr;
    }

    @Override
    public void start() {
        System.out.println(mr.getMessage());
    }
}
