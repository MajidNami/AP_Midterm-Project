package Server.RolesActions;

import Utils.Models.Player;

public class RoleActionFactory {

    /**
     * Returns the suitable RoleAction class according to the players role.
     * @param pl : The player object.
     * @return A class extending the RoleAction abstract class.
     */
    public RoleAction getAction(Player pl){
        switch (pl.getRole()){
            case doctorLecter:
                return new DoctorLecter(pl);
            case cityDoctor:
                return new CityDoctor(pl);
            case dieHard:
                return new DieHard(pl);
            case mayor:
                return new Mayor(pl);
            case sniper:
                return new Sniper(pl);
            case inspector:
                return new Inspector(pl);
            case therapist:
                return new Therapist(pl);
            case simpleMafia:
                return new SimpleMafia(pl);
            case godFather:
                return new GodFather(pl,new Action[]{});
            case simpleCitizen:
                return new SimpleCitizen(pl);
        }
        return null;
    }
}
