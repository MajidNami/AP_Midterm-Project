package Server.Game;

import Server.MessageSystem.Sender;
import Utils.ConfigsAndData;
import Utils.Enums.*;
import Utils.GameData;
import Utils.Logger;
import Utils.Models.Player;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

public class Game extends Thread{

    private final ArrayList<Player> players = new ArrayList<>();
    private final ArrayList<Player> deadPlayers = new ArrayList<>();
    private final ArrayList<Player> alivePlayers = new ArrayList<>();
    private final HashMap<Teams, HashMap<Roles,ArrayList<Player>>> playersRoles = new HashMap<>();
    private final GameCycle cycle = new GameCycle(this);
    private int round = 0;
    private int mafiaAlive = 0;
    private int cityAlive = 0;
    private int playersConfirmed = 0;
    private int dieHardInquiry = 0;
    private int dieHardTakenShots = 0;
    private GameStatus stat = GameStatus.PreLaunch;
    private int playersConnected = 0;
    private Player mafiaLeader;
    private Player silencedPlayer;

    public Game(){
        this.playersRoles.put(Teams.City,new HashMap<>());
        this.playersRoles.put(Teams.Mafia,new HashMap<>());
    }

    /**
     * Adds a new player to the game.
     * This method is private and it's called by either addAMafiaPlayer or addACityPlayer functions.
     * @param team : The HashMap associated with the players team.
     * @param pl : The Player object.
     */
    private void addAPlayer(HashMap<Roles,ArrayList<Player>> team,Player pl){
        if (team.get(pl.getRole()) != null)
            team.get(pl.getRole()).add(pl);
        else{
            ArrayList<Player> newList = new ArrayList<>();
            newList.add(pl);
            team.put(pl.getRole(),newList);
        }
        this.players.add(pl);
    }

    /**
     * Adds a new player to the Mafia team.
     * @param pl : The Player object.
     */
    public void addMafiaPlayer(Player pl){
        HashMap<Roles,ArrayList<Player>> team = this.playersRoles.get(Teams.Mafia);
        this.addAPlayer(team,pl);
        this.mafiaAlive ++;
    }

    /**
     * Adds a new player to the City team.
     * @param pl : The Player object.
     */
    public void addCityPlayer(Player pl){
        HashMap<Roles,ArrayList<Player>> team = this.playersRoles.get(Teams.City);
        this.addAPlayer(team,pl);
        this.cityAlive ++;
    }

    /**
     * Evaluates a player role and returns is the player is in Mafia team.
     * @param player : The Player object.
     * @return boolean indicating the player is in Mafia team or not.
     */
    public boolean isMafia(Player player){
        return
                player.getRole() == Roles.godFather
                || player.getRole() == Roles.doctorLecter
                || player.getRole() == Roles.simpleMafia;
    }

    /**
     * Checks if the player is alive or not.
     * @param pl : The Player object.
     * @return boolean indicating if the player is alive or not.
     */
    public boolean isPlayerAlive(Player pl){
        return this.alivePlayers.contains(pl);
    }

    /**
     * Checks if the player is alive or not.
     * @param role : The role of the player.
     * @param team : The team of the player.
     * @return boolean indicating if the player is alive or not.
     */
    public boolean isPlayerAlive(Roles role,Teams team){
        boolean result = false;
        for (Player p : this.playersRoles.get(team).get(role)){
            if (this.isPlayerAlive(p)){
                result = true;
                break;
            }
        }
        return result;
    }

    /**
     * This method is for retrieving a player's Player object.
     * @param role : The role of the player.
     * @param team : The team of the player.
     * @return The Player object associated with the given information.
     */
    public Player getPlayer(Roles role,Teams team){
        for (Player p : this.playersRoles.get(team).get(role)){
            if (this.isPlayerAlive(p))
                return p;
        }
        return this.playersRoles.get(team).get(role).get(0);
    }

    /**
     * Adds a new player to game.
     * @param pl : The Player object.
     */
    public void addAPlayer(Player pl){
        pl.setRole(GameData.getInstance().popARole());
        if (this.isMafia(pl))
            this.addMafiaPlayer(pl);
        else
            this.addCityPlayer(pl);
        GameData.getInstance().addAPLayer(pl);
        this.alivePlayers.add(pl);
        new Sender().sendToAllExcept(pl,MessageType.info,"Player " + pl.getUserName() + " connected! " + (this.playersConnected + 1) + '/' + ConfigsAndData.getInstance().getPlayersNumber());
        if (++playersConnected == ConfigsAndData.getInstance().getPlayersNumber())
            this.start();
        else
            new Sender(pl).send(MessageType.info,"Waiting for other players ...");
    }

    /**
     * @return The number players connected to the server.
     */
    public int getPlayersConnected(){
        return this.playersConnected;
    }

    /**
     * @return How many rounds have passed.
     */
    public int getRound(){
        return this.round;
    }

    /**
     * Increments the rounds number of the game by 1.
     */
    public void addRound(){
        this.round ++;
    }

    /**
     * Kills a player and moves the Player object to the dead player list.
     * @param pl : The Player object.
     */
    public void killAPlayer(Player pl){
        this.deadPlayers.add(this.alivePlayers.remove(this.alivePlayers.indexOf(pl)));
        if (isMafia(pl)) {
            mafiaAlive--;
            if (pl.getRole() == Roles.godFather)
                this.chooseTheMafiaLeader();
        }
        else
            cityAlive --;
        new Sender(pl).send(MessageType.death,"you have died " + pl.getUserName() + "!");
        pl.haltTheReceiver();
    }

    /**
     * @return The list of the players.
     */
    public ArrayList<Player> getPlayers(){
        return this.players;
    }

    /**
     * This method is for retrieving a team's players list.
     * @param team : The team that you want the players list.
     * @return An ArrayList containing the players.
     */
    public ArrayList<Player> getTeam(Teams team){
        ArrayList<Player> out = new ArrayList<>();
        for (ArrayList<Player> ap : this.playersRoles.get(team).values()){
            out.addAll(ap);
        }
        return out;
    }

    /**
     * @return The status of the game.
     */
    public GameStatus getStat(){
        return this.stat;
    }

    /**
     * Sets the status of the game.
     * @param status : The new status.
     */
    public void setStat(GameStatus status){
        this.stat = status;
        new Sender().sendToAll(MessageType.statusChange,status + "");
    }

    /**
     * @return Number of alive Players in the Mafia team.
     */
    public int getMafiaAlive(){
        return this.mafiaAlive;
    }

    /**
     * @return Number of alive Players in the City team.
     */
    public int getCityAlive(){
        return this.cityAlive;
    }

    /**
     * @return An ArrayList containing the dead players.
     */
    public ArrayList<Player> getDeadPlayers(){
        return this.deadPlayers;
    }

    /**
     * @return An ArrayList containing the alive players.
     */
    public ArrayList<Player> getAlivePlayers(){
        return this.alivePlayers;
    }

    /**
     * Kicks a player ouf of the game and sends a death message to the player.
     * @param p : Player to be kicked.
     */
    public void kickAPLayer(Player p){
        new Sender(p).send(MessageType.info,"You've been inactive for 3 days.");
        this.killAPlayer(p);
        new Sender().sendToAll(MessageType.info,"Player " + p + " has been kicked due to inactivity.");
    }

    /**
     * Initiates the player exit.
     * @param player : The Player object.
     */
    public void playerExit(Player player){
        this.alivePlayers.remove(player);
        this.deadPlayers.remove(player);
        this.players.remove(player);
        if (player.getRole() != null){
            if (this.isMafia(player)) {
                this.playersRoles.get(Teams.Mafia).remove(player.getRole());
                if (this.isPlayerAlive(player) && this.mafiaAlive > 0)
                    this.mafiaAlive--;
            } else {
                this.playersRoles.get(Teams.City).remove(player.getRole());
                if (this.isPlayerAlive(player) && this.cityAlive > 0)
                    this.cityAlive--;
            }
        }
        this.playersConnected --;
        if (this.stat == GameStatus.PreLaunch && player.getRole() != null)
            GameData.getInstance().addARole(player.getRole());
        new Sender().sendToAll(MessageType.info, player.getUserName() + " just exited the game.");
        System.out.println(player.getUserName() + " just exited the game.");
    }

    /**
     * Returns a random alive player for a situation that the client has not decided to pick a player or has ran out of time.
     * @return A random player.
     */
    public Player getARandomPlayer(){
        return this.alivePlayers.get(new Random().nextInt(this.alivePlayers.size()));
    }

    /**
     * Increments the number of confirmed players by 1. This method is and should be used only in PreLaunch status.
     */
    public void addPlayerConfirmation(){
        this.playersConfirmed ++;
    }

    /**
     * This method pauses the game thread until all players have confirmed the match.
     */
    public void waitForConfirmations(){
        new Sender().sendToAll(MessageType.conformation,"");
        System.out.println("Waiting for confirmation ...");
        try {
            while (playersConfirmed != playersConnected)
                Thread.sleep(500);
        }catch (Exception ex){
            Logger.log(ex);
        }
    }

    /**
     * This method returns the current mafia leader. This method has been defined for a situation where the god father has died.
     * @return The Player object of the mafia leader.
     */
    public Player getMafiaLeader(){
        return this.mafiaLeader;
    }

    /**
     * This method has been defined to choose the mafia leader according to the situation.
     * If god father is alive the god father is chosen, if not doctor lecter and if not a random alive simple mafia.
     */
    public void chooseTheMafiaLeader(){
        if (this.isPlayerAlive(Roles.godFather,Teams.Mafia))
            this.mafiaLeader = this.getPlayer(Roles.godFather,Teams.Mafia);
        else{
            if (this.isPlayerAlive(Roles.doctorLecter,Teams.Mafia))
                this.mafiaLeader = this.getPlayer(Roles.doctorLecter,Teams.Mafia);
            else{
                this.mafiaLeader = getPlayer(Roles.simpleMafia,Teams.Mafia);
            }
        }
        new Sender(this.mafiaLeader).send(MessageType.info,"You are the mafia leader!");
    }

    /**
     * Increments the number of die hard inquiry by 1.
     */
    public void addDieHarInquiry(){
        this.dieHardInquiry ++;
    }

    /**
     * Increments the number of die hard taken shots by 1.
     */
    public void addDieHardTakenSHot(){
        this.dieHardTakenShots ++;
    }

    /**
     * @return The number of die hard inquiry.
     */
    public int getDieHardInquiry(){
        return this.dieHardInquiry;
    }

    /**
     * @return The number of die hard taken shots.
     */
    public int getDieHardTakenShots(){
        return this.dieHardTakenShots;
    }

    /**
     * This method returns the silenced player object.
     * @return The silenced player object.
     */
    public Player getSilencedPlayer(){
        return this.silencedPlayer;
    }

    /**
     * This method sets the silenced player.
     * @param pl : The new silenced Player object.
     */
    public void setSilencedPlayer(Player pl){
        this.silencedPlayer = pl;
    }

    @Override
    public void run(){
        try {
            this.chooseTheMafiaLeader();
            new Sender().sendToAll(MessageType.info, this.playersConnected + " players are online.\nGame will begin after conformation ...");
            this.waitForConfirmations();
            new Sender().sendToAll(MessageType.info, "Game will begin in 5 seconds ...");
            Thread.sleep(1000);
            new Sender().sendToAll(MessageType.info, "4");
            Thread.sleep(1000);
            new Sender().sendToAll(MessageType.info, "3");
            Thread.sleep(1000);
            new Sender().sendToAll(MessageType.info, "2");
            Thread.sleep(1000);
            new Sender().sendToAll(MessageType.info, "1");
            Thread.sleep(1000);
            this.cycle.start();
        }catch (Exception ex){
            Logger.log(ex);
        }
    }
}
