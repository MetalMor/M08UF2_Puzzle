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
    MediaPlayer mp;
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
                //perdem el focus per exemple, una altre reproductor de música
                case AudioManager.AUDIOFOCUS_LOSS:
                    mp.stop();
                    Log.d(LOG, "AudioFocus: rebut AUDIOFOCUS_LOSS");
                    mp.release();
                    mp = null;
                    break;
                //perdem el focus temporalement, per exemple, trucada
                case AudioManager.AUDIOFOCUS_LOSS_TRANSIENT:
                    if (mp.isPlaying())
                        mp.pause();

                    Log.d(LOG, "AudioFocus: rebut AUDIOFOCUS_LOSS_TRANSIENT");

                    break;
                //baixem el volum temporalment
                case AudioManager.AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK:
                    mp.setVolume(0.5f, 0.5f);
                    Log.d(LOG, "AudioFocus: rebut AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK");
                    break;

                //es recupera el focus d'audio
                case AudioManager.AUDIOFOCUS_GAIN:
                    mp.start();
                    mp.setVolume(1.0f, 1.0f);
                    Log.d(LOG, "AudioFocus: rebut AUDIOFOCUS_GAIN");
                    break;

                default:
                    Log.e(LOG, "codi desconegut");
            }
        }
    };
}

