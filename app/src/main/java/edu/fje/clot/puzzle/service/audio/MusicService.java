package edu.fje.clot.puzzle.service.audio;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.IBinder;

import edu.fje.clot.puzzle.R;

/**
 * Created by vedmak on 27/01/17.
 */

public class MusicService extends Service {

    private static MusicService instance;
    MediaPlayer backgroundMusic;
    MediaPlayer clickSound;
    private boolean _on = true;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        backgroundMusic = MediaPlayer.create(this, R.raw.music);
        backgroundMusic.setLooping(true);
        clickSound = MediaPlayer.create(this, R.raw.click);
        instance = this;
    }

    @Override
    public void onDestroy() {
        backgroundMusic.stop();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
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
            setOn(true);
            backgroundMusic.start();
        }
    }

    public void pause() {
        if(isOn()) {
            setOn(false);
            backgroundMusic.pause();
        }
    }

    public static MusicService getInstance() {
        return instance;
    }

    public boolean isOn() {
        return _on;
    }

    private void setOn(boolean on) {
        _on = on;
    }
}

