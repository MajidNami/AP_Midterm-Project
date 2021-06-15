import MessageSystem.MessageReader;
import MessageSystem.MessageReceiver;
import MessageSystem.Processors.MessageProcessorFactory;
import MessageSystem.Sender;
import Utils.Data;
import Utils.Enums.MessageTypes;
import Utils.STDINReader;
import java.net.Socket;
import java.util.Scanner;

public class MainThread extends Thread{

    public MainThread(){
        this.start();
    }

    @Override
    public void run(){
        this.initTCPConnectionToServer();
        this.startTheConnection();
        //Start the MessageReceiver thread.
        new MessageReceiver();
        //Start the STDINReader thread.
        new STDINReader();
    }

    /**
     * Initiates a TCP connection to the server.
     */
    private void initTCPConnectionToServer(){
        try {
            Data.getInstance().setConnection(new Socket(
                    Data.getInstance().getServerAddress().getHost(),
                    Data.getInstance().getServerAddress().getPort()
            ));
        }catch (Exception ex){
            System.out.println("Exception occurred!\n" + ex);
        }
    }

    /**
     * Starts the connection to the server by sending a Connection message.
     */
    private void startTheConnection(){
        boolean userNameOK = false;
        try {
            while (!userNameOK){
                new Sender().send(MessageTypes.connection, Data.getInstance().getUserName());
                MessageReader mr = new MessageReader(Data.getInstance().getDis());
                if (mr.getMessageType() == MessageTypes.connection){
                    userNameOK = true;
                    new MessageProcessorFactory(mr).getProcessor().start();
                }
                else if (mr.getMessageType() == MessageTypes.error) {
                    System.out.println("User name not available. Please enter another user name :");
                    Data.getInstance().setUserName(new Scanner(System.in).nextLine());
                }
            }
        }catch (Exception ex){
            System.out.println("Exception occurred in msg receiver system!");
            System.out.println("Server has been disconnected!");
        }
    }
}
