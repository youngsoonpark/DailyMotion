package com.rejasupotaro.dailymotion.test.api;

import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;

import org.apache.http.HttpException;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.mockito.InOrder;
import org.mockito.Mockito;

import android.test.AndroidTestCase;

import com.rejasupotaro.dailymotion.api.DailyMotionApiClient;

public class DailyMotionApiClientTest extends AndroidTestCase {

    public void testGetResponse_success() throws Exception {
        StatusLine mockStatusLine = mock(StatusLine.class);
        when(mockStatusLine.getStatusCode()).thenReturn(200);

        HttpResponse mockResponse = mock(HttpResponse.class);
        when(mockResponse.getStatusLine()).thenReturn(mockStatusLine);

        HttpClient mockHttpClient = mock(HttpClient.class);
        when(mockHttpClient.execute(Mockito.notNull(HttpUriRequest.class)))
        .thenReturn(mockResponse);

        HttpPost httpPost = new HttpPost("http://dummy.com");
        DailyMotionApiClient apiClient = new DailyMotionApiClient(getContext(), mockHttpClient);
        try {
            boolean result = apiClient.getResponse(mockHttpClient, httpPost);
            InOrder inOrder = inOrder(mockResponse);
            inOrder.verify(mockResponse).getEntity();
            inOrder.verify(mockResponse).getStatusLine();
            assertEquals(true, result);
        } catch (ClientProtocolException e) {
            fail(e.toString());
        } catch (IOException e) {
            fail(e.toString());
        }
    }

    public void testGetResponse_error() throws Exception {
        StatusLine mockStatusLine = mock(StatusLine.class);
        when(mockStatusLine.getStatusCode()).thenReturn(500);

        HttpResponse mockResponse = mock(HttpResponse.class);
        when(mockResponse.getStatusLine()).thenReturn(mockStatusLine);

        HttpClient mockHttpClient = mock(HttpClient.class);
        when(mockHttpClient.execute(Mockito.notNull(HttpUriRequest.class)))
        .thenReturn(mockResponse);

        HttpPost httpPost = new HttpPost("http://dummy.com");
        DailyMotionApiClient apiClient = new DailyMotionApiClient(getContext(), mockHttpClient);
        boolean result = false;
        try {
            result = apiClient.getResponse(mockHttpClient, httpPost);
            fail("HttpExceptionがthrowされていない");
        } catch (HttpException e) {
            assertFalse(result);
        }
    }
}
