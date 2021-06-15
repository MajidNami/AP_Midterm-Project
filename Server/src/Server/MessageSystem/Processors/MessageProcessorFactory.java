package Server.MessageSystem.Processors;

import Server.MessageSystem.MessageReader;
import Utils.Enums.MessageType;
import Utils.Models.Player;

public class MessageProcessorFactory {

    private final Player player;

    public MessageProcessorFactory(Player pl){
        this.player = pl;
    }

    /**
     * This method returns the suitable MessageProcessor according to the message type.
     * @param mr : The MessageReader class containing the information of the message.
     * @return A Class implementing the MessageProcessor interface.
     */
    public MessageProcessor getProcessor(MessageReader mr){
        if (mr.getMessageType() == MessageType.exit)
            return new Exit(mr,player);
        else if (mr.getMessageType() == MessageType.chat)
            return new Chat(mr,player);
        else if (mr.getMessageType() == MessageType.roleAction)
            return new RoleAction(mr,player);
        else if (mr.getMessageType() == MessageType.conformation)
            return new Conformation(mr,player);
        return null;
    }
}
