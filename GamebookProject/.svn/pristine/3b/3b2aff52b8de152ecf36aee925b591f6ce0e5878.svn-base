package pl.polsl.ptakjakub.gamebook.activities.instruction;

import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.widget.Toast;

import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.util.HashMap;
import java.util.ListIterator;

import pl.polsl.ptakjakub.gamebook.R;
import pl.polsl.ptakjakub.gamebook.activities.base.AbstractGamebookActivity;
import pl.polsl.ptakjakub.gamebook.dto.Instruction;
import pl.polsl.ptakjakub.gamebook.dto.InstructionEntry;
import pl.polsl.ptakjakub.gamebook.xml.InstructionXmlParser;

/**
 * Activity responsible for instruction presentation.
 *
 * @author Jakub Ptak
 * @version 1.0
 */
public class InstructionActivity extends AbstractGamebookActivity {

    private InstructionXmlParser instructionXmlParser;
    private Instruction instruction;
    private ListIterator<InstructionEntry> listIterator;
    private int currentEntry;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.intruction_activity);

        instructionXmlParser = new InstructionXmlParser();
        try {
            instruction = instructionXmlParser.parse(getAssets().open(getString(R.string.instruction_filename)));
        } catch (IOException e) {
            Log.e(this.getClass().getName(), getString(R.string.error_instruction_parsing), e);
            Toast.makeText(this, R.string.error_reading_xml_document, Toast.LENGTH_SHORT).show();
            this.finish();
        } catch (XmlPullParserException e) {
            Log.e(this.getClass().getName(), getString(R.string.error_instruction_parsing), e);
            Toast.makeText(this, R.string.error_reading_xml_document, Toast.LENGTH_SHORT).show();
            this.finish();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        this.textToSpeech.shutdown();
    }

    @Override
    public void finish() {
        textToSpeech.stop();
        super.finish();
    }

    /**
     * Starts instruction initialization speech.
     *
     * @param entry
     */
    private void instructionEntrySpeech(InstructionEntry entry) {
        speakParams = new HashMap<String, String>();
        speakParams.put(TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID, "instructionEntryEnd");
        speakOut(entry.getId() + ". " + entry.getEntryName() + ". " + entry.getEntryDescription(),
                speakParams);
    }

    @Override
    protected void onInitializationFinished() {
        speakOut(getString(R.string.instruction_opened), null);
        listIterator = instruction.getInstructionEntries().listIterator();
    }

    /**
     * Method responsible for actions after TextToSpeech module finishes executing speakOut().
     *
     * @param uuId id of the speakOut() action
     */
    protected void onDoneSpeaking(String uuId) {
        if ( uuId.equals("swipeDownFinished")) {
            Log.i(this.getClass().getName(), "Swipe down finished.");
            finish();
        }
    }

    @Override
    protected void onSwipeRight() {
        if ( currentEntry > 1 ) {
            currentEntry--;
            instructionEntrySpeech(instruction.getInstructionEntries().get(currentEntry-1));
        } else if (currentEntry == 0) {
            currentEntry = 1;
            instructionEntrySpeech(instruction.getInstructionEntries().get(currentEntry-1));
        } else  {
            currentEntry = 0;
            speakOut(getString(R.string.instrution_start), null);
        }

    }

    @Override
    protected void onSwipeLeft() {
        if ( currentEntry < instruction.getInstructionEntries().size() ) {
            currentEntry++;
            instructionEntrySpeech(instruction.getInstructionEntries().get(currentEntry-1));
        } else if ( currentEntry == instruction.getInstructionEntries().size() ) {
            currentEntry = instruction.getInstructionEntries().size() + 1;
            speakOut(getString(R.string.instrution_end), null);
        } else {
            speakOut(getString(R.string.instrution_end), null);
        }

    }

    @Override
    protected void onSwipeUp() {

    }

    @Override
    protected void onSwipeDown() {
        speakParams = new HashMap<String, String>();
        speakParams.put(TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID, "swipeDownFinished");
        speakOut(getString(R.string.instrution_back), speakParams);
    }
}
