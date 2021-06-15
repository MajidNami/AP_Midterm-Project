package Server.Game;

import Server.RolesActions.*;
import Server.MessageSystem.Sender;
import Utils.Enums.*;
import Utils.GameData;
import Utils.Logger;
import Utils.Models.Player;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.atomic.AtomicInteger;

public class GameCycle {

    /**
     * Indicates if it's the mayor action time.
     */
    private boolean mayorActionTime = false;
    private final Game game;
    private final Object lock = new Object();
    private final ArrayList<RoleActionDispatcher> dispatchers = new ArrayList<>();
    private final HashMap<Player, AtomicInteger> votes = new HashMap<>();

    public GameCycle(Game gm){
        this.game = gm;
    }

    /**
     * Starts the game.
     * This method should be called only once and upon the game start.
     */
    public void start(){
        this.game.setStat(GameStatus.MeetNight);
        this.tellTheRoles();
        this.startTheGameCycle();
    }

    /**
     * Announces each player's role.
     * This method should be called only once and upon the game start.
     */
    private void tellTheRoles(){
        for (Player p : game.getPlayers())
            new Sender(p).send(MessageType.info,"\nyour role is " + p.getRole());
        this.pauseTheThread(2);
        this.tellTheRolesToOtherRoles();
    }

    /**
     * Tells each player the players that they should know.
     * This method should be called only once and upon the game start.
     */
    private void tellTheRolesToOtherRoles(){
        for (Player p1 : this.game.getTeam(Teams.Mafia)){
            StringBuilder msg = new StringBuilder("\nYour teammates are : \n");
            for (Player p2 : this.game.getTeam(Teams.Mafia)){
                if (p1.getUserName().equals(p2.getUserName()))
                    continue;
                msg.append(p2.getUserName()).append(" :").append(p2.getRole()).append('\n');
            }
            new Sender(p1).send(MessageType.info,msg.toString());
        }
        new Sender(this.game.getPlayer(Roles.mayor,Teams.City)).send(MessageType.info,"Player " +
                this.game.getPlayer(Roles.cityDoctor,Teams.City).getUserName() + " is the doctor.");
        this.pauseTheThread(3);
    }

    /**
     * The main method which will begin the game cycle.
     * This method should be called only once and upon the game start.
     */
    private void startTheGameCycle(){
        new Sender().sendToAll(MessageType.info,"\nWelcome to the MAFIA! Play fare and have fun.");
        while (this.game.getStat() != GameStatus.MafiaWin && this.game.getStat() != GameStatus.CityWin){
            this.game.addRound();
            this.startTheDay();
            if(this.checkForGameEnd())
                break;
            this.startTheNight();
            if(this.checkForGameEnd())
                break;
        }
        this.endTheGame();
    }

    /**
     * This method starts the night in the game.
     */
    private void startTheNight(){
        GameData.getInstance().resetNight();
        this.game.setStat(GameStatus.MafiaNight);
        new Sender().sendToAll(MessageType.info,"Be ware Mafia team is now awake.");
        Action[] actions = new Action[this.game.getTeam(Teams.Mafia).size() - 1];
        int index = 0;
        for (Player p : this.game.getTeam(Teams.Mafia)){
            if (p.getRole() == Roles.godFather)
                continue;
            RoleAction act = new RoleActionFactory().getAction(p);
            this.dispatchers.add(new RoleActionDispatcher(act,p.getRole()));
            actions[index] = act.getAction();
            index ++;
        }
        this.waitForActions();
        Player mafiaLeader = this.game.getMafiaLeader();
        this.dispatchers.add(new RoleActionDispatcher(new GodFather(mafiaLeader,actions),mafiaLeader.getRole()));
        this.waitForActions();
        this.game.setStat(GameStatus.CityNight);
        new Sender().sendToAll(MessageType.info,"Now it's time for the City team to wakeup!");
        for (Player p : this.game.getTeam(Teams.City)) {
            if (p.getRole() == Roles.mayor)
                continue;
            this.dispatchers.add(new RoleActionDispatcher(new RoleActionFactory().getAction(p),p.getRole()));
        }
        this.waitForActions();
    }

    /**
     * This method starts the day in the game.
     */
    private void startTheDay(){
        this.game.setStat(GameStatus.Day);
        this.checkForInactivePlayers();
        if (this.game.getRound() > 1) {
            NightAnalyzer na = new NightAnalyzer(this.game).startAnalyzing();
            new Sender().sendToAll(MessageType.info, "Night is over, it's time to reveal the night actions.\n" + na.getResult());
            this.pauseTheThread(5);
        }
        if(!this.checkForGameEnd()) {
            StringBuilder sb = new StringBuilder("Players alive : \n");
            for (Player p : this.game.getAlivePlayers())
                sb.append(p.getUserName()).append('\n');
            new Sender().sendToAll(MessageType.info, sb + "\nChatroom will be open for 2 minutes then voting begins.");
            this.game.setStat(GameStatus.ChatRoomOpen);
            this.pauseTheThread(120);
            this.game.setStat(GameStatus.Vote);
            new Sender().sendToAll(MessageType.info, "Now it's time to vote. Each player has 30 seconds to vote");
            for (Player p : this.game.getAlivePlayers())
                this.dispatchers.add(new RoleActionDispatcher(new RoleActionFactory().getAction(p),p.getRole()));
            this.waitForActions();
            Player toBeHanged = this.calculateTheVotes();
            new Sender().sendToAll(MessageType.info, "Voting has been finished. The player who is going to be hanged is " + toBeHanged +
                    "\nMayor will have 30 seconds to decide ...");
            this.mayorActionTime = true;
            Player mayor = this.game.getPlayer(Roles.mayor, Teams.City);
            if (this.game.isPlayerAlive(mayor))
                this.dispatchers.add(new RoleActionDispatcher(new RoleActionFactory().getAction(mayor),mayor.getRole()));
            this.waitForActions();
            this.game.setStat(GameStatus.VoteFinished);
            if (mayor.getLastNightAction().getResult())
                new Sender().sendToAll(MessageType.info, "Mayor has decided to cancel the voting.");
            else {
                new Sender().sendToAllExcept(toBeHanged,MessageType.info, "Mayor did not cancel the voting, " + toBeHanged.getUserName() + " has been hanged.");
                this.game.killAPlayer(toBeHanged);
            }
            this.pauseTheThread(3);
            new Sender().sendToAll(MessageType.info, "The day is over and now it's time to sleep.");
            this.pauseTheThread(3);
            this.game.setSilencedPlayer(null);
        }else
            this.endTheGame();
    }

    /**
     * This method processes the votes each player has taken.
     * @return player that has been selected by vote.
     */
    private Player calculateTheVotes(){
        this.votes.clear();
        for (Player p : this.game.getAlivePlayers())
            votes.put(p,new AtomicInteger(0));
        for (Player p : this.game.getAlivePlayers()){
            if (p.getLastNightAction().getSubject1() != null)
                votes.get(p.getLastNightAction().getSubject1()).getAndIncrement();
        }
        int higherVote = 0;
        Player toBeHanged = null;
        for (Player p : votes.keySet()){
            if (votes.get(p).get() >= higherVote){
                toBeHanged = p;
                higherVote = votes.get(p).get();
            }
        }
        return toBeHanged;
    }

    /**
     * This method ends the game and announces the winner team!
     */
    private void endTheGame(){
        StringBuilder msg = new StringBuilder();
        if (this.game.getStat() == GameStatus.MafiaWin)
            msg.append("Mafia has won!\n");
        else if (this.game.getStat() == GameStatus.CityWin)
            msg.append("City has won!\n");
        msg.append(this.game.getMafiaAlive()).append(" mafias are alive and ")
                .append(this.game.getCityAlive()).append(" players of city team are alive!")
                .append("Member of the Mafia team :\n");
        for (Player p : this.game.getTeam(Teams.Mafia))
            msg.append(p.getUserName()).append(" :").append(p.getRole());
        new Sender().sendToAll(MessageType.info,msg.toString());
    }

    /**
     * This method checks the number of players of each team to see if the game has ended or not.
     * @return a boolean value indicating the game has finished or not.
     */
    private boolean checkForGameEnd(){
        if (this.game.getMafiaAlive() == 0)
            this.game.setStat(GameStatus.CityWin);
        else{
            if (this.game.getMafiaAlive() >= this.game.getCityAlive())
                this.game.setStat(GameStatus.MafiaWin);
        }
        return this.game.getStat() == GameStatus.MafiaWin || this.game.getStat() == GameStatus.CityWin;
    }

    /**
     * This method waits 30 seconds for the RoleActionDispatchers to complete.
     * If the elapsed time exceeds 30 seconds, haltTheDispatchers() method will be called.
     */
    private void waitForActions(){
        int elapsedTime = 0;
        try {
            while (!this.dispatchers.isEmpty()){
                synchronized (lock){
                    lock.wait(500);
                    elapsedTime += 500;
                }
                if (elapsedTime >= 30000){
                    this.haltTheDispatchers();
                    break;
                }
            }
        }catch (Exception exception){
            Logger.log(exception);
        }
    }

    /**
     * This method force stops all the RoleActionDispatcher threads that are active.
     */
    private void haltTheDispatchers(){
        for (int i = 0 ; i < this.dispatchers.size() ; i++)
            this.dispatchers.remove(i).interrupt();
    }

    /**
     * This method pauses the main game thread.
     * @param seconds : Time in seconds to pause the thread.
     */
    private void pauseTheThread(int seconds){
        try {
            synchronized (lock){
                lock.wait(seconds * 1000L);
            }
        }catch (Exception exception){
            Logger.log(exception);
        }
    }

    /**
     * This method checks all the alive players for inactivity.
     * If they have been inactive for mor than 3 rounds, they will be kicked from the server.
     */
    private void checkForInactivePlayers(){
        for (Player p : this.game.getAlivePlayers())
            if (p.getIdleActions() >= 6)
                this.game.kickAPLayer(p);
    }

    private class RoleActionDispatcher extends Thread{

        private final RoleAction act;
        private final Roles role;

        /**
         * Constructor for class RoleActionDispatcher.
         * RoleAction dispatcher handles the action of each player.
         * This class extends Thread class.
         * @param act : The RoleAction to be dispatched to client.
         * @param role : The role of the player.
         */
        public RoleActionDispatcher(RoleAction act,Roles role){
            this.act = act;
            this.role = role;
            this.start();
        }

        @Override
        public void run(){
            if (this.role == Roles.mayor){
                if (!mayorActionTime)
                    this.act.startDayVoting();
                else
                    this.act.startTheAct();
                mayorActionTime = false;
            }else{
                if (game.getStat() == GameStatus.MafiaNight || game.getStat() == GameStatus.CityNight)
                    this.act.startTheAct();
                else
                    this.act.startDayVoting();
            }
            dispatchers.remove(this);
        }
    }
}
