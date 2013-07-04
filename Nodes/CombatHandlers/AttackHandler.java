package BlackUnicornKiller.Nodes.CombatHandlers;

import BlackUnicornKiller.BlackUnicornKiller;
import BlackUnicornKiller.Nodes.Globals;
import BlackUnicornKiller.Nodes.WalkingHandlers.PaceUnicornsHandler;

import org.powerbot.core.script.job.Task;
import org.powerbot.core.script.job.state.Node;
import org.powerbot.game.api.methods.Calculations;
import org.powerbot.game.api.methods.Walking;
import org.powerbot.game.api.methods.Widgets;
import org.powerbot.game.api.methods.input.Mouse;
import org.powerbot.game.api.methods.interactive.NPCs;
import org.powerbot.game.api.methods.interactive.Players;
import org.powerbot.game.api.methods.tab.Inventory;
import org.powerbot.game.api.methods.widget.Camera;
import org.powerbot.game.api.util.Random;
import org.powerbot.game.api.util.Timer;
import org.powerbot.game.api.wrappers.Entity;
import org.powerbot.game.api.wrappers.interactive.NPC;
import org.powerbot.game.api.wrappers.widget.WidgetChild;

import java.awt.*;

public class AttackHandler extends Node {

    FoodHandler eating = new FoodHandler();
    PaceUnicornsHandler paceUnicorns = new PaceUnicornsHandler();

    Rectangle screen = new Rectangle(1,55,518,258);
    Point clickPoint = new Point();

    public NPC theUnicorn;
    public WidgetChild upText = Widgets.get(548, 436).getChild(0);


    public void altCameraTurnTo(Entity e){
        int x = 0;
        do{
            Camera.setAngle(Camera.getYaw() + Random.nextInt(35, 55));
            x++;
            if(x>=15 || e == null){
                break;
            }
        }while(!altIsOnScreen(e));
    }

    public boolean altInteract(Entity e, String cmd, String sub){
        if(e != null){
            if(!altIsOnScreen(e)){
                for(Polygon p : e.getBounds()){
                    if(screen.contains(p.getBounds())){
                        clickPoint.setLocation(p.getBounds().x,p.getBounds().y);
                    }
                }
                e.hover();
                Task.sleep(25,50);
                if(upText != null && upText.visible()){
                    if(upText.getText().contains(cmd) && upText.getText().contains(sub)){
                        Mouse.click(clickPoint, true);
                        Task.sleep(250,500);
                    }else{
                        e.interact(cmd);
                    }
                }

            }else{
                e.interact(cmd);
            }

            Timer timeCheck = new Timer(3000);
            do{
                Task.sleep(250,350);
                BlackUnicornKiller.status = ("Sleeping after click.");
                if(Globals.interacting != null && Globals.me != null){
                    if(Globals.interacting.getHealthPercent()<=0 || Globals.me.isIdle()){
                        return true;
                    }
                } else if (Globals.interacting == null){
                    return false;
                }

            }while(timeCheck.isRunning() && Globals.interacting.equals(e) ||
                    Globals.me.isMoving() || Globals.me.isInCombat());
        }
        return false;
    }

    public boolean altIsOnScreen(Entity e){

        Integer numOfPoints;
        for(final Polygon p : e.getBounds()){
            numOfPoints = p.xpoints.length + p.ypoints.length;
            if(screen.contains(p.getBounds()) && numOfPoints>=4){
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean activate(){
        Globals.emergencyTeleport();

        theUnicorn = NPCs.getNearest(Globals.ID_NPCS_UNICORNS);
        Globals.interacting = Players.getLocal().getInteracting();

        if(theUnicorn == null){
            if(paceUnicorns.activate()){
                paceUnicorns.execute();
            }
        }

        return(theUnicorn != null && Globals.interacting==null && theUnicorn.getHealthPercent()!=0
                && Calculations.distanceTo(theUnicorn)<=15 && !Inventory.isFull());
    }

    @Override
    public void execute(){
        BlackUnicornKiller.status="Attacking Unicorn.";

        ActionBarHandler.momentumCheck();

        if(theUnicorn != null){
            if(Calculations.distanceTo(theUnicorn)>=4){
                if(!altIsOnScreen(theUnicorn)){
                    altCameraTurnTo(theUnicorn);
                }
            }
            if(!altIsOnScreen(theUnicorn)){
                altCameraTurnTo(theUnicorn);
            }
            if(Calculations.distanceTo(theUnicorn)>=8){
                Walking.walk(theUnicorn);
            }
            if(!altInteract(theUnicorn, "Attack", "Unicorn")){
                Walking.walk(theUnicorn);
                altInteract(theUnicorn, "Attack", "Unicorn");
            }
            altCameraTurnTo(theUnicorn);
            if(!Globals.me.isInCombat()){
                theUnicorn.interact("Attack");
            }
        }

        if(eating.activate()){
            eating.execute();
        }

        theUnicorn = null;
    }

}
