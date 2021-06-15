package MessageSystem.Processors;

import MessageSystem.MessageReader;

public class Error implements MessageProcessor{

    private final MessageReader mr;

    public Error(MessageReader mr){
        this.mr = mr;
    }

    @Override
    public void start() {
        System.out.println("Server has reported an error : \n" + mr.getMessage());
    }
}
