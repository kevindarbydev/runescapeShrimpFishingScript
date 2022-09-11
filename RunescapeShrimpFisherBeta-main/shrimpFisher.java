import org.dreambot.api.Client;
import org.dreambot.api.methods.container.impl.Inventory;
import org.dreambot.api.methods.dialogues.Dialogues;
import org.dreambot.api.methods.map.Area;
import org.dreambot.api.methods.map.Tile;
import org.dreambot.api.methods.skills.Skill;
import org.dreambot.api.script.AbstractScript;
import org.dreambot.api.script.Category;
import org.dreambot.api.script.ScriptManifest;
import org.dreambot.api.wrappers.interactive.NPC;
import org.dreambot.api.wrappers.widgets.message.Message;

import java.awt.*;



//Levels fishing from level 1-20 in the lumbridge swamp fishing area.
//Author: javabrotato




@ScriptManifest(name = "Shrimp/Chovy Fisher", description = "Gets level required for fly fishing", author = "Brotato",
        version = 3.0, category = Category.FISHING, image = "")
public class brotatoFisher extends AbstractScript {
    State state;
    Tile fishTile = new Tile(3241, 3152);
    String s;
    NPC fishSpot;

    boolean isFishing = false;
    Area fishes = new Area(3238, 3159, 3247, 3146);

    @Override // Infinite loop
    public int onLoop() {

        switch (getState()) {
            case STOP:
                stop();
                break;
            case LOGOUT:
                log("Target level reached -- logging out.");
                getTabs().logout();
                break;


            case FISHING:
                log("isF is: " + isFishing);
                s = "Fishing";

                Dialogues d = getDialogues();
                while (d.inDialogue()) {
                    if (d.canContinue()) {
                        d.spaceToContinue();
                    }
                }
                if (!getLocalPlayer().isAnimating()){
                    isFishing = false;
                    break;
                }

                if (Inventory.isFull()) {
                    log("Inventory full -- dropping shrimps.");
                    Inventory.dropAll("Raw shrimps", "Raw anchovies");
                    isFishing = false;

                } else {
                    sleep(5000, 8000);
                }
                break;


            case LOGGEDIN:
                log("Looking for new fishing spot");
                NPC fishSpot = getNpcs().closest(f -> f != null && f.getName().contentEquals("Fishing spot") && fishes.contains(f));
                if (Inventory.isFull()) {
                    log("Inventory full -- dropping shrimps.");
                    Inventory.dropAll("Raw shrimps", "Raw anchovies");
                    isFishing = false;

                } else {
                    sleep(5000, 8000);
                }
                if (fishSpot != null) {
                    log("Found spot - interacted with: " + fishSpot.toString());
                    fishSpot.interact("Net");
                    isFishing = true;
                    log("isF is: " + isFishing);
                    break;
                }

                break;

        }
        return 123;
    }

    private enum State {
        STOP, LOGOUT, FISHING, LOGGEDIN


    }

    private State getState() {
        if (!Client.isLoggedIn()) {
            state = State.STOP;
        } else if (getSkills().getRealLevel(Skill.FISHING) >= 20) {
            state = State.LOGOUT;
        } else if (isFishing && Client.isLoggedIn()) {
            state = State.FISHING;
        } else if (Client.isLoggedIn() && !getLocalPlayer().isAnimating() && fishes.contains(getLocalPlayer())) {
            state = State.LOGGEDIN;
        }
        return state;
    }

    public void onStart() {
        log("Bot started");
        log("isF is: " + isFishing);
    }

    public void onExit() {
        log("Bot ended!");
    }

    public int randomNum(int i, int k) {
        int number = (int) (Math.random() * (k - i)) + i;
        return number;
    }

    @Override
    public void onPaint(Graphics g) {
        g.setColor(Color.RED);
        g.setFont(new Font("Arial", Font.BOLD, 15));
        g.drawString("String", 15, 266);
        g.drawString("String", 15, 282);
    }
/*
    @Override
    public void onGameMessage(Message message){
        if (message.getMessage().contentEquals("You cast out your net...")){
            isFishing = true;
        }
        if (message.getMessage().contains("You catch")){
            caught++;
        }
    }
    */


}
