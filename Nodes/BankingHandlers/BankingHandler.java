package BlackUnicornKiller.Nodes.BankingHandlers;

import BlackUnicornKiller.BlackUnicornKiller;
import BlackUnicornKiller.Nodes.Globals;
import org.powerbot.core.script.job.Task;
import org.powerbot.core.script.job.state.Node;
import org.powerbot.game.api.methods.Calculations;
import org.powerbot.game.api.methods.interactive.Players;
import org.powerbot.game.api.methods.node.SceneEntities;
import org.powerbot.game.api.methods.tab.Equipment;
import org.powerbot.game.api.methods.tab.Inventory;
import org.powerbot.game.api.methods.widget.Bank;
import org.powerbot.game.api.util.Random;
import org.powerbot.game.api.util.Timer;
import org.powerbot.game.api.wrappers.Tile;
import org.powerbot.game.api.wrappers.node.SceneObject;

public class BankingHandler extends Node {

    private Tile bankTile = new Tile(3093,3494,0);

    public boolean haveStaff(){
        if(Equipment.getCount(Globals.ID_WEAPON)>=1){
            Globals.ID_WEAPON = 1;
            return true;
        }
        return false;
    }

    public static boolean invChangeSleep(){
        Timer timeCheck = new Timer(Random.nextInt(1200, 1600));
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

    public boolean toggleBank(boolean open){
        SceneObject bankBooth = SceneEntities.getNearest(Globals.ID_BANK_BOOTH);
        if(open){
            if(!Bank.isOpen()){
                BlackUnicornKiller.status = "Opening bank booth.";
                int x = 0;
                do{
                    x++;
                    BlackUnicornKiller.interacting=bankBooth;
                    bankBooth.interact("Bank");
                    Task.sleep(750,1000);
                    do{
                        Task.sleep(20,30);
                    }while(Players.getLocal().isMoving());
                }while(!Bank.isOpen() && x<=10);
                if(Bank.isOpen()){return true;}
            }
        }else{
            Timer timeCheck = new Timer(5000);
            do{
                Bank.close();
                Task.sleep(125,250);
            }while(Bank.isOpen() && timeCheck.isRunning());
            if(!Bank.isOpen()){return true;}
        }
        return false;
    }

    @Override
    public boolean activate(){
        return Calculations.distanceTo(bankTile)<=8 && !Inventory.contains(Globals.ID_ITEMS_LOBSTER);
    }

    @Override
    public void execute(){
        boolean needStaff = false;

        if(!haveStaff()){
            needStaff = true;
        }

        if(toggleBank(true)){
            BlackUnicornKiller.status = "Depositing inventory.";
            Bank.depositInventory();
            invChangeSleep();
            for(int x = 0; x<= Globals.ITEMS_REQUIRED.length-1; x++){
                BlackUnicornKiller.status = "Withdrawing required items.";
                if(Bank.withdraw(Globals.ITEMS_REQUIRED[x],Globals.ITEMS_REQUIRED_AMOUNTS[x])){invChangeSleep();}
            }
            if(needStaff){
                Bank.withdraw(Globals.ID_WEAPON, 1);
                invChangeSleep();
                if(Inventory.contains(Globals.ID_WEAPON)){
                    Inventory.getItem(Globals.ID_WEAPON).getWidgetChild().interact("Wield");
                    invChangeSleep();
                }
            }
            toggleBank(false);
        }
    }

}