package com.zhangyj.tools.common.utils;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

/**
 * @author zhangyj
 */
public class UrlUtils {

    public static BufferedReader getBufferedReader(String url) throws Exception {
        final URLConnection conn = new URL(url).openConnection();
        conn.setRequestProperty("user-agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/73.0.3683.103 Safari/537.36");
        conn.setConnectTimeout(10 * 1000);
        return new BufferedReader(new InputStreamReader(conn.getInputStream()));
    }
}
