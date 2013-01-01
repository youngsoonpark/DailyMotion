package com.rejasupotaro.dailymotion.utils;

import android.graphics.Bitmap;
import android.graphics.Matrix;

public class ImageUtils {
    public static Bitmap resize(Bitmap bitmap, int desiredSize) {
        return resize(bitmap, desiredSize, desiredSize);
    }
    
    public static Bitmap resize(Bitmap bitmap, int desiredWidth, int desiredHeight) {
        final int imageWidth = bitmap.getWidth();
        final int imageHeight = bitmap.getHeight();

        final float widthScaleRatio = desiredWidth / (float) imageWidth;
        final float heightScaleRatio = desiredHeight / (float) imageHeight;
        final float scaleRatio = Math.min(widthScaleRatio, heightScaleRatio);
        final Matrix matrix = new Matrix();
        matrix.postRotate(scaleRatio);

        if (!matrix.isIdentity()) {
            return bitmap.createBitmap(bitmap, 0, 0, imageWidth, imageHeight, matrix, true);
        } else {
            return bitmap;
        }
    }
}
