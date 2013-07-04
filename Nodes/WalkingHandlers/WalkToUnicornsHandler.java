package BlackUnicornKiller.Nodes.WalkingHandlers;

import BlackUnicornKiller.BlackUnicornKiller;
import BlackUnicornKiller.Nodes.Globals;
import org.powerbot.core.script.job.state.Node;
import org.powerbot.game.api.methods.Calculations;
import org.powerbot.game.api.methods.Walking;
import org.powerbot.game.api.methods.tab.Inventory;
import org.powerbot.game.api.wrappers.Tile;

public class WalkToUnicornsHandler extends Node {

    private Tile distanceToUnicorns;

    public static final Tile[] PATH_TO_UNICORNS = new Tile[] {new Tile(3133,3633,0), new Tile(3126,3627,0)};
    public static final Tile[] PATH_TO_UNICORNS_REVERSE = new Tile[] {new Tile(3126,3627,0), new Tile(3133,3633,0), Globals.TILE_LOAD_WILDERNESS};

    @Override
    public boolean activate(){
        Globals.emergencyTeleport();
        for(int i=0; i<=Globals.unicornPacePath.length-1; i++){
            distanceToUnicorns = Globals.unicornPacePath[i];
            if(Calculations.distanceTo(distanceToUnicorns)<=4){
                break;
            }
        }
        return (Calculations.distanceTo(distanceToUnicorns)>=4 && Calculations.distanceTo(Globals.TILE_LOAD_WILDERNESS)<=12
                && !Inventory.isFull());
    }

    @Override
    public void execute(){
        BlackUnicornKiller.status = "Walking to Unicorns.";
        Walking.newTilePath(PATH_TO_UNICORNS).traverse();
    }

}
