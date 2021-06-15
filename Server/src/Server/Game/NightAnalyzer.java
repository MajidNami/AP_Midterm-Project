package Server.Game;

import Server.RolesActions.Action;
import Utils.Enums.Event;
import Utils.Enums.Roles;
import Utils.Enums.Teams;
import Utils.GameData;
import Utils.Models.Player;
import java.util.ArrayList;
import java.util.HashMap;

public class NightAnalyzer {

    private final StringBuilder broadcast = new StringBuilder("Night events :\n");
    private final ArrayList<Player> deaths = new ArrayList<>();
    private final HashMap<Event, Action> events;
    private Player silencedPlayer = null;
    private boolean dieHardInquiry = false;
    private final Game game;

    /**
     * The NightAnalyzer class is responsible for analyzing the night events.
     * @param game : The game object.
     */
    public NightAnalyzer(Game game){
        this.game = game;
        this.events = GameData.getInstance().getNightEvents();
    }

    /**
     * Starts the analyzing algorithm and returns the result in a client friendly String.
     * @return This NightAnalyzer object.
     */
    public NightAnalyzer startAnalyzing(){
        Player mafiaShot = events.get(Event.MafiaShot).getSubject1();
        Player doctorSave = events.get(Event.CitySave).getSubject1();
        Player sniperShot = events.get(Event.CityShot).getSubject1();
        Player mafiaSave = events.get(Event.MafiaSave).getSubject1();
        if (mafiaShot != null) {
            if (!mafiaShot.getUserName().equals(doctorSave.getUserName()) && !mafiaShot.getUserName().equals(mafiaSave.getUserName())) {
                if (mafiaShot.getRole() == Roles.dieHard){
                    if (this.game.getDieHardTakenShots() >= 1){
                        deaths.add(mafiaShot);
                        this.game.killAPlayer(mafiaShot);
                    }else
                        this.game.addDieHardTakenSHot();
                }else{
                    deaths.add(mafiaShot);
                    this.game.killAPlayer(mafiaShot);
                }
            }
        }
        if (sniperShot != null){
            if (this.game.isMafia(sniperShot)){
                if (!sniperShot.getUserName().equals(mafiaSave.getUserName()) && !sniperShot.getUserName().equals(doctorSave.getUserName())){
                    deaths.add(sniperShot);
                    this.game.killAPlayer(sniperShot);
                }
            }else{
                Player sniper = this.game.getPlayer(Roles.sniper,Teams.City);
                deaths.add(sniper);
                this.game.killAPlayer(sniper);
            }
        }
        Action therapistAction = events.get(Event.Therapist);
        if (therapistAction != null) {
            this.silencedPlayer = therapistAction.getSubject1();
            this.game.setSilencedPlayer(this.silencedPlayer);
        }
        Action dieHardAction = events.get(Event.DieHardInquiry);
        if (dieHardAction != null)
            this.dieHardInquiry = dieHardAction.getResult();
        this.turnTheResultIntoString();
        return this;
    }

    /**
     * Turns the result into
     */
    private void turnTheResultIntoString(){
        this.broadcast.append(this.deaths.size()).append(" players were killed last night\n");
        if (!this.deaths.isEmpty()){
            for (Player p : this.deaths)
                this.broadcast.append(p.getUserName()).append('\n');
        }
        if (this.silencedPlayer != null)
            this.broadcast.append(silencedPlayer.getUserName()).append(" was silenced by the therapist.\n");
        if (this.dieHardInquiry){
            this.broadcast.append("Die hard asked for inquiry. The following roles have been killed and are out of the game :\n");
            for (Player p : this.game.getDeadPlayers())
                this.broadcast.append(p.getRole()).append('\n');
        }
    }

    /**
     * @return An string containing the results.
     */
    public String getResult(){
        return this.broadcast.toString();
    }
}
