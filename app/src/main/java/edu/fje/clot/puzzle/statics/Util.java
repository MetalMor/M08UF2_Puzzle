package edu.fje.clot.puzzle.statics;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.ParcelFileDescriptor;

import java.io.FileDescriptor;
import java.io.IOException;

/**
 * Created by m0r on 03/02/17.
 */

public class Util {

    public static int toInteger(double d) {
        return (int) Math.floor(d);
    }
    public static Bitmap getBitmapFromUri(Uri uri, Context context) throws IOException {
        ParcelFileDescriptor parcelFileDescriptor =
                context.getContentResolver().openFileDescriptor(uri, "r");
        FileDescriptor fileDescriptor = parcelFileDescriptor.getFileDescriptor();
        Bitmap image = BitmapFactory.decodeFileDescriptor(fileDescriptor);
        parcelFileDescriptor.close();
        return image;
    }
    public static ComponentName intentService(Context context, Class service) {
        return context.startService(new Intent(context, service));
    }
    public static boolean destroyService(Context context, Class service) {
        return context.stopService(new Intent(context, service));
    }
}
