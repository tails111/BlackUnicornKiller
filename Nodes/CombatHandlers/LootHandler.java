package BlackUnicornKiller.Nodes.CombatHandlers;

import BlackUnicornKiller.BlackUnicornKiller;
import BlackUnicornKiller.Nodes.Globals;

import org.powerbot.core.script.job.Task;
import org.powerbot.core.script.job.state.Node;
import org.powerbot.game.api.methods.Tabs;
import org.powerbot.game.api.methods.Walking;
import org.powerbot.game.api.methods.interactive.Players;
import org.powerbot.game.api.methods.node.GroundItems;
import org.powerbot.game.api.methods.tab.Inventory;
import org.powerbot.game.api.methods.widget.Camera;
import org.powerbot.game.api.util.Random;
import org.powerbot.game.api.util.Timer;
import org.powerbot.game.api.wrappers.Entity;
import org.powerbot.game.api.wrappers.node.GroundItem;

import java.awt.*;
public class LootHandler extends Node {

    Rectangle screen = new Rectangle(1,55,518,258);
    GroundItem Loot;


    public void altCameraTurnTo(Entity e){
        Timer timeCheck = new Timer(Random.nextInt(2800,3400));
        do{
            Camera.setAngle(Camera.getYaw() + Random.nextInt(35, 55));
            if(e == null){
                break;
            }
        }while(!altIsOnScreen(e) && timeCheck.isRunning());
    }

    public static boolean invChangeSleep(){

        Timer timeCheck = new Timer(Random.nextInt(1200,1600));
        int tempInvCount = Inventory.getCount();
        int newInvCount;
        do {
            BlackUnicornKiller.status="Waiting for Inv Change: " +timeCheck.getRemaining();
            Task.sleep(20,50);
            newInvCount = Inventory.getCount();
            if(timeCheck.getRemaining()<=100){return false;}
        }while(tempInvCount==newInvCount && timeCheck.isRunning());
        return true;
    }

    public boolean altIsOnScreen(Entity e){
        for(final Polygon p : e.getBounds()){
            if(screen.contains(p.getBounds())){
                return true;
            }
        }
        return false;
    }

    public void boneAndCharmClearer(){
        if(Inventory.contains(Globals.ID_ITMES_BONES)){
            Inventory.getItem(Globals.ID_ITMES_BONES).getWidgetChild().interact("Drop");
        }
        for(int i=0; i<=Globals.ID_CHARMS.length-1; i++){
            if(Inventory.contains(Globals.ID_CHARMS[i])){
                Inventory.getItem(Globals.ID_CHARMS[i]).getWidgetChild().interact("Drop");
            }
        }

    }

    @Override
    public boolean activate(){
        Globals.me = Players.getLocal();

        if(!Tabs.getCurrent().equals(Tabs.INVENTORY)){
            Tabs.INVENTORY.open();
        }
        boneAndCharmClearer();
        Globals.emergencyTeleport();

        Loot = GroundItems.getNearest(Globals.ID_ITEMS_HORN);

        if(Inventory.isFull() && Inventory.contains(Globals.ID_ITEMS_LOBSTER)){
            Inventory.getItem(Globals.ID_ITEMS_LOBSTER).getWidgetChild().interact("Eat");
        }
        return(Loot != null && !Inventory.isFull());
    }

    @Override
    public void execute(){
        while(activate()){
            System.out.println("Loot handler");
            Loot = GroundItems.getNearest(Globals.ID_ITEMS_HORN);
            Globals.emergencyTeleport();
            Globals.idleTimeOut();

            if(Loot != null){
                if(!altIsOnScreen(Loot)){
                    BlackUnicornKiller.status = "Walking towards Loot";
                    Walking.walk(Loot);
                    BlackUnicornKiller.status = "Turning Camera to Loot";
                    altCameraTurnTo(Loot);
                    if(Loot.interact("Take")){
                        invChangeSleep();
                    }
                    while(Globals.me.isMoving()){
                        Globals.me = Players.getLocal();
                        sleep(50,100);
                    }
                } else {
                    BlackUnicornKiller.status = "Grabbing Loot.";
                    if(Loot.interact("Take")){
                        invChangeSleep();
                    }
                    while(Globals.me.isMoving()){
                        sleep(50,100);
                    }
                }
            }
            BlackUnicornKiller.actualProfit= BlackUnicornKiller.actualProfit + (Globals.HornPrice);
            BlackUnicornKiller.postedHorns= BlackUnicornKiller.postedHorns + 1;
        }
        Loot = null;

    }


}