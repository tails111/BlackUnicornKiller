package BlackUnicornKiller.Nodes.CombatHandlers;


import org.powerbot.core.script.job.Task;
import org.powerbot.game.api.methods.Settings;
import org.powerbot.game.api.methods.Widgets;
import org.powerbot.game.api.methods.input.Keyboard;
import org.powerbot.game.api.methods.interactive.Players;

public class ActionBarHandler{

    public static void momentumCheck(){
        if(Settings.get(679)>=1){
            do{
                Task.sleep(50,100);
            }while(Players.getLocal().isInCombat());
            int x=0;
            do{
                x++;
                Task.sleep(400,600);
                if(abilityReady(1)){
                    executeAbility(1);
                }
                if(Players.getLocal().getMessage().matches("Momentum is now active.")){
                    break;
                }
            }while(x<=20);
        }
    }

    public static boolean abilityReady(int slotNum){
        final int TEXTURE_ID = 14521;
        final int ADRENA_TEXT_COLOR = 16777215;
        final int WIDGET = 640;
        int coolDownSlot = 0;
        int adrenaSlot = 0;

        switch (slotNum){
            case 1: coolDownSlot = 36;
                adrenaSlot = 32;
                break;
            case 2: coolDownSlot = 73;
                adrenaSlot = 72;
                break;
            case 3: coolDownSlot = 77;
                adrenaSlot = 76;
                break;
            case 4: coolDownSlot = 81;
                adrenaSlot = 80;
                break;
            case 5: coolDownSlot = 85;
                adrenaSlot = 84;
                break;
            case 6: coolDownSlot = 89;
                adrenaSlot = 88;
                break;
            case 7: coolDownSlot = 93;
                adrenaSlot = 92;
                break;
            case 8: coolDownSlot = 97;
                adrenaSlot = 96;
                break;
            case 9: coolDownSlot = 101;
                adrenaSlot = 100;
                break;
            case 0: coolDownSlot = 105;
                adrenaSlot = 104;
                break;
            case 10: coolDownSlot = 109;
                adrenaSlot = 108;
                break;
            case 11: coolDownSlot = 113;
                adrenaSlot = 112;
                break;

        }

        if(Widgets.get(WIDGET,coolDownSlot).getTextureId()==TEXTURE_ID){
            if(Widgets.get(WIDGET,adrenaSlot).getTextColor()==ADRENA_TEXT_COLOR){
                return true;
            }
        }
        return false;
    }

    public static boolean damageReduction(){
        if(Settings.get(901)>=400){
            return true;
        }
        return false;
    }

    public static int abilityCoolDown(int Slot){
        if(Slot == 7){
            if(Widgets.get(640,93).getTextureId()==10){
                return(0);
            }
        }
        return(0);
    }

    public static void executeAbility(int slotNum){
        switch (slotNum){
            case 1: Keyboard.sendText("1", false);
                break;
            case 2: Keyboard.sendText("2", false);
                Task.sleep(1850,2150);
                break;
            case 3: Keyboard.sendText("3", false);
                break;
            case 4: Keyboard.sendText("4", false);
                Task.sleep(1850,2150);
                break;
            case 5: Keyboard.sendText("5", false);
                Task.sleep(2150,2650);
                break;
            case 6: Keyboard.sendText("6", false);
                break;
            case 7: Keyboard.sendText("7", false);
                break;
            case 8: Keyboard.sendText("8", false);
                break;
            case 9: Keyboard.sendText("9", false);
                break;
            case 0: Keyboard.sendText("0", false);
                break;
            case 10: Keyboard.sendText("-", false);
                break;
            case 11: Keyboard.sendText("=", false);
                break;
        }
        Task.sleep(1000,1500);
    }
}