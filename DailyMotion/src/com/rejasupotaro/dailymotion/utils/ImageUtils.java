package com.rejasupotaro.dailymotion.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;

public class ImageUtils {
    public static Bitmap resize(Bitmap bitmap, int reqSize) {
        return resize(bitmap, reqSize, reqSize);
    }

    public static Bitmap resize(Bitmap bitmap, int reqWidth, int reqHeight) {
        final int imageWidth = bitmap.getWidth();
        final int imageHeight = bitmap.getHeight();

        final float widthScaleRatio = reqWidth / (float) imageWidth;
        final float heightScaleRatio = reqHeight / (float) imageHeight;
        final float scaleRatio = Math.min(widthScaleRatio, heightScaleRatio);
        final Matrix matrix = new Matrix();
        matrix.postRotate(scaleRatio);

        if (!matrix.isIdentity()) {
            return bitmap.createBitmap(bitmap, 0, 0, imageWidth, imageHeight, matrix, true);
        } else {
            return bitmap;
        }
    }

    public static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {
            if (width > height) {
                inSampleSize = Math.round((float)height / (float)reqHeight);
            } else {
                inSampleSize = Math.round((float)width / (float)reqWidth);
            }
        }
        return inSampleSize;
    }

    public static Bitmap decodeSampledBitmapFromFile(String filePath, int reqWidth, int reqHeight) {
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(filePath, options);

        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeFile(filePath, options);
    }
}
