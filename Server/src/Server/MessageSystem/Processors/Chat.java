package Server.MessageSystem.Processors;

import Server.MessageSystem.MessageReader;
import Server.MessageSystem.Sender;
import Utils.Enums.GameStatus;
import Utils.Enums.MessageType;
import Utils.GameData;
import Utils.Logger;
import Utils.Models.Player;

import java.io.FileWriter;

public class Chat implements MessageProcessor{

    private final Player pl;
    private final MessageReader mr;

    public Chat (MessageReader mr, Player pl){
        this.mr = mr;
        this.pl = pl;
    }

    @Override
    public void start() {
        if (GameData.getInstance().isPlayerAlive(this.pl) && GameData.getInstance().getGame().getStat() == GameStatus.ChatRoomOpen){
            if (!GameData.getInstance().getGame().getSilencedPlayer().getUserName().equals(this.pl.getUserName())) {
                String publicMessage = this.pl.getUserName() + ": " + this.mr.getMessage();
                new Sender().sendToAll(MessageType.chat, publicMessage);
                try (FileWriter fw = new FileWriter("Chats.log", true)) {
                    fw.write(publicMessage);
                    fw.write("\n");
                    fw.flush();
                } catch (Exception ex) {
                    Logger.log(ex);
                }
            }
            else
                new Sender(this.pl).send(MessageType.info,"You have been silenced by the therapist and can't chat!");
        }
    }
}
