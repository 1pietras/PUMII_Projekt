package pl.polsl.ptakjakub.gamebook.activities.base;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.speech.tts.UtteranceProgressListener;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.WindowManager;

import java.util.HashMap;
import java.util.Locale;

import pl.polsl.ptakjakub.gamebook.R;
import pl.polsl.ptakjakub.gamebook.activities.mainmenu.MainMenuActivity;

/**
 * Abstract class of Gamebook application capable of Text-To-Speech and Gesture detection.
 *
 * @author Jakub Ptak
 * @version 1.0
 */
public abstract class AbstractGamebookActivity extends Activity implements TextToSpeech.OnInitListener,
        GestureDetector.OnGestureListener, GestureDetector.OnDoubleTapListener {
    private static final int SWIPE_MIN_DISTANCE = 120;
    private static final int SWIPE_MIN_DIFFERENCE = 100;

    protected GestureDetector gestureDetector;
    protected TextToSpeech textToSpeech;
    protected HashMap<String, String> speakParams;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        if (getIntent().getBooleanExtra("EXIT", false)) {
            finish();
        } else {
            initializeGestureDetector();
            this.textToSpeech = new TextToSpeech(getApplicationContext(), this);
            this.textToSpeech.setOnUtteranceProgressListener(new UtteranceProgressListener() {
                @Override
                public void onStart(String s) {
                    Log.i(this.getClass().getName(), getString(R.string.speech_start) + " ID: " + s);
                }

                @Override
                public void onDone(String s) {
                    AbstractGamebookActivity.this.onDoneSpeaking(s);
                }

                @Override
                public void onError(String s) {
                    Log.e(this.getClass().getName(), getString(R.string.speech_error) + " ID: " + s);
                }
            });
            this.textToSpeech.setSpeechRate(0.8f);
        }
    }

    @Override
    public void onInit(int status) {
        if (status == TextToSpeech.SUCCESS) {

            int result = textToSpeech.setLanguage(Locale.US);

            if (result == TextToSpeech.LANG_MISSING_DATA
                    || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                Log.e("TTS", "This Language is not supported");
            } else {
                onInitializationFinished();
            }
        } else {
            Log.e("TTS", "Initilization Failed!");
        }
    }

    @Override
    public boolean onDown(MotionEvent motionEvent) {
        return false;
    }

    @Override
    public void onShowPress(MotionEvent motionEvent) {

    }

    @Override
    public boolean onSingleTapUp(MotionEvent motionEvent) {
        return false;
    }

    @Override
    public boolean onScroll(MotionEvent motionEvent, MotionEvent motionEvent2, float v, float v2) {
        return false;
    }

    @Override
    public void onLongPress(MotionEvent motionEvent) {

    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float v, float v2) {
        float xDiff = e1.getX() - e2.getX();
        float yDiff = e1.getY() - e2.getY();
        if ((Math.abs(xDiff) > SWIPE_MIN_DISTANCE ||
                Math.abs(yDiff) > SWIPE_MIN_DISTANCE) &&
                Math.abs(Math.abs(xDiff) - Math.abs(yDiff)) > SWIPE_MIN_DIFFERENCE) {

            if (Math.abs(xDiff) > Math.abs(yDiff)) {
                if (xDiff < 0)
                    onSwipeRight();
                else
                    onSwipeLeft();
            } else {
                if (yDiff < 0)
                    onSwipeDown();
                else
                    onSwipeUp();
            }
        }
        return true;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return gestureDetector.onTouchEvent(event);
    }

    @Override
    public boolean onSingleTapConfirmed(MotionEvent motionEvent) {
        return false;
    }

    @Override
    public boolean onDoubleTap(MotionEvent motionEvent) {
        return false;
    }

    @Override
    public boolean onDoubleTapEvent(MotionEvent motionEvent) {
        return false;
    }

    /**
     * Method responsible for immediate reading text by speaker.
     *
     * @param text   text to be spoken
     * @param params parameters of text-to-speak speak function
     */
    public void speakOut(String text, HashMap<String, String> params) {
        int result = textToSpeech.speak(text, TextToSpeech.QUEUE_FLUSH, params);
        if (result == TextToSpeech.ERROR) {
            Log.e(this.getClass().getName(), getApplicationContext().getString(R.string.error_textToSpeech_initialization));
        }
    }

    /**
     * Closes all application activities by going to MainMenuActivity with EXIT flag.
     */
    public void closeApplication() {
        Intent intent = new Intent(getApplicationContext(), MainMenuActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra("EXIT", true);
        startActivity(intent);
    }

    /**
     * Method called after initialization of TextToSpeech and Gesture detector modules.
     */
    protected abstract void onInitializationFinished();

    /**
     * Method called after finishing of text speaking.
     *
     * @param uuId String id of text spoken.
     */
    protected abstract void onDoneSpeaking(String uuId);

    /**
     * Action fired after performing swipe right gesture.
     */
    protected abstract void onSwipeRight();

    /**
     * Action fired after performing swipe left gesture.
     */
    protected abstract void onSwipeLeft();

    /**
     * Action fired after performing swipe up gesture.
     */
    protected abstract void onSwipeUp();

    /**
     * Action fired after performing swipe down gesture.
     */
    protected abstract void onSwipeDown();

    /**
     * Initializes gesture actions.
     */
    protected void initializeGestureDetector() {

        gestureDetector = new GestureDetector(this, this);
    }

}
