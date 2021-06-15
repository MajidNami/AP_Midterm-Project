import Utils.Data;
import Utils.ServerAddress;

import java.util.Scanner;

public class Main {

    public static void main(String[] args){
        String host;
        int port;
        System.out.println("Enter the server address [Host:port] :");
        String[] address = new Scanner(System.in).next().split(":",2);
        if (address.length == 2) {
            host = address[0];
            port = Integer.parseInt(address[1]);
            Data.getInstance().setServerAddress(new ServerAddress(host,port));
            System.out.println("Enter your username : ");
            String userName = new Scanner(System.in).next();
            Data.getInstance().setUserName(userName);
            new MainThread();
        }else
            System.out.println("Server address is not correct!");
    }
}
