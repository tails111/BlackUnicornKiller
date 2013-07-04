package BlackUnicornKiller.Nodes.WalkingHandlers;

import BlackUnicornKiller.BlackUnicornKiller;
import BlackUnicornKiller.Nodes.Globals;
import org.powerbot.core.script.job.Task;
import org.powerbot.core.script.job.state.Node;
import org.powerbot.game.api.methods.Calculations;
import org.powerbot.game.api.methods.Widgets;
import org.powerbot.game.api.methods.interactive.Players;
import org.powerbot.game.api.methods.tab.Inventory;
import org.powerbot.game.api.util.Timer;

public class TeleportToUnicornsHandler  extends Node {

    private void sleepGameTick(){
        Task.sleep(650,850);
    }


    @Override
    public boolean activate(){
         return (Calculations.distanceTo(Globals.TILE_LOAD_WILDERNESS)>=10 && PaceUnicornsHandler.distanceToUnicorns()<=6
         && !Inventory.contains(Globals.ID_ITEMS_HORN) && Inventory.contains(Globals.ID_ITEMS_LOBSTER)
         && Globals.interacting == null);
    }

    @Override
    public void execute(){
        BlackUnicornKiller.status = "Homeporting to Wilderness.";

        //Wilderness load click  to warning
        if(Widgets.get(1092).getChild(0).visible()){
            Widgets.get(1092).getChild(59).click(true);
            Timer timeCheck = new Timer(2000);
            do{
                Task.sleep(30,50);
            }while(timeCheck.isRunning() && !Widgets.get(1186,7).visible());
        }
        sleepGameTick();

        //Wilderness Warning to Teleport Question
        if(Widgets.get(1186,7) != null && Widgets.get(1186,7).visible()){
             Widgets.get(1186, 7).click(true);
             Timer timeCheck2 = new Timer(2000);
             do{
                 Task.sleep(30,50);
             }while(timeCheck2.isRunning() && !Widgets.get(1188,3).visible());
        }
        sleepGameTick();

        //Yes to Wilderness
        if(Widgets.get(1188,3) != null && Widgets.get(1188,3).visible()){
            if(Widgets.get(1188,3).getText().contains("Yes")){
                Widgets.get(1188,3).click(true);
            }
            Timer timeCheck3 = new Timer(20000);
            do{
                Task.sleep(30,50);
            }while(timeCheck3.isRunning() && Calculations.distanceTo(Globals.TILE_LOAD_WILDERNESS)>=5);
        }
        sleepGameTick();

        //Action bar click to LodeStone wait.
        if(!Widgets.get(1092).getChild(0).visible() && !Widgets.get(1188,3).visible() && !Widgets.get(1186,7).visible()
                && !Widgets.get(1092,0).visible() && Players.getLocal().isIdle()){
            Widgets.get(640).getChild(114).click(true);   //Action bar click
            Timer timeCheck = new Timer(5000);
            do{
                Task.sleep(20,50);
            }while(timeCheck.isRunning() && !Widgets.get(1092,0).visible()); //Lodestone Network Screen
        }

    }
}