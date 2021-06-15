package Server.RolesActions;

import Server.MessageSystem.Sender;
import Utils.Enums.MessageType;
import Utils.Enums.Roles;
import Utils.GameData;
import Utils.Logger;
import Utils.Models.Player;

public abstract class RoleAction {

    protected Action action;
    protected Player player;
    protected boolean sleepInterrupted = false;

    /**
     * This method invokes the special action of each player.
     * This method should be override by children.
     */
    public abstract void startTheAct();

    /**
     * This method starts the voting in the day.
     * This method is final and should not and can not be override.
     */
    public final void startDayVoting(){
        new Sender(player).send(MessageType.roleAction,"You have 30 seconds to vote for a player to be hanged! (Type the username):");
        this.getPlayer(1);
        new Sender(this.player).send(MessageType.roleAction,"Your vote for " + this.action.getSubject1() + " has been submitted. If you want to change your vote, type another username :");
        new Sender().sendToAllExcept(player,MessageType.info,"Player " + this.player + " voted for " + this.action.getSubject1());
        if (this.player.getRole() != Roles.mayor){
            while (!sleepInterrupted) {
                this.getPlayer(1);
                if (!sleepInterrupted) {
                    new Sender(this.player).send(MessageType.roleAction, "Your vote has been changed to " + this.action.getSubject1());
                    new Sender().sendToAllExcept(player, MessageType.info, "Player " + this.player + " has changed their vote to " + this.action.getSubject1());
                }
            }
        }
    }

    /**
     * This method is used for processing and receiving a player from the client.
     * This method can be used in voting and special actions.
     * @param sub : The subject slot (of Action class) to save the player in.
     */
    protected final void getPlayer(int sub){
        try {
            boolean actEnded = false;
            while (!actEnded) {
                this.waitForNewMessage();
                if (!sleepInterrupted) {
                    String userName = this.player.getLastRoleActionMessage();
                    if (userName != null) {
                        Player pl = GameData.getInstance().getPlayer(userName);
                        if (pl != null) {
                            boolean continuePerm = true;
                            if (this.player.getRole() == Roles.doctorLecter){
                                if (sub == 1){
                                    if (this.player.getUserName().equals(pl.getUserName())){
                                        if (!this.player.isSpecialActionDone())
                                            this.player.setSpecialActionDone(true);
                                        else
                                            continuePerm = false;
                                    }
                                }
                            }
                            else if (this.player.getRole() == Roles.cityDoctor){
                                if (this.player.getUserName().equals(pl.getUserName())){
                                    if (!this.player.isSpecialActionDone())
                                        this.player.setSpecialActionDone(true);
                                    else
                                        continuePerm = false;
                                }
                            }
                            if (continuePerm){
                                if (GameData.getInstance().isPlayerAlive(pl)) {
                                    if (sub == 1)
                                        this.action.setSubject1(pl);
                                    else
                                        this.action.setSubject2(pl);
                                    actEnded = true;
                                } else {
                                    new Sender(player).send(MessageType.error, "This player is already dead! Please type another username :");
                                    new Sender(player).send(MessageType.roleAction, "");
                                }
                            }else{
                                new Sender(player).send(MessageType.error, "You can't choose your self again! Please type another username :");
                                new Sender(player).send(MessageType.roleAction, "");
                            }
                        } else {
                            new Sender(player).send(MessageType.error, "Username does not exists! Please type a correct username :");
                            new Sender(player).send(MessageType.roleAction, "");
                        }
                    }
                }
            }
        }catch (Exception ex){
            Logger.log(ex);
        }
    }

    /**
     * This method pauses the Thread (RoleActionDispatcher) until a new message arrives.
     */
    protected final void waitForNewMessage(){
        this.player.addIdleAction();
        try {
            while (!this.player.hasNewRAMsgArrived())
                Thread.sleep(500);
            this.player.resetIdleActions();
        }catch (Exception ex){
            this.sleepInterrupted = true;
            Logger.log(ex);
        }
    }

    /**
     * Returns the Action object of this RoleAction class.
     * @return Action class associated with this RoleAction.
     */
    public Action getAction(){
        return this.action;
    }
}
