package Server.MessageSystem;

import Utils.GameData;
import Utils.Logger;
import Utils.Models.Player;
import java.io.DataOutputStream;

public class Sender {

    private Player player;

    /**
     * The constructor of Sender class, a class responsible for sending a message to the client(s).
     * This constructor should be used if the message should be sent to a specific player.
     * @param pl : The player whom is receiving the message.
     */
    public Sender (Player pl){
        this.player = pl;
    }

    /**
     * The constructor of Sender class, a class responsible for sending a message to the client(s).
     * This constructor should be used if the message should be sent to more than one client.
     */
    public Sender(){}

    /**
     * Sends out the message to the player given to the constructor.
     * @param messageType : An int indicating the message type. (See MessageType class in Utils/Enums package).
     * @param message : The message to be sent.
     */
    public void send(int messageType,String message){
        DataOutputStream dos = player.getDos();
        try {
            dos.writeInt(message.getBytes().length);
            dos.writeInt(messageType);
            dos.write(message.getBytes());
        }catch (Exception ex){
            Logger.log(ex);
        }
    }

    /**
     * Sends a message to all the connected players.
     * @param messageType : An int indicating the message type. (See MessageType class in Utils/Enums package).
     * @param message : The message to be sent.
     */
    public void sendToAll(int messageType, String message){
        for (Player p : GameData.getInstance().getPlayers()){
            this.player = p;
            this.send(messageType,message);
        }
    }

    /**
     * Sends a message to all the connected players except the given player.
     * @param pl : The player to leave out.
     * @param messageType : An int indicating the message type. (See MessageType class in Utils/Enums package).
     * @param message : The message to be sent.
     */
    public void sendToAllExcept(Player pl,int messageType,String message){
        for (Player p : GameData.getInstance().getPlayers()){
            if (p.getUserName().equals(pl.getUserName()))
                continue;
            this.player = p;
            this.send(messageType,message);
        }
    }
}
