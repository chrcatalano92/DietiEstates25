package com.demo.dietiestates25.support_classes.retrofit_utility;

import android.content.Context;

import androidx.annotation.NonNull;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class IpAddressReader {
    private IpAddressReader(){}

    @NonNull
    public static String readIpAddressFromFile(@NonNull Context context) {
        try(InputStream is = context.getAssets().open("ip_address.txt");
            InputStreamReader isr = new InputStreamReader(is);
            BufferedReader reader= new BufferedReader(isr)
        ) {
            StringBuilder content = new StringBuilder();
            char[] buffer = new char[1024];
            int bytesRead;

            while((bytesRead = reader.read(buffer)) != -1) {
                content.append(buffer, 0, bytesRead);
            }

            String ip = content.toString().trim();

            if(isValidIpAddress(ip)) {
                return ip;
            }
            else {
                return getFallbackIp();
            }
        }
        catch(IOException e) {
            return getFallbackIp();
        }
    }

    private static boolean isValidIpAddress(@NonNull String ip) { return ip.matches("^\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}$"); }

    @NonNull
    private static String getFallbackIp() { return "192.168.1.1"; }
}
