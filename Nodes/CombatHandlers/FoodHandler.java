package BlackUnicornKiller.Nodes.CombatHandlers;
import BlackUnicornKiller.BlackUnicornKiller;

import BlackUnicornKiller.Nodes.Globals;
import org.powerbot.core.script.job.state.Node;
import org.powerbot.game.api.methods.Tabs;
import org.powerbot.game.api.methods.Widgets;
import org.powerbot.game.api.methods.interactive.Players;
import org.powerbot.game.api.methods.tab.Inventory;

public class FoodHandler extends Node {

    public int getHpPercent() {
        return Math.abs(100 - 100 * Widgets.get(748, 5).getHeight() / 28);
    }

    public void emergencyEat(){
        if(getHpPercent()<=20){
            execute();
        }
    }

    @Override
    public boolean activate(){
        if(!Tabs.getCurrent().equals(Tabs.INVENTORY)){
            Tabs.INVENTORY.open();
        }
        Globals.emergencyTeleport();
        return (Players.getLocal().validate() && getHpPercent()<=50
                && Inventory.getCount(Globals.ID_ITEMS_LOBSTER)>=1);
    }

    @Override
    public void execute(){
        if(Inventory.getItem(Globals.ID_ITEMS_LOBSTER).getWidgetChild() != null){
            BlackUnicornKiller.status = "Eating food.";
            Inventory.getItem(Globals.ID_ITEMS_LOBSTER).getWidgetChild().interact("Eat");
        }
    }
}
