package edu.fje.clot.puzzle.image;


import android.app.Service;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.IBinder;


/**
 * Created by vedmak on 30/01/17.
 */

public class ImageService extends Service {
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
    private BitmapFactory pepito() {
        return new BitmapFactory();
    }

}

