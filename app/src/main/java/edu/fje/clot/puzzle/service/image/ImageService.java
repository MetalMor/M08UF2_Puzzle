package edu.fje.clot.puzzle.service.image;


import android.app.Service;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.os.IBinder;

import java.util.ArrayList;
import java.util.List;

import edu.fje.clot.puzzle.R;
import edu.fje.clot.puzzle.statics.Util;


/**
 * Created by vedmak on 30/01/17.
 */

public class ImageService extends Service {
    private static ImageService instance;
    private static final int PUZZLE_SIZE = 9;
    private Bitmap image;
    private List<Bitmap> chunks = new ArrayList<Bitmap>();
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
    @Override
    public void onCreate() {
        image = BitmapFactory.decodeResource(getApplicationContext().getResources(), R.drawable.porky);
        instance = this;
    }
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        splitImage();
        return super.onStartCommand(intent, flags, startId);
    }

    private void splitImage() {
        int index, chunkHeight, chunkWidth;
        while(chunks.size() < PUZZLE_SIZE) {
            index = chunks.size();
            chunkHeight = toInteger(image.getHeight() / getProportion());
            chunkWidth = toInteger(image.getWidth() / getProportion());
            chunks.add(
                    Bitmap.createBitmap(image,
                            chunkWidth * getColumnIndex(index),
                            chunkHeight * getRowIndex(index),
                            chunkWidth, chunkHeight)
            );
        }
    }

    private static int toInteger(double d) {
        return Util.toInteger(d);
    }

    private static int getProportion() {
        return toInteger(Math.sqrt(PUZZLE_SIZE));
    }

    public static int getRowIndex(int index) {
        return toInteger(index / getProportion());
    }

    public static int getColumnIndex(int index) {
        return toInteger(index % getProportion());
    }

    public List<Bitmap> getChunks() {
        return chunks;
    }
    public static ImageService getInstance() {
        return instance;
    }

}

