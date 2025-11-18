package com.demo.dietiestates25.support_classes.retrofit_utility;

public class UrlUtils {
    private static final String BASE_URL = RetrofitClient.getBaseUrl();
    public static final String LOCALHOST = "http://localhost:8080/";

    private UrlUtils(){}

    public static String changeImageUrl(String imgUrl) {
        if(imgUrl == null) return null;

        if(imgUrl.contains(LOCALHOST)) { return imgUrl.replace(LOCALHOST, BASE_URL); }

        if(imgUrl.startsWith("/")) { return BASE_URL + imgUrl; }

        return imgUrl;
    }
}
