package Server;

import Utils.ConfigsAndData;
import Utils.Enums.Roles;
import Utils.GameData;

public class Starter {

    /**
     * Starts the pre launch sequence.
     */
    public void start(){
        this.determineRoles();
        GameData.getInstance().initializeTheGame();
    }

    /**
     * Determines which roles will be present in this match.
     */
    private void determineRoles(){
        int mafiaTeam = ((ConfigsAndData.getInstance().getPlayersNumber() / 3));
        this.determineCityRoles(ConfigsAndData.getInstance().getPlayersNumber() - mafiaTeam);
        this.determineMafiaRoles(mafiaTeam);
    }

    /**
     * Determines the Mafia roles present in this match.
     */
    private void determineMafiaRoles(int mafiaTeam){
        GameData.getInstance().addARole(Roles.godFather);
        GameData.getInstance().addARole(Roles.doctorLecter);
        for (int i = 0 ; i < mafiaTeam - 2 ; i++)
            GameData.getInstance().addARole(Roles.simpleMafia);
    }

    /**
     * Determines the City roles present in this match.
     */
    private void determineCityRoles(int cityTeam){
        GameData.getInstance().addARole(Roles.cityDoctor);
        GameData.getInstance().addARole(Roles.inspector);
        GameData.getInstance().addARole(Roles.mayor);
        GameData.getInstance().addARole(Roles.sniper);
        if (cityTeam > 4){
                GameData.getInstance().addARole(Roles.therapist);
            if (cityTeam >= 6)
                GameData.getInstance().addARole(Roles.simpleCitizen);
            if (cityTeam >= 7)
                GameData.getInstance().addARole(Roles.dieHard);
            if (cityTeam >= 8){
                for (int i = 0 ; i < cityTeam - 7; i++)
                    GameData.getInstance().addARole(Roles.simpleCitizen);
            }
        }
    }
}
