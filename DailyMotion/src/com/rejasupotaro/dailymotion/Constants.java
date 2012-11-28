package com.rejasupotaro.dailymotion;

public final class Constants {
    public static final boolean PRODUCTION = false;
    public static final String APP_SITE_URL = PRODUCTION ? "http://rejasupotaro.com:3000" : "http://192.168.3.3:3000";
    public static final String API_GENERATE_GIF_URL = APP_SITE_URL + "/api/convert";

}
