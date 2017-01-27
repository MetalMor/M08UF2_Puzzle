package edu.fje.clot.puzzle.audio;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import edu.fje.clot.puzzle.R;

/**
 * Created by vedmak on 27/01/17.
 */

public class MusicService extends Service {

    private static final String IDENTIFICADOR = "MusicService";
    MediaPlayer player;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        //Toast.makeText(this, "servei creat", Toast.LENGTH_LONG).show();
        Log.d(IDENTIFICADOR, "onCreate");

        player = MediaPlayer.create(this, R.raw.music);
        player.setLooping(true);
    }

    @Override
    public void onDestroy() {
        //Toast.makeText(this, "servei parat", Toast.LENGTH_LONG).show();
        Log.d(IDENTIFICADOR, "onDestroy");
        player.stop();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        //Toast.makeText(this, "servei iniciat", Toast.LENGTH_LONG).show();
        Log.d(IDENTIFICADOR, "onStartCommand");
        player.start();
        return super.onStartCommand(intent, flags, startId);
    }
}

