package edu.fje.clot.puzzle;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.res.TypedArray;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.Display;
import android.view.View;
import android.widget.ImageView;

/**
 * Created by m0r on 21/02/17.
 */

public class CarrouselActivity extends Activity {

    // TODO probar! boton en la MainActivity que te abra esta Activity

    ImageView imageView;
    int imageIndex = 0;

    AnimatorSet firstAnimation;
    AnimatorSet lastAnimation;

    private static final String PROPERTY_X_COORD = "x";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_carrousel);
        imageView = (ImageView) findViewById(R.id.carrousel_image);

        new android.os.Handler().postDelayed(new Runnable() {
            public void run() {
                Display display = getWindowManager().getDefaultDisplay();
                Point size = new Point();
                final int width = size.x;

                display.getSize(size);
                imageView.setX(width/4);

                firstAnimation = new AnimatorSet();
                lastAnimation = new AnimatorSet();

                final AnimatorSet fa = firstAnimation;
                final AnimatorSet la = lastAnimation;

                fa.playSequentially(
                        ObjectAnimator.ofFloat(imageView, PROPERTY_X_COORD, width),
                        ObjectAnimator.ofFloat(imageView, View.ALPHA, 1, 0)
                );

                fa.setDuration(500);
                fa.addListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(final Animator firstAnimator) {
                        super.onAnimationEnd(firstAnimator);

                        setImage();
                        imageView.setX(0 - imageView.getWidth());
                        la.playSequentially(
                                ObjectAnimator.ofFloat(imageView, View.ALPHA, 0, 1),
                                ObjectAnimator.ofFloat(imageView, PROPERTY_X_COORD, width / 4)
                        );
                        la.setDuration(500);
                        la.addListener(new AnimatorListenerAdapter() {
                            @Override
                            public void onAnimationEnd(Animator lastAnimator) {
                                super.onAnimationEnd(lastAnimator);

                                new android.os.Handler().postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        fa.start();
                                    }
                                }, 1000);
                            }
                        });
                        la.start();
                    }
                });
                fa.start();
            }
        }, 1000);
    }

    private Drawable[] findImages() {
        int id;
        TypedArray layouts = getResources().obtainTypedArray(R.array.carrousel_images);
        Drawable[] images = new Drawable[layouts.length()];
        for(int i = 0; i < layouts.length(); i++) {
            id = layouts.getResourceId(i, -1);
            if(id != -1) images[i] = getResources().getDrawable(id);
        }
        layouts.recycle();
        return images;
    }

    private Drawable getImageByIndex() {
        try {
            return findImages()[imageIndex];
        } catch (ArrayIndexOutOfBoundsException aioobEx) {
            imageIndex = 0;
            return findImages()[imageIndex];
        }
    }

    private void setImage() {
        imageView.setImageDrawable(getImageByIndex());
        imageIndex++;
    }
}
