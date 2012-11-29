package com.rejasupotaro.dailymotion.test.api;

import java.io.IOException;

import org.apache.http.HttpStatus;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;

import android.test.AndroidTestCase;

import com.rejasupotaro.dailymotion.api.DailyMotionApiClient;

public class DailyMotionApiClientTest extends AndroidTestCase {

    public void testGetResponseStatusLine() {
        HttpClient httpClient = new DefaultHttpClient();
        HttpPost httpPost = new HttpPost("http://google.com");
        
        DailyMotionApiClient apiClient = new DailyMotionApiClient(getContext());
        StatusLine statusLine = null;
        try {
            statusLine = apiClient.getResponseStatusLine(httpClient, httpPost);
        } catch (ClientProtocolException e) {
            fail(e.toString());
        } catch (IOException e) {
            fail(e.toString());
        }

        assertNotNull(statusLine);
        assertEquals(405, statusLine.getStatusCode());
    }

}
