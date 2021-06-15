package Utils.Models;

import Server.MessageSystem.MessageReceiver;
import Server.MessageSystem.Sender;
import Server.RolesActions.Action;
import Utils.Enums.*;
import Utils.GameData;
import Utils.Logger;
import Server.MessageSystem.MessageReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Stack;

public class Player {

    private final Socket clientSocket;
    private DataInputStream dis;
    private DataOutputStream dos;
    private Roles role = null;
    private PlayerStatus status;
    private String userName;
    private Action lastNightAction;
    private MessageReceiver messageReceiver;
    private final Stack<String> roleActionMessages = new Stack<>();
    private final ArrayList<MessageReader> messages = new ArrayList<>();
    private boolean newRoleActionMessage = false;
    private boolean specialActionDone = false;
    private int idleActions = 0;

    public Player(Socket sock){
        this.clientSocket = sock;
        try {
            this.dis = new DataInputStream(sock.getInputStream());
            this.dos = new DataOutputStream(sock.getOutputStream());
            this.readUserName();
            System.out.println("Player " + this.userName + " connected.");
        }catch (Exception ex){
            Logger.log(ex);
        }
        this.startTheReceiver();
    }

    /**
     * @return The socket of the players connection.
     */
    public Socket getClientSocket(){
        return this.clientSocket;
    }

    /**
     * @return The players role.
     */
    public Roles getRole(){
        return this.role;
    }

    /**
     * @return The players status.
     */
    public PlayerStatus getStatus(){
        return this.status;
    }

    /**
     * @return The players username.
     */
    public String getUserName(){
        return this.userName;
    }

    /**
     * @return The players DataInputStream.
     */
    public DataInputStream getDis(){
        return this.dis;
    }

    /**
     * @return The players DataOutputStream.
     */
    public DataOutputStream getDos(){
        return this.dos;
    }

    /**
     * Sets the players role. This method will only work one time which means it will only change the payers role if the role is null.
     * @param role : The players role.
     */
    public void setRole(Roles role){
        if (this.role == null){
            this.role = role;
            new Sender(this).send(MessageType.connection,role + "");
        }
    }

    /**
     * Sets the new status of the player.
     * @param status : The new status.
     */
    public void setStatus(PlayerStatus status){
        this.status = status;
    }

    /**
     * This method is called upon this class creation and reads a username sent the by the client in the form of Connection message.
     * If the user name is not acceptable by the server, a new user name is asked.
     * @throws ClientConnectionException : is thrown if the first message is not a Connection message.
     */
    private void readUserName() throws ClientConnectionException{
        boolean userNameOk = false;
        try {
            while (!userNameOk){
                MessageReader mr = new MessageReader(this.dis).read();
                if (mr.getMessageType() == MessageType.connection) {
                    if (GameData.getInstance().isUserNameOk(mr.getMessage())) {
                        this.userName = mr.getMessage();
                        userNameOk = true;
                    }
                    else
                        new Sender(this).send(MessageType.error,"");
                }
                else
                    throw new ClientConnectionException("Bad first message!");
            }
        }catch (Exception ex){
            Logger.log(ex);
            System.out.println("Player "+  this.userName + " has been disconnected");
            GameData.getInstance().performPlayerExit(this.userName);
        }
    }

    /**
     * Sets this players last night action.
     * @param act : The action.
     */
    public void setLastNightAction(Action act){
        this.lastNightAction = act;
    }

    /**
     * @return The player's last night action.
     */
    public Action getLastNightAction(){
        return this.lastNightAction;
    }

    /**
     * Adds a new message to the player's message list.
     * @param msg : The message.
     */
    public void addAMessage(MessageReader msg){
        this.messages.add(msg);
    }

    /**
     * This method returns the players messages.
     * This method is not safe to use and may cause malfunction.
     * @return The message list of the player.
     */
    public ArrayList<MessageReader> getMessages(){
        return this.messages;
    }

    /**
     * Stops the player's message receiving thread.
     */
    public void haltTheReceiver(){
        this.messageReceiver.close();
    }

    /**
     * This method is called upon the creation of this class (after the username has been read) and starts a new thread which
     * will receive and analyze the players messages.
     */
    private void startTheReceiver(){
        this.messageReceiver = new MessageReceiver(this);
    }

    /**
     * Adds a new RoleAction message received from the client and sets the newRoleActionMessage to true to indicate that a new message has been received.
     * @param msg : The last roleAction message (can be null).
     */
    public void addARoleActionMessage(String msg){
        this.roleActionMessages.add(msg);
        this.newRoleActionMessage = true;
    }

    /**
     * This method returns the last RoleAction message received from this player.
     * @return The last RoleAction message received from this player.
     */
    public String getLastRoleActionMessage(){
        this.newRoleActionMessage = false;
        return this.roleActionMessages.peek();
    }

    /**
     * This method increments the number of role action times that no message has been received from the player.
     * This method is used to track the idle state of a player and finally kick the player that have been inactive for more than 3 rounds.
     */
    public void addIdleAction(){
        this.idleActions ++;
    }

    /**
     * Resets the idleActions to 0.
     */
    public void resetIdleActions(){
        this.idleActions = 0;
    }

    /**
     * @return The number of continuous role actions that the player has been inactive.
     */
    public int getIdleActions(){
        return this.idleActions;
    }

    /**
     * This method is for checking if a new RoleAction message has arrived.
     * @return The newRoleActionMessage flag.
     */
    public boolean hasNewRAMsgArrived(){
        return this.newRoleActionMessage;
    }

    /**
     * This method changes the specialActionDone flag.
     * @param sp : The new flag.
     */
    public void setSpecialActionDone(boolean sp){
        this.specialActionDone = sp;
    }

    /**
     * This method returns the current specialActionDone flag.
     * @return boolean indicating if the special action has been done or not.
     */
    public boolean isSpecialActionDone(){
        return this.specialActionDone;
    }

    @Override
    public String toString(){
        return this.userName;
    }
}
