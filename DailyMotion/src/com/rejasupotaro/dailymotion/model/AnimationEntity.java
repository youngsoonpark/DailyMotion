package com.rejasupotaro.dailymotion.model;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.entity.mime.content.FileBody;

import android.graphics.Bitmap;
import android.net.Uri;

public class AnimationEntity {
    private String mTitle = "かわいみGIF";
    private List<Uri> mUriList;
    private List<Bitmap> mBitmapList;
    private int mDelay;

    public AnimationEntity() {
        mUriList = new ArrayList<Uri>();
        mBitmapList = new ArrayList<Bitmap>();
    }

    public void setTitle(String title) {
        mTitle = title;
    }
    
    public String getTitle() {
        return mTitle;
    }

    public void setDelay(int delay) {
        mDelay = delay;
    }
    
    public int getDelay() {
        return mDelay;
    }

    public void add(Uri uri, Bitmap bitmap) {
        if (uri == null || bitmap == null) return;

        mUriList.add(uri);
        mBitmapList.add(bitmap);
    }

    public int size() {
        return mUriList.size();
    }

    public List<Bitmap> getBitmapList() {
        return mBitmapList;
    }

    public Bitmap getBitmap(int index) {
        return mBitmapList.get(index);
    }

    public List<Uri> getUriList() {
        return mUriList;
    }

    public List<FileBody> getFileBodyList() {
        List<FileBody> fileBodyList = new ArrayList<FileBody>();

        for (Uri imageUri: mUriList) {
            fileBodyList.add(new FileBody(new File(imageUri.toString())));
        }

        return fileBodyList;
    }
}
