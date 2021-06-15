package Server;

import Utils.ConfigsAndData;
import Utils.Logger;
import Utils.Models.Player;
import java.net.ServerSocket;

public class MainConnectionThread extends Thread{

    @Override
    public void run(){
        try {
            ServerSocket ss = new ServerSocket(ConfigsAndData.getInstance().getPort());
            System.out.println("Server is running ...");
            while (true)
                new PlayerEvaluator(new Player(ss.accept()));
        }catch (Exception ex){
            Logger.log(ex);
        }
    }
}
