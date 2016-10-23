package pl.polsl.ptakjakub.gamebook.utils;

import android.app.Activity;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.util.Log;

import java.io.IOException;
import java.util.Random;

import pl.polsl.ptakjakub.gamebook.exceptions.GamebookException;

/**
 * Class representing dice rolling.
 */
public class Dice {

    private int from;

    private int to;

    /**
     * Default constructor. Sets range from 1 to 6.
     */
    public Dice() {
        this(1, 6);
    }

    /**
     * Initializes dice for specified range.
     *
     * @param from first bound
     * @param to   second bound
     */
    public Dice(int from, int to) {
        this.from = from;
        this.to = to;
    }

    /**
     * Roll a dice and returns result.
     *
     * @param rollCount sets number of dice rolls
     * @return dice roll result
     */
    public int roll(int rollCount) {
        int result = 0;
        Random random = new Random();
        for (int i = 0; i < rollCount; i++) {
            result += random.nextInt(to - from + 1) + from;
        }

        return result;

    }

    /**
     * Roll a dice, plays a sound and returns the result.
     *
     * @param assetManager AssetsManager object.
     * @param rollCount    sets number of dice rolls
     * @return dice roll result
     * @throws GamebookException
     */
    public int roll(AssetManager assetManager, int rollCount) throws GamebookException {
        try {
            AssetFileDescriptor afd = assetManager.openFd("sound/roll-dice-sound.mp3");
            MediaPlayer player = new MediaPlayer();
            player.setAudioStreamType(AudioManager.STREAM_MUSIC);
            player.setDataSource(afd.getFileDescriptor(), afd.getStartOffset(), afd.getLength());
            player.prepare();
            player.start();
            while(player.isPlaying()) {

            }
            player.stop();
            afd.close();
            int result = 0;
            Random random = new Random();
            for (int i = 0; i < rollCount; i++) {
                result += random.nextInt(to - from + 1) + from;
            }

            return result;
        } catch (IOException ioe) {
            Log.e(this.getClass().getName(), ioe.getMessage());
            throw new GamebookException(ioe);
        }
    }
}
