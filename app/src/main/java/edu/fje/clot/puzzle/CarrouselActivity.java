package edu.fje.clot.puzzle;

import android.app.Activity;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.widget.ImageView;

/**
 * Created by m0r on 21/02/17.
 */

public class CarrouselActivity extends Activity {

    ImageView imageView;
    int counter = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        imageView = (ImageView) findViewById(R.id.carrousel_image);

        new android.os.Handler().postDelayed(new Runnable() {
            public void run() {
                // TODO run carrousel...
            }
        }, 1000);
    }

    private Drawable[] findImages() {
        Drawable[] images = new Drawable[9];
        TypedArray layouts = getResources().obtainTypedArray(R.array.carrousel_images);
        for(int i = 0; i < 9; i++)
            images[i] = getResources().getDrawable(layouts.getResourceId(i, -1));
        layouts.recycle();
        return images;
    }

    private Drawable getImageByIndex() {
        try {
            return findImages()[counter];
        } catch (ArrayIndexOutOfBoundsException aioobEx) {
            counter = 0;
            return findImages()[counter];
        }
    }
}
