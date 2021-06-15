package Utils;

import MessageSystem.Sender;
import Utils.Enums.GameStatus;
import Utils.Enums.MessageTypes;
import java.util.Scanner;

public class STDINReader extends Thread{

    /**
     * This class is used to always (until the player is alive) read from the STDIN.
     */
    public STDINReader(){
        this.start();
    }

    @Override
    public void run(){
        while (Data.getInstance().getGame().getStatus() != GameStatus.MafiaWin || Data.getInstance().getGame().getStatus() != GameStatus.CityWin)
            this.analyzeTheInput(new Scanner(System.in).nextLine());
    }

    /**
     * This method analyzes the received message from the user.
     * @param message : The message received from the user.
     */
    private void analyzeTheInput(String message){
        if (!message.equalsIgnoreCase("exit")) {
            if (Data.getInstance().getGame().getStatus() != null){
                if (Data.getInstance().getGame().getStatus() == GameStatus.ChatRoomOpen)
                    new Sender().send(MessageTypes.chat, message);
                else if (Data.getInstance().getGame().getStatus() == GameStatus.PreLaunch)
                    new Sender().send(MessageTypes.conformation, "");
                else if (Data.getInstance().getGame().getStatus() == GameStatus.Vote || Data.getInstance().getGame().getStatus() == GameStatus.MafiaNight ||
                        Data.getInstance().getGame().getStatus() == GameStatus.CityNight) {
                    if (Data.getInstance().getGame().isPlayerRoleActionTime()) {
                        Data.getInstance().getGame().setPlayerRoleActionTime(false);
                        new Sender().send(MessageTypes.roleAction, message);
                    }
                }
            }else{
                if (message.toLowerCase().startsWith("y")) {
                    System.out.println("Thanks for playing, goodbye!");
                    new Sender().send(MessageTypes.exit,"");
                    Runtime.getRuntime().halt(0);
                }else
                    System.out.println("You will stay but you won't be able to chat.");
            }
        }
        else
            new Sender().send(MessageTypes.exit,"");
    }
}
