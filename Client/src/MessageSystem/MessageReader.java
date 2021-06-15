package MessageSystem;

import java.io.DataInputStream;

public class MessageReader {

    private final DataInputStream dis;
    private int messageType;
    private String message;

    /**
     * This class is used for reading a message.
     * @param dis : DataInputStream associated with the connection to the server.
     * @throws Exception
     */
    public MessageReader(DataInputStream dis) throws Exception{
        this.dis = dis;
        this.read();
    }

    /**
     * Reads one message from the input stream.
     * @throws Exception
     */
    private void read() throws Exception{
        int length = dis.readInt();
        this.messageType = dis.readInt();
        byte[] msg = new byte[length];
        int read = this.dis.read(msg);
        this.message = new String(msg);
    }

    /**
     * @return The message's message type.
     */
    public int getMessageType(){
        return this.messageType;
    }

    /**
     * @return The received message.
     */
    public String getMessage(){
        return this.message;
    }
}
