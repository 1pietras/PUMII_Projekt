package pl.polsl.ptakjakub.gamebook;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.Toast;

import java.io.IOException;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import pl.polsl.ptakjakub.gamebook.dto.Player;
import pl.polsl.ptakjakub.gamebook.dto.Item;
import pl.polsl.ptakjakub.gamebook.exceptions.GamebookException;
import pl.polsl.ptakjakub.gamebook.xml.XPathGamebookParser;

/**
 * Gamebook application main class for storing application data..
 *
 * @author Jakub Ptak
 * @version 1.0
 */
public class GamebookApplication extends Application {

    private static String SAVE_FILE = "GamebookSave";

    private XPathGamebookParser xPathGamebookParser;
    private Player player;

    @Override
    public void onCreate() {
        super.onCreate();

        try {
            xPathGamebookParser = new XPathGamebookParser(getAssets().open(getString(R.string.gamebook_filename)));
        } catch (IOException ioe) {
            Log.e(this.getClass().getName(), ioe.getMessage());
            Toast.makeText(this, R.string.error_reading_xml_document, Toast.LENGTH_SHORT).show();
        } catch (GamebookException ge) {
            Toast.makeText(this, R.string.error_reading_xml_document, Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Gets player's player.
     *
     * @return player
     */
    public Player getPlayer() {
        return player;
    }

    /**
     * Sets player's player.
     *
     * @param player player's player
     */
    public void setPlayer(Player player) {
        this.player = player;
    }

    /**
     * Gets XPath parser object for game XML document.
     *
     * @return xpath parser
     */
    public XPathGamebookParser getxPathGamebookParser() {
        return xPathGamebookParser;
    }

    /**
     * Saves current state of a game in SharedPreferences.
     *
     * @return true if saving was successful, false otherwise
     */
    public boolean saveGameState(int currentParagraph) {
        SharedPreferences sharedPreferences = getSharedPreferences(SAVE_FILE, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(getString(R.string.sp_agility), player.getAgility());
        editor.putInt(getString(R.string.sp_vitality), player.getVitality());
        editor.putInt(getString(R.string.sp_max_vitality), player.getMaxVitality());
        editor.putInt(getString(R.string.sp_luck), player.getLuck());
        editor.putInt(getString(R.string.sp_foodAmount), player.getFoodAmount());
        editor.putInt(getString(R.string.sp_current_paragraph), currentParagraph);
        Set<String> itemIdsSet = new LinkedHashSet<String>();
        for (Item i : player.getItems())
            itemIdsSet.add(Integer.toString(i.getId()));

        editor.putStringSet(getString(R.string.sp_items), itemIdsSet);

        boolean result = editor.commit();
        if (result) {
            Log.i(this.getClass().getName(), getString(R.string.saving_successful));
        } else {
            Log.e(this.getClass().getName(), getString(R.string.saving_fail));
        }
        return result;
    }

    /**
     * Loads state of a game stored in SharedPreferences.
     * If loading is successful paragraph number will be returned.
     * If there is no data inside or error occures it returns -1.
     * If there is SharedPreferences file missing any preference it returns -2.
     *
     * @return paragraph
     */
    public int loadGameState() {

        SharedPreferences sharedPreferences = getSharedPreferences(SAVE_FILE, Context.MODE_PRIVATE);

        if (sharedPreferences.contains(getString(R.string.sp_current_paragraph))
                && sharedPreferences.contains(getString(R.string.sp_agility))
                && sharedPreferences.contains(getString(R.string.sp_vitality))
                && sharedPreferences.contains(getString(R.string.sp_max_vitality))
                && sharedPreferences.contains(getString(R.string.sp_luck))
                && sharedPreferences.contains(getString(R.string.sp_foodAmount))
                && sharedPreferences.contains(getString(R.string.sp_items))) {
            try {
                Player loadedPlayer = new Player();
                int currentParagraph = sharedPreferences.getInt(getString(R.string.sp_current_paragraph), -999);
                loadedPlayer.setAgility(sharedPreferences.getInt(getString(R.string.sp_agility), -999));
                loadedPlayer.setVitality(sharedPreferences.getInt(getString(R.string.sp_vitality), -999));
                loadedPlayer.setMaxVitality(sharedPreferences.getInt(getString(R.string.sp_max_vitality), -999));
                loadedPlayer.setLuck(sharedPreferences.getInt(getString(R.string.sp_luck), -999));
                loadedPlayer.setFoodAmount(sharedPreferences.getInt(getString(R.string.sp_foodAmount), -999));
                Set<String> loadedItemIdsSet = sharedPreferences.getStringSet(getString(R.string.sp_items), null);
                List<Item> gameItems = xPathGamebookParser.getGameItemList();
                if (loadedItemIdsSet != null) {
                    for (Item gameItem : gameItems) {
                        if (loadedItemIdsSet.contains(Integer.toString(gameItem.getId())))
                            player.addItem(gameItem);
                    }
                }

                if (currentParagraph != -999
                        && loadedPlayer.getAgility() != -999
                        && loadedPlayer.getVitality() != 999
                        && loadedPlayer.getMaxVitality() != 999
                        && loadedPlayer.getLuck() != 999
                        && loadedPlayer.getFoodAmount() != 999
                        && loadedPlayer.getItems() != null) {
                    Log.i(this.getClass().getName(), getString(R.string.loading_successful));
                    player = loadedPlayer;
                    return currentParagraph;
                } else {
                    return -1;
                }

            } catch (Exception e) {
                Log.e(this.getClass().getName(), getString(R.string.loading_fail), e);
                return -1;
            }

        } else {
            return -2;
        }
    }
}
