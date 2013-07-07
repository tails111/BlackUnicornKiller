package BlackUnicornKiller.Nodes.CombatHandlers;

import BlackUnicornKiller.BlackUnicornKiller;
import BlackUnicornKiller.Nodes.Globals;
import BlackUnicornKiller.Nodes.WalkingHandlers.PaceUnicornsHandler;

import org.powerbot.core.script.job.state.Node;
import org.powerbot.game.api.methods.Calculations;
import org.powerbot.game.api.methods.Walking;
import org.powerbot.game.api.methods.interactive.NPCs;
import org.powerbot.game.api.methods.interactive.Players;
import org.powerbot.game.api.methods.tab.Inventory;
import org.powerbot.game.api.methods.widget.Camera;
import org.powerbot.game.api.util.Random;
import org.powerbot.game.api.wrappers.Entity;
import org.powerbot.game.api.wrappers.interactive.*;
import org.powerbot.game.api.wrappers.interactive.Character;

import java.awt.*;

public class AttackHandler extends Node {

    FoodHandler eating = new FoodHandler();
    PaceUnicornsHandler paceUnicorns = new PaceUnicornsHandler();

    Rectangle screen = new Rectangle(1,55,518,258);

    NPC theUnicorn;
    Character interacting;
    Character me;

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
        interacting = Players.getLocal().getInteracting();
        me = Players.getLocal();
        if(theUnicorn == null){
            if(paceUnicorns.activate()){
                paceUnicorns.execute();
            }
        }
        return(theUnicorn != null && interacting==null && theUnicorn.getHealthPercent()!=0
                && Calculations.distanceTo(theUnicorn)<=15 && !Inventory.isFull());
    }

    @Override
    public void execute(){
        System.out.println("Attack handler");
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
            if(!theUnicorn.interact("Attack")){
                Walking.walk(theUnicorn);
                theUnicorn.interact("Attack");
            }
            altCameraTurnTo(theUnicorn);
            if(!me.isInCombat()){
                theUnicorn.interact("Attack");
            }


        }

        if(eating.activate()){
            eating.execute();
        }

        theUnicorn = null;
    }

}
