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

                firstAnimation.playSequentially(
                        ObjectAnimator.ofFloat(imageView, PROPERTY_X_COORD, width),
                        ObjectAnimator.ofFloat(imageView, View.ALPHA, 1, 0)
                );

                firstAnimation.setDuration(500);
                firstAnimation.addListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(final Animator firstAnimator) {
                        super.onAnimationEnd(firstAnimator);

                        setImage();
                        imageView.setX(0 - imageView.getWidth());
                        lastAnimation.playSequentially(
                                ObjectAnimator.ofFloat(imageView, View.ALPHA, 0, 1),
                                ObjectAnimator.ofFloat(imageView, PROPERTY_X_COORD, width / 4)
                        );
                        lastAnimation.setDuration(500);
                        lastAnimation.addListener(new AnimatorListenerAdapter() {
                            @Override
                            public void onAnimationEnd(Animator lastAnimator) {
                                super.onAnimationEnd(lastAnimator);

                                new android.os.Handler().postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        firstAnimator.start();
                                    }
                                }, 1000);
                            }
                        });
                        lastAnimation.start();
                    }
                });
                firstAnimation.start();
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
