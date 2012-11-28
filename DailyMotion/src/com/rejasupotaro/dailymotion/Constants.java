package com.rejasupotaro.dailymotion;

public final class Constants {
    public static final boolean PRODUCTION = false;
    public static final String DOMAIN = PRODUCTION ? "rejasupotaro.com" : "192.168.3.3";
    public static final int PORT = 3000;
    public static final String APP_SITE_URL = "http://" + DOMAIN + ":" + PORT;
    public static final String API_GENERATE_GIF_URL = APP_SITE_URL + "/api/convert";

}
