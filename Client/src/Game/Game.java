package Game;

import Utils.Data;
import Utils.Enums.*;

public class Game {

    private GameStatus status = GameStatus.PreLaunch;
    private Roles playerRole;
    private boolean playerRoleActionTime = false;

    /**
     * @return This players role.
     */
    public Roles getRole(){
        return this.playerRole;
    }

    /**
     * @return The current status of the game.
     */
    public GameStatus getStatus() {
        return status;
    }

    /**
     * This method is used fr changing the game status.
     * @param status : The new status.
     */
    public void setStatus(GameStatus status) {
        this.status = status;
        if (this.status == GameStatus.CityNight || this.status == GameStatus.Day || this.status == GameStatus.VoteFinished)
            Data.getInstance().haltTheProcessor();
    }

    /**
     * This method is for setting the players role and will only work once.
     * @param role : The players role.
     */
    public void setPlayerRole(Roles role){
        if (this.playerRole == null)
            this.playerRole = role;
    }

    /**
     * This method sets the playerRoleActionTime flag.
     * @param rt : boolean
     */
    public void setPlayerRoleActionTime(boolean rt){
        this.playerRoleActionTime = rt;
    }

    /**
     * This method checks if it's player role action time.
     * @return boolean indicating if its player role action time.
     */
    public boolean isPlayerRoleActionTime(){
        return this.playerRoleActionTime;
    }
}
