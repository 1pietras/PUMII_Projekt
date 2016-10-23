package pl.polsl.ptakjakub.gamebook.activities.paragraph;

import android.content.Intent;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.Toast;

import java.util.HashMap;
import java.util.List;

import pl.polsl.ptakjakub.gamebook.GamebookApplication;
import pl.polsl.ptakjakub.gamebook.R;
import pl.polsl.ptakjakub.gamebook.activities.base.AbstractGamebookActivity;
import pl.polsl.ptakjakub.gamebook.activities.mainmenu.MainMenuActivity;
import pl.polsl.ptakjakub.gamebook.dto.Player;
import pl.polsl.ptakjakub.gamebook.dto.Creature;
import pl.polsl.ptakjakub.gamebook.dto.Effect;
import pl.polsl.ptakjakub.gamebook.dto.Item;
import pl.polsl.ptakjakub.gamebook.dto.Path;
import pl.polsl.ptakjakub.gamebook.dto.Range;
import pl.polsl.ptakjakub.gamebook.exceptions.GamebookException;
import pl.polsl.ptakjakub.gamebook.paragraphs.CrossroadParagraph;
import pl.polsl.ptakjakub.gamebook.paragraphs.DicegameParagraph;
import pl.polsl.ptakjakub.gamebook.paragraphs.FightParagraph;
import pl.polsl.ptakjakub.gamebook.paragraphs.ItemlossParagraph;
import pl.polsl.ptakjakub.gamebook.paragraphs.ItempickParagraph;
import pl.polsl.ptakjakub.gamebook.paragraphs.NormalParagraph;
import pl.polsl.ptakjakub.gamebook.paragraphs.SinglepathParagraph;
import pl.polsl.ptakjakub.gamebook.utils.Dice;
import pl.polsl.ptakjakub.gamebook.xml.XPathGamebookParser;


/**
 * Activity responsible for paragraph processing.
 *
 * @author Jakub Ptak
 * @version 1.0
 */
public class ParagraphActivity extends AbstractGamebookActivity {

    private XPathGamebookParser xPathGamebookParser;
    private Integer currentParagraph;
    private boolean swipeEnabled;
    private boolean diceRollEnabled;
    private boolean isClosing;
    private int rollNumber;
    private Creature creature;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.paragraph_activity);
        swipeEnabled = false;
        diceRollEnabled = false;
        isClosing = false;

        xPathGamebookParser = ((GamebookApplication) getApplicationContext()).getxPathGamebookParser();
        try {
            Intent intent = getIntent();
            currentParagraph = intent.getExtras().getInt("paragraph");
        } catch (NullPointerException npe) {
            currentParagraph = 0;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        swipeEnabled = false;
        diceRollEnabled = false;
        isClosing = false;
        readCurrentParagraph();
    }

    @Override
    protected void onPause() {
        super.onPause();
        this.textToSpeech.stop();
        ((GamebookApplication)getApplicationContext()).saveGameState(currentParagraph);
    }

    @Override
    protected void onInitializationFinished() {
        readCurrentParagraph();
    }

    @Override
    protected void onDoneSpeaking(String uuId) {
        try {
            if (!isClosing) {
                if (uuId.equals("singlepath")) {
                    SinglepathParagraph singlepathParagraph = xPathGamebookParser.getSinglepathParagraph(currentParagraph);
                    List<Effect> effects = singlepathParagraph.getEffects();
                    if (effects != null && effects.size() > 0) {
                        processEffects(effects);
                    }
                    if (isPlayerAlive()) {
                        currentParagraph = singlepathParagraph.getPath().getTargetParagraph();
                        readCurrentParagraph();
                    } else {
                        playerDeath();
                    }
                } else if (uuId.equals("normal")) {
                    NormalParagraph normalParagraph = xPathGamebookParser.getNormalParagraph(currentParagraph);
                    List<Effect> effects = normalParagraph.getEffects();
                    if (effects != null && effects.size() > 0) {
                        processEffects(effects);
                    }
                    if (isPlayerAlive()) {
                        swipeEnabled = true;
                    } else {
                        playerDeath();
                    }
                } else if (uuId.equals("food")) {
                    swipeEnabled = true;
                } else if (uuId.equals("crossroad")) {
                    CrossroadParagraph crossroadParagraph = xPathGamebookParser.getCrossroadParagraph(currentParagraph);
                    List<Effect> effects = crossroadParagraph.getEffects();
                    if (effects != null && effects.size() > 0) {
                        processEffects(effects);
                    }
                    if (isPlayerAlive()) {
                        swipeEnabled = true;
                    } else {
                        playerDeath();
                    }
                } else if (uuId.equals("dicegame")) {
                    rollNumber = 1;
                    diceRollEnabled = true;
                } else if (uuId.equals("checkluck")) {
                    rollNumber = 2;
                    diceRollEnabled = true;

                } else if (uuId.equals("itemloss")) {
                    ItemlossParagraph itemlossParagraph = xPathGamebookParser.getItemlossParagraph(currentParagraph);
                    Player player = ((GamebookApplication) getApplicationContext()).getPlayer();
                    Item item = xPathGamebookParser.getItem(itemlossParagraph.getItemId());
                    player.removeItem(item);
                    if (item.getAttribute() != null && item.getValue() != null) {
                        if (item.getAttribute().equalsIgnoreCase(getString(R.string.attr_agility)))
                            player.modifyAgility(-item.getValue());
                        else if (item.getAttribute().equalsIgnoreCase(getString(R.string.attr_vitality)))
                            player.modifyVitality(-item.getValue());
                        else if (item.getAttribute().equalsIgnoreCase(getString(R.string.attr_luck))) {
                            player.modifyLuck(-item.getValue());
                        }
                    }
                    List<Effect> effects = itemlossParagraph.getEffects();
                    if (effects != null && effects.size() > 0) {
                        processEffects(effects);
                    }
                    if (isPlayerAlive()) {
                        currentParagraph = itemlossParagraph.getPath().getTargetParagraph();
                        readCurrentParagraph();
                    } else {
                        playerDeath();
                    }

                } else if (uuId.equals("itempick")) {
                    ItempickParagraph itempickParagraph = xPathGamebookParser.getItempickParagraph(currentParagraph);
                    Player player = ((GamebookApplication) getApplicationContext()).getPlayer();
                    Item item = xPathGamebookParser.getItem(itempickParagraph.getItemId());
                    player.addItem(item);
                    if (item.getAttribute() != null && item.getValue() != null) {
                        if (item.getAttribute().equalsIgnoreCase(getString(R.string.attr_agility)))
                            player.modifyAgility(item.getValue());
                        else if (item.getAttribute().equalsIgnoreCase(getString(R.string.attr_vitality)))
                            player.modifyVitality(item.getValue());
                        else if (item.getAttribute().equalsIgnoreCase(getString(R.string.attr_luck))) {
                            player.modifyLuck(item.getValue());
                        }
                    }
                    List<Effect> effects = itempickParagraph.getEffects();
                    if (effects != null && effects.size() > 0) {
                        processEffects(effects);
                    }
                    if (isPlayerAlive()) {
                        currentParagraph = itempickParagraph.getPath().getTargetParagraph();
                        readCurrentParagraph();
                    } else {
                        playerDeath();
                    }
                } else if (uuId.equals("end")) {
                    String endType = xPathGamebookParser.getEndParagraph(currentParagraph).getEnd();
                    speakParams = new HashMap<String, String>();
                    speakParams.put(TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID, "endtext");
                    if (endType.equalsIgnoreCase(getString(R.string.end_win))) {
                        speakOut(getString(R.string.win_text), speakParams);
                    } else if (endType.equalsIgnoreCase(getString(R.string.end_loss))) {
                        speakOut(getString(R.string.loss_text), speakParams);
                    }

                } else if (uuId.equals("fight")) {
                    FightParagraph fightParagraph = xPathGamebookParser.getFightParagraph(currentParagraph);
                    creature = fightParagraph.getCreature();
                    if (creature != null) {
                        speakParams = new HashMap<String, String>();
                        speakParams.put(TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID, "fightstart");
                        speakOut(getString(R.string.fight_creature)
                                .replace("{0}", creature.getName())
                                .replace("{1}", Integer.toString(creature.getAgility()))
                                .replace("{2}", Integer.toString(creature.getVitality())),
                                speakParams);
                    }
                } else if (uuId.equalsIgnoreCase("foodEating")) {
                    currentParagraph = xPathGamebookParser.getFoodParagraph(currentParagraph).getPath().getTargetParagraph();
                    readCurrentParagraph();
                } else if (uuId.equalsIgnoreCase("gameleft")) {
                    this.closeApplication();
                } else if (uuId.equalsIgnoreCase("endtext")) {
                    Intent intent = new Intent(ParagraphActivity.this, MainMenuActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                } else if (uuId.equalsIgnoreCase("rollFinished")) {
                    readCurrentParagraph();
                } else if (uuId.equalsIgnoreCase("fightstart")) {
                    rollNumber = 2;
                    diceRollEnabled = true;
                } else if (uuId.equalsIgnoreCase("fightnextturn")) {
                    if (isPlayerAlive()) {
                        if (creature.getVitality() > 0) {
                            rollNumber = 2;
                            diceRollEnabled = true;
                        } else {
                            speakParams = new HashMap<String, String>();
                            speakParams.put(TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID, "creatureslayed");
                            speakOut(getString(R.string.creature_slained), speakParams);
                        }
                    } else {
                        speakParams = new HashMap<String, String>();
                        speakParams.put(TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID, "defeated");
                        speakOut(getString(R.string.fight_defeat), speakParams);
                    }
                } else if (uuId.equalsIgnoreCase("creatureslayed")) {
                    currentParagraph = xPathGamebookParser.getFightParagraph(currentParagraph).getPath().getTargetParagraph();
                    readCurrentParagraph();
                } else if (uuId.equalsIgnoreCase("defeated")) {
                    playerDeath();
                }
            } else {

                if (uuId.equalsIgnoreCase("paragraphClosed")) {
                    Intent intent = new Intent(ParagraphActivity.this, MainMenuActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                }
            }
        } catch (GamebookException ge) {
            Toast.makeText(this, R.string.error_reading_xml_document, Toast.LENGTH_SHORT).show();
            Log.e(this.getClass().getName(), ge.getMessage());
            this.finish();
        }
    }

    @Override
    protected void onSwipeRight() {
        Log.i(this.getClass().getName(), "Swipe right called");
        try {
            if (swipeEnabled) {
                String type = xPathGamebookParser.getParagraphType(currentParagraph);

                Path path = null;
                if (type.equalsIgnoreCase(getString(R.string.normal_type))) {
                    for (Path p : xPathGamebookParser.getNormalParagraph(currentParagraph).getPaths()) {
                        if (p.getSwipe().equalsIgnoreCase(getString(R.string.swipe_right))) {
                            path = p;
                            break;
                        }
                    }
                } else if (type.equalsIgnoreCase(getString(R.string.food_type))) {
                    path = xPathGamebookParser.getFoodParagraph(currentParagraph).getPath();

                } else if (type.equalsIgnoreCase(getString(R.string.crossroad_type))) {
                    for (Path p : xPathGamebookParser.getCrossroadParagraph(currentParagraph).getPaths()) {
                        if (p.getSwipe().equalsIgnoreCase(getString(R.string.swipe_right))) {
                            path = p;
                            break;
                        }
                    }
                }

                if (path != null) {
                    if (path.getRequirement() == null) {
                        currentParagraph = path.getTargetParagraph();
                        readCurrentParagraph();
                    } else {
                        if (((GamebookApplication) getApplicationContext()).getPlayer().hasItem(path.getRequirement())) {
                            currentParagraph = path.getTargetParagraph();
                            readCurrentParagraph();
                        } else {
                            speakOut(xPathGamebookParser.getItem(path.getRequirement()).getName() + " " + getString(R.string.is_required), null);
                        }
                    }
                }

            }
        } catch (GamebookException ge) {
            Toast.makeText(this, R.string.error_reading_xml_document, Toast.LENGTH_SHORT).show();
            Log.e(this.getClass().getName(), ge.getMessage());
            this.finish();
        }
    }

    @Override
    protected void onSwipeLeft() {
        Log.i(this.getClass().getName(), "Swipe left called");
        try {
            if (swipeEnabled) {
                String type = xPathGamebookParser.getParagraphType(currentParagraph);

                Path path = null;
                if (type.equalsIgnoreCase(getString(R.string.normal_type))) {
                    for (Path p : xPathGamebookParser.getNormalParagraph(currentParagraph).getPaths()) {
                        if (p.getSwipe().equalsIgnoreCase(getString(R.string.swipe_left))) {
                            path = p;
                            break;
                        }
                    }
                } else if (type.equalsIgnoreCase(getString(R.string.crossroad_type))) {
                    for (Path p : xPathGamebookParser.getCrossroadParagraph(currentParagraph).getPaths()) {
                        if (p.getSwipe().equalsIgnoreCase(getString(R.string.swipe_left))) {
                            path = p;
                            break;
                        }
                    }
                }

                if (path != null) {
                    if (path.getRequirement() == null) {
                        currentParagraph = path.getTargetParagraph();
                        readCurrentParagraph();
                    } else {
                        if (((GamebookApplication) getApplicationContext()).getPlayer().hasItem(path.getRequirement())) {
                            currentParagraph = path.getTargetParagraph();
                            readCurrentParagraph();
                        } else {
                            speakOut(xPathGamebookParser.getItem(path.getRequirement()).getName() + " " + getString(R.string.is_required), null);
                        }
                    }
                }

            }
        } catch (GamebookException ge) {
            Toast.makeText(this, R.string.error_reading_xml_document, Toast.LENGTH_SHORT).show();
            Log.e(this.getClass().getName(), ge.getMessage());
            this.finish();
        }
    }

    @Override
    protected void onSwipeUp() {
        Log.i(this.getClass().getName(), "Swipe up called");
        try {
            if (swipeEnabled) {
                String type = xPathGamebookParser.getParagraphType(currentParagraph);

                Path path = null;
                if (type.equalsIgnoreCase(getString(R.string.crossroad_type))) {
                    for (Path p : xPathGamebookParser.getCrossroadParagraph(currentParagraph).getPaths()) {
                        if (p.getSwipe().equalsIgnoreCase(getString(R.string.swipe_up))) {
                            path = p;
                            break;
                        }
                    }
                } else if (type.equalsIgnoreCase(getString(R.string.food_type))) {
                    int eatingStatus = ((GamebookApplication) getApplicationContext()).getPlayer().eatFood();
                    speakParams = new HashMap<String, String>();
                    speakParams.put(TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID, "foodEating");
                    switch (eatingStatus) {
                        case 0:
                            speakOut(getString(R.string.restore_0), speakParams);
                            break;
                        case 1:
                            speakOut(getString(R.string.restore_1), speakParams);
                            break;
                        case 2:
                            speakOut(getString(R.string.restore_2), speakParams);
                            break;
                        case 3:
                            speakOut(getString(R.string.restore_3), speakParams);
                            break;
                        default:
                            break;
                    }
                } else {
                    speakOut( ((GamebookApplication)getApplicationContext()).getPlayer().getPlayerDescription(), null );
                }
                if (path != null) {
                    if (path.getRequirement() == null) {
                        currentParagraph = path.getTargetParagraph();
                        readCurrentParagraph();
                    } else {
                        if (((GamebookApplication) getApplicationContext()).getPlayer().hasItem(path.getRequirement())) {
                            currentParagraph = path.getTargetParagraph();
                            readCurrentParagraph();
                        } else {
                            speakOut(xPathGamebookParser.getItem(path.getRequirement()).getName() + " " + getString(R.string.is_required), null);
                        }
                    }
                }

            }
        } catch (GamebookException ge) {
            Toast.makeText(this, R.string.error_reading_xml_document, Toast.LENGTH_SHORT).show();
            Log.e(this.getClass().getName(), ge.getMessage());
            this.finish();
        }
    }

    @Override
    protected void onSwipeDown() {
        Log.i(this.getClass().getName(), "Swipe down called");
        try {
            if (swipeEnabled) {
                String type = xPathGamebookParser.getParagraphType(currentParagraph);

                Path path = null;
                if (type.equalsIgnoreCase(getString(R.string.crossroad_type))) {
                    for (Path p : xPathGamebookParser.getCrossroadParagraph(currentParagraph).getPaths()) {
                        if (p.getSwipe().equalsIgnoreCase(getString(R.string.swipe_down))) {
                            path = p;
                            break;
                        }
                    }
                } else {
                    speakParams = new HashMap<String, String>();
                    speakParams.put(TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID, "gameleft");
                    speakOut(getString(R.string.game_left), speakParams);
                }

                if (path != null) {
                    if (path.getRequirement() == null) {
                        currentParagraph = path.getTargetParagraph();
                        readCurrentParagraph();
                    } else {
                        if (((GamebookApplication) getApplicationContext()).getPlayer().hasItem(path.getRequirement())) {
                            currentParagraph = path.getTargetParagraph();
                            readCurrentParagraph();
                        } else {
                            speakOut(xPathGamebookParser.getItem(path.getRequirement()).getName() + " " + getString(R.string.is_required), null);
                        }
                    }
                }

            } else {
                isClosing = true;
                speakParams = new HashMap<String, String>();
                speakParams.put(TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID, "paragraphClosed");
                speakOut(getString(R.string.paragraph_closed), speakParams);
            }
        } catch (GamebookException ge) {
            Toast.makeText(this, R.string.error_reading_xml_document, Toast.LENGTH_SHORT).show();
            Log.e(this.getClass().getName(), ge.getMessage());
            this.finish();
        }
    }

    @Override
    public boolean onDoubleTap(MotionEvent motionEvent) {
        Log.i(this.getClass().getName(), "Double tap occurred");
        if (diceRollEnabled) {
            try {
                String type = xPathGamebookParser.getParagraphType(currentParagraph);
                if (type.equalsIgnoreCase(getString(R.string.checkluck_type))) {

                    int result = new Dice().roll(getAssets(), rollNumber);
                    speakParams = new HashMap<String, String>();
                    speakParams.put(TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID, "rollFinished");
                    speakOut(getString(R.string.rolled) + result, speakParams);
                    Path p = checkLuckPath(result);
                    currentParagraph = p.getTargetParagraph();
                    diceRollEnabled = false;
                    rollNumber = 0;
                } else if (type.equalsIgnoreCase(getString(R.string.fight_type))) {
                    Dice dice = new Dice();
                    Player player = ((GamebookApplication) getApplicationContext()).getPlayer();
                    int playerResult;
                    int creatureResult;
                    playerResult = dice.roll(getAssets(), rollNumber) + player.getAgility();
                    creatureResult = dice.roll(rollNumber) + creature.getAgility();

                    int luckResult = dice.roll(rollNumber);
                    boolean isPlayerLucky = luckResult <= player.getLuck();

                    speakParams = new HashMap<String, String>();
                    speakParams.put(TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID, "fightNextTurn");
                    String text = null;
                    if (playerResult == creatureResult) {
                        text = getString(R.string.fight_draw);
                    } else if (playerResult > creatureResult) {
                        if (isPlayerLucky) {
                            creature.setVitality(creature.getVitality() - 4);
                            text = getString(R.string.fight_win_lucky);
                        } else {
                            creature.setVitality(creature.getVitality() - 1);
                            text = getString(R.string.fight_win_unlucky);
                        }
                    } else {
                        if (isPlayerLucky) {
                            player.modifyVitality(-1);
                            text = getString(R.string.fight_loss_lucky);
                        } else {
                            player.modifyVitality(-3);
                            text = getString(R.string.fight_loss_unlucky);
                        }
                    }
                    text += getString(R.string.fight_status)
                            .replace("{0}", Integer.toString(player.getVitality()))
                            .replace("{1}", Integer.toString(creature.getVitality()));
                    speakOut(text, speakParams);
                    diceRollEnabled = false;
                    rollNumber = 0;
                } else if (type.equalsIgnoreCase(getString(R.string.dicegame_type))) {
                    DicegameParagraph dicegameParagraph = xPathGamebookParser.getDicegameParagraph(currentParagraph);
                    if (dicegameParagraph.getRanges() != null && dicegameParagraph.getRanges().size() > 0) {
                        int minBound = getMinBound(dicegameParagraph.getRanges());
                        int maxBound = getMaxBound(dicegameParagraph.getRanges());
                        int value = new Dice(minBound, maxBound).roll(getAssets(), 1);
                        currentParagraph = dicegameParagraph.getRangeForValue(value).getNextParagraphId();
                        readCurrentParagraph();
                    }
                    diceRollEnabled = false;
                    rollNumber = 0;
                }

            } catch (GamebookException ge) {
                Toast.makeText(this, R.string.error_reading_xml_document, Toast.LENGTH_SHORT).show();
                Log.e(this.getClass().getName(), ge.getMessage());
                this.finish();
            }
        }
        return true;
    }

    /**
     * Gets maximal bound for ranges.
     *
     * @param ranges ranges
     * @return max bound
     */
    private int getMaxBound(List<Range> ranges) {
        int maxBound = ranges.get(0).getTo();
        for (Range range : ranges) {
            maxBound = range.getTo() > maxBound ? range.getTo() : maxBound;
        }
        return maxBound;
    }

    /**
     * Gets minimal bound for ranges.
     *
     * @param ranges ranges
     * @return min bound
     */
    private int getMinBound(List<Range> ranges) {
        int minBound = ranges.get(0).getFrom();
        for (Range range : ranges) {
            minBound = range.getFrom() < minBound ? range.getFrom() : minBound;
        }
        return minBound;
    }

    /**
     * Checks player's luck and returns appropriate path.
     * It also decreases player's luck by 1.
     *
     * @param rollResult dice rolling result
     * @return path
     */
    private Path checkLuckPath(int rollResult) {
        Path path = null;
        try {
            int playersLuck = ((GamebookApplication) getApplicationContext()).getPlayer().getLuck();
            ((GamebookApplication) getApplicationContext()).getPlayer().modifyLuck(-1);

            if (rollResult <= playersLuck)
                path = xPathGamebookParser.getCheckluckParagraph(currentParagraph).getLuckyPath();
            else
                path = xPathGamebookParser.getCheckluckParagraph(currentParagraph).getUnluckPath();
        } catch (GamebookException ge) {
            Toast.makeText(this, R.string.error_reading_xml_document, Toast.LENGTH_SHORT).show();
            Log.e(this.getClass().getName(), ge.getMessage());
            this.finish();
        }
        return path;
    }

    /**
     * Reads text of current paragraph.
     */
    private void readCurrentParagraph() {
        try {
            swipeEnabled = false;
            if (isPlayerAlive()) {
                String type = xPathGamebookParser.getParagraphType(currentParagraph);

                String text;
                if (type.equalsIgnoreCase(getString(R.string.singlepath_type))) {
                    text = xPathGamebookParser.getSinglepathParagraph(currentParagraph).getText();
                    speakParams = new HashMap<String, String>();
                    speakParams.put(TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID, "singlepath");
                    speakOut(text, speakParams);
                } else if (type.equalsIgnoreCase(getString(R.string.normal_type))) {
                    text = xPathGamebookParser.getNormalParagraph(currentParagraph).getText();
                    speakParams = new HashMap<String, String>();
                    speakParams.put(TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID, "normal");
                    speakOut(text, speakParams);
                } else if (type.equalsIgnoreCase(getString(R.string.food_type))) {
                    text = xPathGamebookParser.getFoodParagraph(currentParagraph).getText();
                    speakParams = new HashMap<String, String>();
                    speakParams.put(TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID, "food");
                    speakOut(text, speakParams);
                } else if (type.equalsIgnoreCase(getString(R.string.crossroad_type))) {
                    text = xPathGamebookParser.getCrossroadParagraph(currentParagraph).getText();
                    speakParams = new HashMap<String, String>();
                    speakParams.put(TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID, "crossroad");
                    speakOut(text, speakParams);
                } else if (type.equalsIgnoreCase(getString(R.string.dicegame_type))) {
                    text = xPathGamebookParser.getDicegameParagraph(currentParagraph).getText();
                    speakParams = new HashMap<String, String>();
                    speakParams.put(TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID, "dicegame");
                    speakOut(text, speakParams);
                } else if (type.equalsIgnoreCase(getString(R.string.checkluck_type))) {
                    text = xPathGamebookParser.getCheckluckParagraph(currentParagraph).getText();
                    speakParams = new HashMap<String, String>();
                    speakParams.put(TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID, "checkluck");
                    speakOut(text, speakParams);
                } else if (type.equalsIgnoreCase(getString(R.string.itemloss_type))) {
                    text = xPathGamebookParser.getItemlossParagraph(currentParagraph).getText();
                    speakParams = new HashMap<String, String>();
                    speakParams.put(TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID, "itemloss");
                    speakOut(text, speakParams);
                } else if (type.equalsIgnoreCase(getString(R.string.itempick_type))) {
                    text = xPathGamebookParser.getItempickParagraph(currentParagraph).getText();
                    speakParams = new HashMap<String, String>();
                    speakParams.put(TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID, "itempick");
                    speakOut(text, speakParams);
                } else if (type.equalsIgnoreCase(getString(R.string.end_type))) {
                    text = xPathGamebookParser.getEndParagraph(currentParagraph).getText();
                    speakParams = new HashMap<String, String>();
                    speakParams.put(TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID, "end");
                    speakOut(text, speakParams);
                } else if (type.equalsIgnoreCase(getString(R.string.fight_type))) {
                    text = xPathGamebookParser.getFightParagraph(currentParagraph).getText();
                    speakParams = new HashMap<String, String>();
                    speakParams.put(TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID, "fight");
                    speakOut(text, speakParams);
                }
            } else {
                playerDeath();
            }
        } catch (GamebookException ge) {
            Toast.makeText(this, R.string.error_reading_xml_document, Toast.LENGTH_SHORT).show();
            Log.e(this.getClass().getName(), ge.getMessage());
            this.finish();
        }
    }

    /**
     * Processes effects of a paragraph and introduce the to player's character.
     *
     * @param effects effects to process
     */
    private void processEffects(List<Effect> effects) {
        Player player = ((GamebookApplication) getApplicationContext()).getPlayer();
        for (Effect effect : effects) {
            if (effect.getAttribute().equalsIgnoreCase(getString(R.string.attr_agility))) {
                player.modifyAgility(effect.getValue());
            } else if (effect.getAttribute().equalsIgnoreCase(getString(R.string.attr_vitality))) {
                player.modifyVitality(effect.getValue());
            } else if (effect.getAttribute().equalsIgnoreCase(getString(R.string.attr_luck))) {
                player.modifyLuck(effect.getValue());
            }
        }
    }

    /**
     * Check if player's vitality is greater than 0.
     *
     * @return true if vitality > 0, false otherwise
     */
    private boolean isPlayerAlive() {
        return ((GamebookApplication) getApplicationContext()).getPlayer().getVitality() > 0 ? true : false;
    }

    /**
     * Method called in case of player's character death.
     */
    private void playerDeath() {
        speakParams = new HashMap<String, String>();
        speakParams.put(TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID, "endtext");
        speakOut(getString(R.string.death_text), speakParams);
    }
}
