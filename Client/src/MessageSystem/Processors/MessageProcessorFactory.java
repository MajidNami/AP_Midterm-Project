package MessageSystem.Processors;

import MessageSystem.MessageReader;

public class MessageProcessorFactory {

    private final MessageReader mr;

    public MessageProcessorFactory(MessageReader mr){
        this.mr = mr;
    }

    /**
     * This method returns a class which implements the MessageProcessor interface based on the MessageType.
     * @return A class implementing the MessageProcessor interface.
     */
    public MessageProcessor getProcessor(){
        switch (this.mr.getMessageType()){
            case 0:
                return new Connection(this.mr);
            case 1:
                return new RoleAction(this.mr);
            case 2:
                return new Info(this.mr);
            case 3:
                return new Chat(this.mr);
            case 4:
                return new StatusChange(this.mr);
            case 5:
                return new Error(this.mr);
            case 7:
                return new Conformation(this.mr);
            case 8:
                return new Death(this.mr);
        }
        return null;
    }
}
