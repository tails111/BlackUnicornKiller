package BlackUnicornKiller.Nodes;

import BlackUnicornKiller.Nodes.WalkingHandlers.TeleportToBankHandler;
import org.powerbot.core.script.job.Task;
import org.powerbot.game.api.methods.Calculations;
import org.powerbot.game.api.methods.Widgets;
import org.powerbot.game.api.methods.interactive.Players;
import org.powerbot.game.api.methods.tab.Inventory;
import org.powerbot.game.api.util.Timer;
import org.powerbot.game.api.wrappers.Tile;
import org.powerbot.game.api.wrappers.interactive.Character;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;

public class Globals {
    public static final int ID_ITEMS_FALLYTAB = 8009, ID_ITEMS_HORN = 237, ID_ITMES_BONES = 526;
    public static final int ID_ITEMS_LOBSTER = 379;
    public static final int ID_CHARMS_GREEN = 12159, ID_CHARMS_CRIMSON = 12160, ID_CHARMS_BLUE = 12163, ID_CHARMS_GOLD = 12158;
    public static final int ID_NPCS_UNICORNS = 133;

    public static int ID_WEAPON = 1405;

    public static int HornPrice = getPrices(ID_ITEMS_HORN);

    public static int FoodNum = 1;

    public static final int[] ID_CHARMS = {ID_CHARMS_GOLD, ID_CHARMS_CRIMSON, ID_CHARMS_GREEN, ID_CHARMS_BLUE};


    public static final int[] ID_BANK_BOOTH = {42378,42377,42217};
    public static final int[] ITEMS_REQUIRED = {ID_ITEMS_FALLYTAB, ID_ITEMS_LOBSTER};
    public static final int[] ITEMS_REQUIRED_AMOUNTS =  {1,FoodNum};

    public static final Tile[] unicornPacePath = new Tile[] {
            new Tile(3116,3601,0), new Tile(3119,3610,0), new Tile(3123,3619,0), new Tile(3122,3629,0),
            new Tile(3115,3638,0), new Tile(3100,3638,0), new Tile(3089,3634,0), new Tile(3079,3632,0),
            new Tile(3075,3622,0)};

    public static final Tile TILE_LOAD_WILDERNESS = new Tile(3143,3635,0);
    public static final Tile TILE_LOAD_EDGEVILLE = new Tile(3067,3505,0);

    public static Character me = Players.getLocal();

    public static Timer idleCheck = new Timer(45000);

    public static void idleTimeOut(){
        if(!idleCheck.isRunning()){
            if(new TeleportToBankHandler().activate()){
                new TeleportToBankHandler().execute();
            }
        }
        if(!me.isIdle()){
            idleCheck.reset();
        }
    }

    public static boolean emergencyTeleport(){
        if(me!=null){
            if(me.getHealthPercent()<=70){
                Timer timeCheck = new Timer(2000);
                do{
                    if(Inventory.contains(ID_ITEMS_FALLYTAB)){
                        Inventory.getItem(ID_ITEMS_FALLYTAB).getWidgetChild().interact("Break");
                    }
                }while(me.getHealthPercent()<=70 && Inventory.contains(ID_ITEMS_FALLYTAB) && timeCheck.isRunning());
                if(!Globals.me.isInCombat()){
                    if(!Widgets.get(1092).getChild(0).visible()){
                        Widgets.get(640).getChild(113).click(true);
                        Task.sleep(750, 1250);
                    }
                    if(Widgets.get(1092).getChild(0).visible()){
                        Widgets.get(1092).getChild(45).click(true);
                        Timer timeCheck3 = new Timer(15000);
                        do{
                            Task.sleep(1000);
                        }while(Calculations.distanceTo(Globals.TILE_LOAD_EDGEVILLE)>=5 && timeCheck3.isRunning());
                    }
                }
                return true;
            }
        }
        return false;
    }

    public static int getPrices(final int id) {
        int price = 0;
        String add = "http://scriptwith.us/api/?return=text&item=";
        add += id;
        try {
            final BufferedReader in = new BufferedReader(new InputStreamReader(
                    new URL(add).openConnection().getInputStream()));
            String line = in.readLine();
            if (line == null) {
                line = in.readLine();
            }
            String[] subset = line.split("[:]");
            price = Integer.parseInt(subset[1]);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return price;
    }

}