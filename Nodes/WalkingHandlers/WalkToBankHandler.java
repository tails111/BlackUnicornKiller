package BlackUnicornKiller.Nodes.WalkingHandlers;

import BlackUnicornKiller.BlackUnicornKiller;
import BlackUnicornKiller.Nodes.Globals;
import org.powerbot.core.script.job.state.Node;
import org.powerbot.game.api.methods.Calculations;
import org.powerbot.game.api.methods.Walking;
import org.powerbot.game.api.methods.interactive.Players;
import org.powerbot.game.api.methods.tab.Inventory;
import org.powerbot.game.api.wrappers.Tile;

public class WalkToBankHandler extends Node {

    private final Tile[] PATH_BANK_TO = new Tile[] {
            new Tile(3074,3502,0), new Tile(3081, 3501, 0), new Tile(3087, 3498, 0), new Tile(3093, 3494, 0)};
    private final Tile TILE_BANK = new Tile(3093,3494,0);
    private final Tile TILE_TELEPORT_AFTER = new Tile(3067,3505,0);

    @Override
    public boolean activate(){
        return (Calculations.distanceTo(TILE_TELEPORT_AFTER)<=45) && Inventory.getCount(Globals.ID_ITEMS_HORN)>=27 &&
                (Players.getLocal().getAnimation()==-1 && Calculations.distanceTo(TILE_BANK)>=5);
    }

    @Override
    public void execute(){
        BlackUnicornKiller.status = "Walking to Bank.";
        Walking.newTilePath(PATH_BANK_TO).traverse();
    }
}