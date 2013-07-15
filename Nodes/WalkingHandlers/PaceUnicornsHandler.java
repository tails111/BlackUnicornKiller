package BlackUnicornKiller.Nodes.WalkingHandlers;

import BlackUnicornKiller.BlackUnicornKiller;
import BlackUnicornKiller.Nodes.Globals;
import org.powerbot.core.script.job.state.Node;
import org.powerbot.game.api.methods.Calculations;
import org.powerbot.game.api.methods.Walking;
import org.powerbot.game.api.methods.interactive.Players;
import org.powerbot.game.api.methods.node.GroundItems;
import org.powerbot.game.api.methods.tab.Inventory;
import org.powerbot.game.api.wrappers.Tile;
import org.powerbot.game.api.wrappers.interactive.Character;

public class PaceUnicornsHandler extends Node {

    public static Tile[] unicornPacePath = new Tile[] {
            new Tile(3116,3601,0), new Tile(3119,3610,0), new Tile(3123,3619,0), new Tile(3122,3629,0),
            new Tile(3115,3638,0), new Tile(3100,3638,0), new Tile(3089,3634,0), new Tile(3079,3632,0),
            new Tile(3075,3622,0), new Tile(3075,3633,0)};

    public static Tile[] unicornPacePathReverse = new Tile[] {
            new Tile(3075,3633,0), new Tile(3075,3622,0), new Tile(3079,3632,0), new Tile(3089,3634,0),
            new Tile(3100,3638,0), new Tile(3115,3638,0), new Tile(3122,3629,0), new Tile(3123,3619,0),
            new Tile(3119,3610,0), new Tile(3116,3601,0)};

    public static Tile endTile = new Tile(3075,3633,0);
    public static Tile startTile = new Tile(3116,3601,0);

    public static Tile distanceToUnicornsTile;

    public static char placement = 'B';

    public static double distanceToUnicorns(){
        for(int i=0; i<=Globals.unicornPacePath.length-1; i++){
            distanceToUnicornsTile = Globals.unicornPacePath[i];
            if(Calculations.distanceTo(distanceToUnicornsTile)<=8){
                return(Calculations.distanceTo(distanceToUnicornsTile));
            }
        }
        return 0;
    }

    Character interacting;
    Character me;

    @Override
    public boolean activate(){
        me = Players.getLocal();
        Globals.emergencyTeleport();
        return(!Inventory.isFull() && me.getHealthPercent()>=25 && distanceToUnicorns()<=10
                && interacting == null && GroundItems.getNearest(Globals.ID_ITEMS_HORN) == null
                && Inventory.contains(Globals.ID_ITEMS_FALLYTAB));
    }

    @Override
    public void execute(){
        BlackUnicornKiller.status="Pacing to find Unicorns.";

        if(Calculations.distanceTo(endTile)<=6){
            placement = 'E';
        }
        if(Calculations.distanceTo(startTile)<=6){
            placement = 'B';
        }

        if(placement == 'B'){
            Walking.newTilePath(unicornPacePath).traverse();
        }

        if(placement == 'E'){
            Walking.newTilePath(unicornPacePathReverse).traverse();
        }



    }

}
