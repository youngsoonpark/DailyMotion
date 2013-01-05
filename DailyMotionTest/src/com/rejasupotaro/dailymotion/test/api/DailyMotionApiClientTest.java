package com.rejasupotaro.dailymotion.test.api;

import static org.mockito.Matchers.anyInt;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

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
            inOrder.verify(mockResponse).getStatusLine();
            inOrder.verify(mockResponse).getEntity();
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

    public void testDrinkMockito() throws Exception {
        // mockでモックオブジェクトを作成する
        List<String> mockedList = mock(ArrayList.class);
        mockedList.add("foo");
        mockedList.clear();

        // whenで引数ごとの返り値を決められる
        when(mockedList.get(0)).thenReturn("first");
        when(mockedList.get(1)).thenThrow(new RuntimeException());
        assertEquals("first", mockedList.get(0));
        try {
            mockedList.get(1);
            fail("RuntimeExceptionがthrowされていない");
        } catch (RuntimeException e) {
        }

        // whenではanyInt()やanyString()やanyMap()のような指定の仕方もできる
        when(mockedList.get(anyInt())).thenReturn("element");
        assertEquals("element", mockedList.get(999));

        // verifyでモックオブジェクトが対象のメソッドを実行したか確認できる
        verify(mockedList).add("foo");
        verify(mockedList).clear();

        // verifyはメソッドの実行回数も確認することができる
        mockedList.add("bar");
        mockedList.add("bar");
        // mockedList.add("bar")が2回呼ばれたことを確認する
        verify(mockedList, times(2)).add("bar");
        // mockedList.add("bar")は1回も呼ばれなかったことを確認する
        verify(mockedList, never()).add("baz");

        // verifyはメソッドの実行順序も確認することができる
        mockedList.add("baz");
        mockedList.clear();
        InOrder inOrder = inOrder(mockedList);
        inOrder.verify(mockedList).add("baz");
        inOrder.verify(mockedList).clear();

        // spyで部分的にメソッドを置き換えることもできる
        List<String> spy = Mockito.spy(new ArrayList<String>());
        doReturn(100).when(spy).size();
        spy.add("foo"); // 実際のオブジェクトのメソッド呼び出し
        spy.size(); // => 100
    }
}
