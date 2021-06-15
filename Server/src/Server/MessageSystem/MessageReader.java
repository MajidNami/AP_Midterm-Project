package Server.MessageSystem;

import java.io.DataInputStream;

public class MessageReader {

    private final DataInputStream dis;
    private String message = "";
    private int messageType;

    /**
     * This class is used dto read a message from the input stream.
     * @param dis : The DataInputStream to read from.
     */
    public MessageReader(DataInputStream dis){
        this.dis = dis;
    }

    /**
     * Reads a new message.
     * @return This MessageReader class.
     * @throws Exception
     */
    public MessageReader read() throws Exception{
        int length = dis.readInt();
        this.messageType = dis.readInt();
        byte[] msg = new byte[length];
        int read = this.dis.read(msg);
        this.message = new String(msg);
        return this;
    }

    /**
     * @return The read message.
     */
    public String getMessage(){
        return this.message;
    }

    /**
     * @return The received message type.
     */
    public int getMessageType(){
        return this.messageType;
    }
}
