package com.rejasupotaro.dailymotion.model;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.entity.FileEntity;
import org.apache.http.entity.InputStreamEntity;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.james.mime4j.stream.NameValuePair;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v4.content.AsyncTaskLoader;
import android.util.Log;

import com.rejasupotaro.dailymotion.Constants;
import com.rejasupotaro.dailymotion.DailyMotionUtils;

public class DailyMotionApiClient extends AsyncTaskLoader<Void> {

    private static final String TAG = DailyMotionApiClient.class.getSimpleName();
    public static final String UPLOAD_FILE_NAME = "upload_file_name";
    public static final String UPLOAD_FILE_CONTENTS = "upload_file_contents";
    public static final String CONTENTTYPE_BINARY = "application/octet-stream";
    public static final String CONTENTTYPE_ZIP = "application/zip";
    public static final String CONTENTTYPE_XML = "application/xml";
    private static final int BUFF_SIZE = 10240;

    private static final int DEFAULT_BUFFSIZE = -1;
    private static final Charset DEFAULT_CHARSET = Charset.forName("UTF-8");

    private Context mContext;
    private DefaultHttpClient httpClient;
    private String responseMessage;
    private List<Uri> mUriList;
    private List<FileBody> mFileBodyList;

    public DailyMotionApiClient(Context context) {
        super(context);
    }

    public DailyMotionApiClient(Context context, List<Uri> uriList, List<FileBody> fileBodyList) {
        super(context);
        mContext = context;
        mUriList = uriList;
        mFileBodyList = fileBodyList;

    }

    @Override
    public Void loadInBackground() {
        Log.d("DEBUG", "loadInBackground");

        Log.d("DEBUG", mContext.getExternalCacheDir().getPath() + "/out.zip");
        File zipFile = toZip(mContext.getExternalCacheDir().getPath() + "/out.zip", mUriList);

        try {
            fileUpload(Constants.APP_SITE_URL,
                    new NameValuePair(UPLOAD_FILE_NAME, "rejasupotaro"),
                    new NameValuePair(UPLOAD_FILE_CONTENTS, zipFile.getAbsolutePath()));
        } catch (IOException e) {
            Log.v(TAG, "Something wrong with fileUpload()");
        }

        return null;
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }

    public StatusLine fileUpload(String url,
            NameValuePair titleNameValuePair,
            NameValuePair fileNameValuePair) throws IOException {
        final HttpPost httpPost = new HttpPost(url);

        final MultipartEntity reqEntity =
                new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE);
        reqEntity.addPart(titleNameValuePair.getName(),
                new StringBody(titleNameValuePair.getValue(), DEFAULT_CHARSET));
        final File file = new File(fileNameValuePair.getValue());
        reqEntity.addPart(fileNameValuePair.getName(),
                new FileBody(file, CONTENTTYPE_BINARY));

        httpPost.setEntity(reqEntity);
        return getResponseStatusLine(httpClient, httpPost);
    }

    public StatusLine sendBinaryFile(String url,
            String absoluteFilePath) throws IOException {
        final File file = new File(absoluteFilePath);
        final HttpPost httpPost = new HttpPost(url);
        // length==-1 then BUFF_SIZE=2048
        final InputStreamEntity reqEntity =
                new InputStreamEntity(new FileInputStream(file), DEFAULT_BUFFSIZE);
        reqEntity.setContentType(CONTENTTYPE_BINARY);
        reqEntity.setChunked(true);
        httpPost.setEntity(reqEntity);
        return getResponseStatusLine(httpClient, httpPost);
    }

    public StatusLine sendSpecifiedFile(String url,
            String absoluteFilePath,
            String contentType) throws IOException  {
        final File file = new File(absoluteFilePath);
        final HttpPost httpPost = new HttpPost(url);
        final FileEntity reqEntity = new FileEntity(file, contentType);
        reqEntity.setChunked(true);
        httpPost.setEntity(reqEntity);
        return getResponseStatusLine(httpClient, httpPost);
    }

    public String getResponseMessage() {
        return responseMessage;
    }

    private StatusLine getResponseStatusLine(HttpClient httpClient,
            HttpRequestBase method)
                    throws ClientProtocolException, IOException {
        HttpResponse response = null;
        StatusLine status = null;
        try {
            response = httpClient.execute(method);
            status = response.getStatusLine();
            HttpEntity responseEntity = response.getEntity();
            if (responseEntity != null) {
                InputStream content = responseEntity.getContent();

                BufferedReader reader = new BufferedReader(new InputStreamReader(content));
                try {
                    StringBuilder buf = new StringBuilder();
                    String line;
                    while ((line = reader.readLine()) != null) {
                        buf.append(line);
                    }
                    responseMessage = buf.toString();
                } finally {
                    content.close();
                    reader.close();
                }
            }
        } finally {
            if (httpClient != null) {
                httpClient.getConnectionManager().shutdown();
            }
        }
        return status;
    }

    private File toZip(String outputFilePath, List<Uri> inputFileUriList) {
        // 古いファイルを削除
        File oldFile = new File(outputFilePath);
        if (oldFile.isFile()) {
            oldFile.delete();
        }

        ZipOutputStream zipOutputStream = null;
        BufferedInputStream bufferedInputStream = null;
        try {
            zipOutputStream = new ZipOutputStream(
                    new BufferedOutputStream(new FileOutputStream(outputFilePath)));

            byte[] buffer = new byte[BUFF_SIZE];
            for (int i = 0; i < inputFileUriList.size(); i++) {
                String filePath = getPath(inputFileUriList.get(i));
                bufferedInputStream = new BufferedInputStream(
                        new FileInputStream(new File(filePath)), BUFF_SIZE);
                final ZipEntry entry = new ZipEntry(i +  ".jpg");
                zipOutputStream.putNextEntry(entry);
                int len = 0;
                while ((len = bufferedInputStream.read(buffer, 0, BUFF_SIZE)) != -1) {
                    zipOutputStream.write(buffer, 0, len);
                }
                zipOutputStream.closeEntry();
            }
        } catch (FileNotFoundException e) {
            Log.e(TAG, "toZip():OutZipFileNotError=" +e.getMessage());
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } finally {
            DailyMotionUtils.close(zipOutputStream);
            DailyMotionUtils.close(bufferedInputStream);
        }

        File result = new File(outputFilePath);
        return result;
    }

    private String getPath(Uri uri) {
        ContentResolver contentResolver = mContext.getContentResolver();
        String[] columns = { MediaStore.Images.Media.DATA };
        Cursor cursor = contentResolver.query(uri, columns, null, null, null);
        cursor.moveToFirst();
        String path = cursor.getString(0);
        cursor.close();
        return path;
    }
}
