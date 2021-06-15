import Server.MainConnectionThread;
import Server.Starter;
import Utils.ConfigsAndData;
import java.util.Scanner;

public class Main {

    public static void main(String[] args){
        Scanner input = new Scanner(System.in);
        System.out.println("Enter the port to run the server on (enter 0 to run of default [27960]) :");
        int port = input.nextInt();
        if (port != 0)
            ConfigsAndData.getInstance().setPort(port);
        System.out.println("Enter the number of players (6 and above) :");
        int plNm = input.nextInt();
        if (plNm >= 6) {
            ConfigsAndData.getInstance().setPlayersNumber(plNm);
            startTheServer();
        }
        else
            System.out.println(plNm + " player" + (plNm == 1 ? " is" : "s are") + " not enough for Mafia.");
    }

    private static void startTheServer(){
        new Starter().start();
        new MainConnectionThread().start();
    }
}
