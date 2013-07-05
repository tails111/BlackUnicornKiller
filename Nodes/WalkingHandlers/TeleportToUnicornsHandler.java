package BlackUnicornKiller.Nodes.WalkingHandlers;

import BlackUnicornKiller.BlackUnicornKiller;
import BlackUnicornKiller.Nodes.Globals;
import org.powerbot.core.script.job.Task;
import org.powerbot.core.script.job.state.Node;
import org.powerbot.game.api.methods.Calculations;
import org.powerbot.game.api.methods.Widgets;
import org.powerbot.game.api.methods.interactive.Players;
import org.powerbot.game.api.methods.node.GroundItems;
import org.powerbot.game.api.methods.tab.Inventory;
import org.powerbot.game.api.util.Timer;
import org.powerbot.game.api.wrappers.interactive.*;
import org.powerbot.game.api.wrappers.interactive.Character;
import org.powerbot.game.api.wrappers.widget.WidgetChild;

public class TeleportToUnicornsHandler  extends Node {

    private void sleepGameTick(){
        Task.sleep(650,850);
    }

    Character interacting;
    Character me;
    WidgetChild wildernessLode;
    WidgetChild wildernessLodeClick;
    WidgetChild wildernessWarning;
    WidgetChild wildernessChat;
    WidgetChild actionBar;
    WidgetChild actionBarSpell;

    @Override
    public boolean activate(){

        me = Players.getLocal();
        wildernessLode = Widgets.get(1092).getChild(0);
        wildernessLodeClick = Widgets.get(1092).getChild(59);
        wildernessWarning = Widgets.get(1186,7);
        wildernessChat = Widgets.get(1188,3);
        actionBar = Widgets.get(1092).getChild(0);
        actionBarSpell = Widgets.get(640).getChild(114);

         return (Calculations.distanceTo(Globals.TILE_LOAD_WILDERNESS)>=10 && PaceUnicornsHandler.distanceToUnicorns()<=6
         && !Inventory.contains(Globals.ID_ITEMS_HORN) && Inventory.contains(Globals.ID_ITEMS_LOBSTER)
         && interacting == null && GroundItems.getNearest(Globals.ID_ITEMS_HORN)==null);
    }

    @Override
    public void execute(){
        BlackUnicornKiller.status = "Homeporting to Wilderness.";

        //Wilderness load click  to warning
        if(wildernessLode.visible()){
            wildernessLodeClick.click(true);
            Timer timeCheck = new Timer(2000);
            do{
                Task.sleep(30,50);
            }while(timeCheck.isRunning() && !Widgets.get(1186,7).visible());
        }
        sleepGameTick();

        //Wilderness Warning to Teleport Question
        if(wildernessWarning != null && wildernessWarning.visible()){
            wildernessWarning.click(true);
             Timer timeCheck2 = new Timer(2000);
             do{
                 Task.sleep(30,50);
             }while(timeCheck2.isRunning() && !wildernessChat.visible());
        }
        sleepGameTick();

        //Yes to Wilderness
        if(wildernessChat != null && wildernessChat.visible()){
            if(wildernessChat.getText().contains("Yes")){
                wildernessChat.click(true);
            }
            Timer timeCheck3 = new Timer(20000);
            do{
                Task.sleep(30,50);
            }while(timeCheck3.isRunning() && Calculations.distanceTo(Globals.TILE_LOAD_WILDERNESS)>=5);
        }
        sleepGameTick();

        //Action bar click to LodeStone wait.
        if(!actionBar.visible() && !wildernessChat.visible() && !wildernessWarning.visible()
                && !actionBar.visible() && Players.getLocal().isIdle()){
            actionBarSpell.click(true);   //Action bar click
            Timer timeCheck = new Timer(5000);
            do{
                Task.sleep(20,50);
            }while(timeCheck.isRunning() && !actionBar.visible()); //Lodestone Network Screen
        }

    }
}