package pl.polsl.ptakjakub.gamebook.activities.mainmenu;

import android.content.Intent;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.widget.Toast;

import java.util.HashMap;

import pl.polsl.ptakjakub.gamebook.GamebookApplication;
import pl.polsl.ptakjakub.gamebook.R;
import pl.polsl.ptakjakub.gamebook.activities.base.AbstractGamebookActivity;
import pl.polsl.ptakjakub.gamebook.activities.instruction.InstructionActivity;
import pl.polsl.ptakjakub.gamebook.activities.paragraph.ParagraphActivity;
import pl.polsl.ptakjakub.gamebook.dto.Player;
import pl.polsl.ptakjakub.gamebook.exceptions.GamebookException;
import pl.polsl.ptakjakub.gamebook.utils.Dice;

/**
 * Main menu activity class.
 *
 * @author Jakub Ptak
 * @version 1.0
 */
public class MainMenuActivity extends AbstractGamebookActivity {

    private boolean swipeEnabled;
    private int paragraph = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        swipeEnabled = true;
    }

    @Override
    protected void onResume() {
        super.onResume();
        swipeEnabled = true;
    }

    @Override
    protected void onPause() {
        super.onPause();
        this.textToSpeech.stop();
    }

    @Override
    protected void onInitializationFinished() {
        speakParams = new HashMap<String, String>();
        speakParams.put(TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID, "introductionSpeech");
        speakOut(getString(R.string.welcome_speech), speakParams);
    }

    @Override
    protected void onDoneSpeaking(String uuId) {
        if (uuId.equalsIgnoreCase("swipeRightFinished")) {
            try {
                Log.i(this.getClass().getName(), "Swipe right occurred.");
                Player player = new Player();
                Dice dice = new Dice();
                int agi = dice.roll(getAssets(), 1) + 6;
                int vit = dice.roll(2) + 12;
                int luck = dice.roll(1) + 6;
                player.setAgility(agi);
                player.setVitality(vit);
                player.setMaxVitality(vit);
                player.setLuck(luck);
                ((GamebookApplication) getApplicationContext()).setPlayer(player);
                swipeEnabled = false;
                speakParams = new HashMap<String, String>();
                speakParams.put(TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID, "characterCreated");
                speakOut(getString(R.string.character_stats)
                        .replace("{0}", Integer.toString(player.getAgility()))
                        .replace("{1}", Integer.toString(player.getVitality()))
                        .replace("{2}", Integer.toString(player.getLuck())),
                        speakParams);
            } catch (GamebookException ge) {
                Log.e(this.getClass().getName(), ge.getMessage());
                Toast.makeText(this, R.string.error_stats_rolling, Toast.LENGTH_SHORT).show();
            }
        } else if (uuId.equalsIgnoreCase("swipeLeftFinished")) {
            Intent intent = new Intent(MainMenuActivity.this, ParagraphActivity.class);
            intent.putExtra("paragraph", paragraph);
            startActivity(intent);

        } else if (uuId.equalsIgnoreCase("swipeUpFinished")) {
            Log.i(this.getClass().getName(), "Swipe up occurred.");
            swipeEnabled = false;
            Intent intent = new Intent(MainMenuActivity.this, InstructionActivity.class);
            startActivity(intent);
        } else if (uuId.equalsIgnoreCase("swipeDownFinished")) {
            swipeEnabled = false;
            Log.i(this.getClass().getName(), "Swipe down occurred.");
            closeApplication();
        } else if (uuId.equalsIgnoreCase("initSpeech")) {
            Log.i(this.getClass().getName(), "Initial speech finished");
        } else if (uuId.equalsIgnoreCase("characterCreated")) {
            Intent intent = new Intent(MainMenuActivity.this, ParagraphActivity.class);
            startActivity(intent);
        }
    }

    @Override
    public void onSwipeRight() {
        if (swipeEnabled) {
            speakParams = new HashMap<String, String>();
            speakParams.put(TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID, "swipeRightFinished");
            speakOut(getString(R.string.mainmenu_new_game), speakParams);
        }
    }

    @Override
    public void onSwipeLeft() {
        if (swipeEnabled) {
            Log.i(this.getClass().getName(), "Swipe left occurred.");
            swipeEnabled = false;
            paragraph = ((GamebookApplication)getApplicationContext()).loadGameState();
            if ( paragraph == -2 ) {
                speakOut(getString(R.string.loading_uncomplete), null);
                swipeEnabled = true;
            } else if ( paragraph == -1 ) {
                speakOut(getString(R.string.loading_fail), null);
                swipeEnabled = true;
            } else {
                speakParams = new HashMap<String, String>();
                speakParams.put(TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID, "swipeLeftFinished");
                speakOut(getString(R.string.loading_successful) + " "
                        + ((GamebookApplication)getApplicationContext()).getPlayer().getPlayerDescription(),
                        speakParams);
            }
        }

    }

    @Override
    public void onSwipeUp() {
        if (swipeEnabled) {
            speakParams = new HashMap<String, String>();
            speakParams.put(TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID, "swipeUpFinished");
            speakOut(getString(R.string.mainmenu_instrucion_open), speakParams);
        }
    }

    @Override
    public void onSwipeDown() {
        if (swipeEnabled) {
            speakParams = new HashMap<String, String>();
            speakParams.put(TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID, "swipeDownFinished");
            speakOut(getString(R.string.app_close), speakParams);
            finish();
        }
    }

}
