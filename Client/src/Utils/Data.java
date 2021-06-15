package Utils;

import Game.*;
import MessageSystem.ProcessorDispatcher;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;

public class Data {

    private ProcessorDispatcher actionInProgress;
    private ServerAddress serverAddress;
    private String userName;
    private Socket connection;
    private DataInputStream dis;
    private DataOutputStream dos;
    private final Game game = new Game();

    private final static Data data = new Data();

    private Data(){}

    /**
     * Sets the username of this player.
     * @param userName : This player's username.
     */
    public void setUserName(String userName){
        this.userName = userName;
    }

    /**
     * Sets the server information.
     * @param ad : The ServerAddress object containing the server's information.
     */
    public void setServerAddress(ServerAddress ad){
        this.serverAddress = ad;
    }

    /**
     * @return The ServerAddress object containing the server's information.
     */
    public ServerAddress getServerAddress(){
        return this.serverAddress;
    }

    /**
     * @return This player's username.
     */
    public String getUserName(){
        return this.userName;
    }

    /**
     * @return The socket of the server and client connection.
     */
    public Socket getConnection() {
        return connection;
    }

    /**
     * This method sets the socket of the server and client connection.
     * @param connection : The socket.
     */
    public void setConnection(Socket connection) {
        this.connection = connection;
        try {
            this.dis = new DataInputStream(this.connection.getInputStream());
            this.dos = new DataOutputStream(this.connection.getOutputStream());
        }catch (Exception ex){
            System.out.println("Exception occurred!\n" + ex);
        }
    }

    /**
     * @return The DataInputStream.
     */
    public DataInputStream getDis() {
        return dis;
    }

    /**
     * @return The DataOutputStream.
     */
    public DataOutputStream getDos() {
        return dos;
    }

    /**
     * @return The game object.
     */
    public Game getGame(){
        return this.game;
    }

    /**
     * Closes the current ProcessorDispatcher by interrupting it.
     */
    public void haltTheProcessor(){
        this.actionInProgress.interrupt();
        System.out.println("Time is up!");
    }

    /**
     * Sets the action in progress.
     * @param pd : ProcessorDispatcher.
     */
    public void setActionInProgress(ProcessorDispatcher pd){
        this.actionInProgress = pd;
    }

    /**
     * @return The only instance of this class.
     */
    public static Data getInstance(){
        return data;
    }
}
