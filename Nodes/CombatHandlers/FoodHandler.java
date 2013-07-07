package BlackUnicornKiller.Nodes.CombatHandlers;
import BlackUnicornKiller.BlackUnicornKiller;

import BlackUnicornKiller.Nodes.Globals;
import org.powerbot.core.script.job.state.Node;
import org.powerbot.game.api.methods.Tabs;
import org.powerbot.game.api.methods.Widgets;
import org.powerbot.game.api.methods.interactive.Players;
import org.powerbot.game.api.methods.tab.Inventory;
import org.powerbot.game.api.wrappers.interactive.Character;
import org.powerbot.game.api.wrappers.widget.WidgetChild;

public class FoodHandler extends Node {

    WidgetChild food;
    Character me;

    public int getHpPercent() {
        return Math.abs(100 - 100 * Widgets.get(748, 5).getHeight() / 28);
    }

    @Override
    public boolean activate(){
        me= Players.getLocal();

        if(Inventory.getItem(Globals.ID_ITEMS_LOBSTER) != null){
            food = Inventory.getItem(Globals.ID_ITEMS_LOBSTER).getWidgetChild();
        }

        if(!Tabs.getCurrent().equals(Tabs.INVENTORY)){
            Tabs.INVENTORY.open();
        }
        Globals.emergencyTeleport();
        return (me.validate() && getHpPercent()<=50
                && Inventory.getCount(Globals.ID_ITEMS_LOBSTER)>=1);
    }

    @Override
    public void execute(){
        if(food != null){
            BlackUnicornKiller.status = "Eating food.";
            food.interact("Eat");
        }
    }
}
