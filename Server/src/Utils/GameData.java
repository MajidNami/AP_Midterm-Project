package Utils;

import java.util.*;
import Server.RolesActions.Action;
import Utils.Enums.Event;
import Utils.Enums.Roles;
import Server.Game.Game;
import Utils.Models.Player;

public class GameData {

    private final ArrayList<Roles> rolesInTheGame = new ArrayList<>();
    private final ArrayList<Roles> availableRoles = new ArrayList<>();
    private final HashMap<String, Player> playersMap = new HashMap<>();
    private final HashMap<Event, Action> nightEvents = new HashMap<>();
    private Game game;

    private final static GameData instance = new GameData();

    /**
     * Returns the only instance of the GameData.
     * @return The only instance of the GameData.
     */
    public static GameData getInstance(){
        return instance;
    }

    /**
     * Adds a new role to the roles list.
     * @param role : The new role.
     */
    public void addARole(Roles role){
        if (!this.rolesInTheGame.contains(role))
            this.rolesInTheGame.add(role);
        if (!this.availableRoles.contains(role))
            this.availableRoles.add(role);
    }

    /**
     * @return The roles list.
     */
    public ArrayList<Roles> getRoles(){
        return this.rolesInTheGame;
    }

    /**
     * This method returns a role and remove that from available roles list.
     * @return A role object.
     */
    public Roles popARole(){
        return availableRoles.remove(new Random().nextInt(availableRoles.size()));
    }

    /**
     * Initializes the game object.
     */
    public void initializeTheGame(){
        this.game = new Game();
    }

    /**
     * @return The game object.
     */
    public Game getGame(){
        return this.game;
    }

    /**
     * Adds a new player.
     * @param pl : The player object.
     */
    public void addAPLayer(Player pl){
        this.playersMap.put(pl.getUserName(),pl);
    }

    /**
     * Returns the Player object associated with that username.
     * @param userName : The players username.
     * @return The player object.
     */
    public Player getPlayer(String userName){
        return this.playersMap.get(userName);
    }

    /**
     * Clears out the night events.
     */
    public void resetNight(){
        this.nightEvents.clear();
    }

    /**
     * Adds a new night event.
     * @param ev : The event to add.
     * @param act : The Action object related to that event.
     */
    public void addNightEvent(Event ev, Action act){
        this.nightEvents.put(ev,act);
    }

    /**
     * @return A HashMap containing the night events.
     */
    public HashMap<Event,Action> getNightEvents(){
        return this.nightEvents;
    }

    /**
     * @param pl : The player object.
     * @return True if the player is alive.
     */
    public boolean isPlayerAlive(Player pl){
        return this.game.isPlayerAlive(pl);
    }

    /**
     * @return An ArrayList containing all the connected payers.
     */
    public Collection<Player> getPlayers(){
        return this.playersMap.values();
    }

    /**
     * Starts the steps of a player's exit.
     * @param userName : The players username.
     */
    public void performPlayerExit(String userName){
        this.game.playerExit(this.playersMap.remove(userName));
    }

    /**
     * Starts the steps of a player's exit.
     * @param player : The player's Player object..
     */
    public void performPlayerExit(Player player){
        this.playersMap.remove(player.getUserName());
        this.game.playerExit(player);
    }

    /**
     * @return A random player.
     */
    public Player getARandomPlayer(){
        return this.game.getARandomPlayer();
    }

    /**
     * Checks if the players username is ok.
     * @param un
     * @return
     */
    public boolean isUserNameOk(String un){
        return !un.equalsIgnoreCase("exit") && !this.playersMap.containsKey(un);
    }

    /**
     * Increments the number of confirmed players by 1.
     */
    public void addConfirmedPlayer(){
        this.game.addPlayerConfirmation();
    }
}
