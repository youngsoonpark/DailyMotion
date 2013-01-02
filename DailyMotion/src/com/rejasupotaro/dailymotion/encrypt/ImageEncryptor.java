package com.rejasupotaro.dailymotion.encrypt;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;

import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.CipherOutputStream;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.util.Log;

import com.rejasupotaro.dailymotion.utils.CloseableUtils;

public class ImageEncryptor {

    private static final String TAG = ImageEncryptor.class.getSimpleName();
    private static final String ENCRYPT_ALGORYTHM = "AES/CBC/PKCS5Padding";
    private static final int DEFAULT_KEY_LENGTH = 16;

    // アプリ固有の情報ということでアプリをインストールした時間をもとに、
    // getByte()したときにデフォルトの鍵長の128ビットになるStringを返す
    public static String getPassword(Context context) {
        PackageInfo packageInfo = null;
        try {
            packageInfo = context.getPackageManager()
                    .getPackageInfo(context.getApplicationInfo().packageName, PackageManager.GET_META_DATA);
        } catch (NameNotFoundException e) {
            Log.e(TAG, e.toString());
            return null;
        }

        long installTime = packageInfo.firstInstallTime;
        return formatValidPassword(String.valueOf(installTime));
    }

    private static String formatValidPassword(String password) {
        while (password.length() < DEFAULT_KEY_LENGTH) {
            password = "0" + password;
        }
        return password;
    }

    public static void encrypt(String filePath, Bitmap bitmap, String password) 
            throws FileNotFoundException, IOException {
        encrypt(new File(filePath), bitmap, password);
    }
    
    public static void encrypt(File file, Bitmap bitmap, String password) 
            throws FileNotFoundException, IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(CompressFormat.JPEG, 100, baos);
        byte[] bin = baos.toByteArray();

        encrypt(file, bin, password);
    }

    public static void encrypt(String filePath, byte[] content, String password) 
            throws FileNotFoundException, IOException {
        encrypt(new File(filePath), content, password);
    }
    
    public static void encrypt(File file, byte[] content, String password) 
            throws FileNotFoundException, IOException {

        FileOutputStream fos = null;
        CipherOutputStream cos = null;
        try {
            Cipher cipher = Cipher.getInstance(ENCRYPT_ALGORYTHM);
            cipher.init(Cipher.ENCRYPT_MODE, getKey(password));
            fos = new FileOutputStream(file);
            cos = new CipherOutputStream(fos, cipher);
            fos.write(cipher.getIV());
            cos.write(content);
            cos.flush();
        } catch (NoSuchAlgorithmException e) {
            Log.e(TAG, e.toString());
        } catch (NoSuchPaddingException e) {
            Log.e(TAG, e.toString());
        } catch (InvalidKeyException e) {
            Log.e(TAG, e.toString());
        } finally {
            CloseableUtils.close(fos);
            CloseableUtils.close(cos);
        }
    }

    public static Bitmap decrypt(String filePath, String password) {
        CipherInputStream cis = null;
        try {
            FileInputStream fis = new FileInputStream(filePath);
            byte[] iv = new byte[DEFAULT_KEY_LENGTH];
            fis.read(iv);
            Cipher cipher = Cipher.getInstance(ENCRYPT_ALGORYTHM);
            IvParameterSpec ivspec = new IvParameterSpec(iv);
            cipher.init(Cipher.DECRYPT_MODE, getKey(password), ivspec);
            cis = new CipherInputStream(fis, cipher);
        } catch (FileNotFoundException e) {
            Log.e(TAG, e.toString());
        } catch (IOException e) {
            Log.e(TAG, e.toString());
        } catch (NoSuchAlgorithmException e) {
            Log.e(TAG, e.toString());
        } catch (NoSuchPaddingException e) {
            Log.e(TAG, e.toString());
        } catch (InvalidKeyException e) {
            Log.e(TAG, e.toString());
        } catch (InvalidAlgorithmParameterException e) {
            Log.e(TAG, e.toString());
        }
        if (cis == null) return null;

        Bitmap bitmap = null;
        try {
            bitmap = BitmapFactory.decodeStream(cis, null, null);
        } finally {
            CloseableUtils.close(cis);
        }
        return bitmap;
    }

    private static Key getKey(String password) {
        return new SecretKeySpec(password.getBytes(), "AES");
    }
}
