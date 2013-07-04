package BlackUnicornKiller.Nodes.CombatHandlers;


import BlackUnicornKiller.BlackUnicornKiller;
import BlackUnicornKiller.Nodes.Globals;
import org.powerbot.core.script.job.Task;
import org.powerbot.core.script.job.state.Node;
import org.powerbot.game.api.methods.Calculations;
import org.powerbot.game.api.methods.Tabs;
import org.powerbot.game.api.methods.Walking;
import org.powerbot.game.api.methods.Widgets;
import org.powerbot.game.api.methods.input.Mouse;
import org.powerbot.game.api.methods.interactive.Players;
import org.powerbot.game.api.methods.node.GroundItems;
import org.powerbot.game.api.methods.tab.Inventory;
import org.powerbot.game.api.methods.widget.Camera;
import org.powerbot.game.api.util.Random;
import org.powerbot.game.api.util.Timer;
import org.powerbot.game.api.wrappers.Entity;
import org.powerbot.game.api.wrappers.Tile;
import org.powerbot.game.api.wrappers.node.GroundItem;

import java.awt.*;
public class LootHandler extends Node {

    Rectangle screen = new Rectangle(1,55,518,258);
    Point clickPoint = new Point();

    public void altCameraTurnTo(Entity e){
        do{
            Camera.setAngle(Camera.getYaw() + Random.nextInt(35, 55));
        }while(!altIsOnScreen(e));
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

    public boolean altInteract(Entity e, String cmd, String sub){
        if(e != null){
            if(altIsOnScreen(e)){
                for(Polygon p : e.getBounds()){
                    if(screen.contains(p.getBounds())){
                        clickPoint.setLocation(p.getBounds().x,p.getBounds().y);
                    }
                }
                if(Widgets.get(548, 436).getChild(0) != null && Widgets.get(548, 436).getChild(0) != null){
                    e.hover();
                    if(Widgets.get(548, 436).getChild(0).getText().contains(cmd) && Widgets.get(548, 436).getChild(0).getText().contains(sub)){
                        clickPoint.setLocation(e.getCentralPoint());
                        Task.sleep(50,150);
                        Mouse.click(clickPoint, true);
                        Task.sleep(250,500);
                    }else{
                        if(e.interact(cmd)){return true;}
                    }
                }

            }
            Timer timeCheck = new Timer(3000);
            do{
                Task.sleep(250,350);
                BlackUnicornKiller.status = ("Sleeping after click.");
                if(Players.getLocal().getInteracting() != null && Players.getLocal() != null){
                    if(Players.getLocal().getInteracting().getHealthPercent()<=0 || Players.getLocal().isIdle()){
                        return true;
                    }
                } else if (Players.getLocal().getInteracting() == null){
                    return false;
                }

            }while(timeCheck.isRunning() && Players.getLocal().getInteracting().equals(e) ||
                    Players.getLocal().isMoving() || Players.getLocal().isInCombat());
        }
        return false;
    }

    public boolean altIsOnScreen(Entity e){
        for(final Polygon p : e.getBounds()){
            if(screen.contains(p.getBounds())){
                return true;
            }
        }
        return false;
    }

    public void boneClearer(){
        if(Inventory.contains(Globals.ID_ITMES_BONES)){
            Inventory.getItem(Globals.ID_ITMES_BONES).getWidgetChild().interact("Drop");
        }
    }

    @Override
    public boolean activate(){
        if(!Tabs.getCurrent().equals(Tabs.INVENTORY)){
            Tabs.INVENTORY.open();
        }
        boneClearer();
        Globals.emergencyTeleport();

        GroundItem Loot = GroundItems.getNearest(Globals.ID_ITEMS_HORN);
        if(Inventory.isFull() && Inventory.contains(Globals.ID_ITEMS_LOBSTER)){
            Inventory.getItem(Globals.ID_ITEMS_LOBSTER).getWidgetChild().interact("Eat");
        }
        return(Loot != null && !Inventory.isFull());
    }

    @Override
    public void execute(){
        while(activate()){
            Globals.emergencyTeleport();
            GroundItem Loot = GroundItems.getNearest(Globals.ID_ITEMS_HORN);
            if(Loot != null){
                if(!altIsOnScreen(Loot)){
                    BlackUnicornKiller.status = "Walking towards Loot";
                    Walking.walk(Loot);
                    BlackUnicornKiller.status = "Grabbing Loot.";
                    Camera.turnTo(Loot);
                    if(altInteract(Loot, "Take", Loot.getGroundItem().getName())){
                        invChangeSleep();
                    }

                    while(Players.getLocal().isMoving()){
                        sleep(50,100);
                    }
                } else {
                    if(Calculations.distanceTo(Loot)>=2){
                        BlackUnicornKiller.status = "Turning Camera to Loot.";
                        altCameraTurnTo(Loot);
                    }
                    BlackUnicornKiller.status = "Grabbing Loot.";
                    if(altInteract(Loot, "Take", Loot.getGroundItem().getName())){
                        invChangeSleep();
                    }
                    while(Players.getLocal().isMoving()){
                        sleep(50,100);
                    }
                }
            }
            BlackUnicornKiller.actualProfit= BlackUnicornKiller.actualProfit + (Globals.HornPrice);

        }
    }


}