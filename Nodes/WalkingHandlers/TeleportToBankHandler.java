package BlackUnicornKiller.Nodes.WalkingHandlers;

import BlackUnicornKiller.BlackUnicornKiller;
import BlackUnicornKiller.Nodes.Globals;
import org.powerbot.core.script.job.Task;
import org.powerbot.core.script.job.state.Node;
import org.powerbot.game.api.methods.Calculations;
import org.powerbot.game.api.methods.Walking;
import org.powerbot.game.api.methods.Widgets;
import org.powerbot.game.api.methods.interactive.Players;
import org.powerbot.game.api.methods.tab.Inventory;
import org.powerbot.game.api.util.Timer;
import org.powerbot.game.api.wrappers.Area;
import org.powerbot.game.api.wrappers.Tile;
import org.powerbot.game.api.wrappers.interactive.Character;

public class TeleportToBankHandler  extends Node {


    final Area edgeville = new Area(new Tile(3063,3509,0), new Tile(3100,3486,0));

    Character me;

    @Override
    public boolean activate(){
        me = Players.getLocal();

        Globals.emergencyTeleport();
        return !edgeville.contains(Globals.me.getLocation()) &&
                me.getAnimation() == -1 && Inventory.getCount(Globals.ID_ITEMS_HORN)>=27;
    }

    @Override
    public void execute(){
        BlackUnicornKiller.status = "Homeporting to Edgeville.";
        Timer timeCheck = new Timer(5000);
        do{
            Walking.walk(Globals.TILE_LOAD_WILDERNESS);
            Timer timeCheck2 = new Timer(2000);
            do{
                Task.sleep(150,275);
            }while(me.isMoving() && timeCheck2.isRunning());
        }while(timeCheck.isRunning() && Calculations.distanceTo(Globals.TILE_LOAD_WILDERNESS)>=2);

        Walking.walk(Globals.TILE_LOAD_WILDERNESS);

        if(Calculations.distanceTo(Globals.TILE_LOAD_WILDERNESS)<=14 && !Globals.me.isInCombat()){
            if(!Widgets.get(1092).getChild(0).visible()){
                Widgets.get(640).getChild(113).click(true);
                Task.sleep(750,1250);
            }
            if(Widgets.get(1092).getChild(0).visible()){
                Widgets.get(1092).getChild(45).click(true);
                Timer timeCheck3 = new Timer(15000);
                do{
                    Task.sleep(1000);
                }while(Calculations.distanceTo(Globals.TILE_LOAD_EDGEVILLE)>=5 && timeCheck3.isRunning());
            }
        }
    }
}
