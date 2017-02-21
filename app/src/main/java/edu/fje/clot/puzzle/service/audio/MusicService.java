package edu.fje.clot.puzzle.service.audio;

import android.app.Service;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.IBinder;
import android.util.Log;

import edu.fje.clot.puzzle.R;

/**
 * Created by vedmak on 27/01/17.
 */

public class MusicService extends Service {

    private static MusicService instance;
    MediaPlayer backgroundMusic;
    MediaPlayer clickSound;
    private String LOG = "edu.fje.dam2";
    private boolean on = true;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        init();
    }

    @Override
    public void onDestroy() {
        stop();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if(instance == null) init();
        backgroundMusic.start();
        return super.onStartCommand(intent, flags, startId);
    }

    public void playClickSound() {
        if(isOn()) {
            clickSound.seekTo(0); // va un poco mal esto
            clickSound.start();
        }
    }

    public void play() {
        if(!isOn()) {
            on = true;
            backgroundMusic.start();
        }
    }

    public void stop() {
        stopMediaPlayer(backgroundMusic);
        stopMediaPlayer(clickSound);
    }

    private void stopMediaPlayer(MediaPlayer mediaPlayer) {
        if(mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer = null;
        }
    }

    public void pause() {
        if(isOn()) {
            on = false;
            backgroundMusic.pause();
        }
    }


    private void init() {
        backgroundMusic = MediaPlayer.create(this, R.raw.music);
        backgroundMusic.setLooping(true);
        clickSound = MediaPlayer.create(this, R.raw.click);
        instance = this;
    }

    public static MusicService getInstance() {
        return instance;
    }

    public boolean isOn() {
        return on;
    }

    private AudioManager.OnAudioFocusChangeListener mAudioFocusListener = new AudioManager.OnAudioFocusChangeListener() {
        public void onAudioFocusChange(int focusChange) {

            switch (focusChange) {
                case AudioManager.AUDIOFOCUS_LOSS:
                    stop();
                    break;
                case AudioManager.AUDIOFOCUS_LOSS_TRANSIENT:
                    pause();
                    break;
                case AudioManager.AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK:
                    pause();
                    break;

                case AudioManager.AUDIOFOCUS_GAIN:
                    play();
                    break;

                default:
                    Log.e(LOG, "codi desconegut");
            }
        }
    };
}

