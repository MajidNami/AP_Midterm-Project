package Server.RolesActions;

import Utils.Enums.ActionType;
import Utils.Enums.Roles;
import Utils.GameData;
import Utils.Models.Player;

public class Action {

    private final Player player;
    private ActionType actionType;
    private Player subject1 = GameData.getInstance().getARandomPlayer();
    private Player subject2 = GameData.getInstance().getARandomPlayer();
    private boolean result = false;

    public Action(Player player){
        this.player = player;
        this.determineActionType(player.getRole());
    }

    public void setSubject1(Player player){
        this.subject1 = player;
    }

    public void setResult(boolean res){
        this.result = res;
    }

    public void setSubject2(Player subject2) {
        this.subject2 = subject2;
    }

    public boolean getResult(){
        return this.result;
    }

    public Player getSubject1(){
        return this.subject1;
    }

    public ActionType getActionType(){
        return this.actionType;
    }

    public Player getSubject2() {
        return this.subject2;
    }

    private void determineActionType(Roles role){
        switch (role){
            case mayor:
                this.actionType = ActionType.mayor;
                break;
            case therapist:
                this.actionType = ActionType.silencedAPlayer;
                break;
            case inspector:
                this.actionType = ActionType.inspectedAPlayer;
                break;
            case dieHard:
                this.actionType = ActionType.askedForInquiry;
                break;
            case sniper:
            case godFather:
                this.actionType = ActionType.shotAPlayer;
                break;
            case cityDoctor:
            case doctorLecter:
                this.actionType = ActionType.savedAPlayer;
                break;
            default:
                this.actionType = ActionType.none;
                break;
        }
    }

    public Player getPlayer(){
        return this.player;
    }
}
