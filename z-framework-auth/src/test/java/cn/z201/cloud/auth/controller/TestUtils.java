package cn.z201.cloud.auth.controller;

import cn.z201.cloud.auth.JwtTokenUtils;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import cn.z201.cloud.encrypt.core.dto.HttpBodyEncryptBodyDto;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.client.CookieStore;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.ByteArrayOutputStream;

/**
 * @author z201.coding@gmail.com
 **/
public class TestUtils {


    public static String post(String url, Object postData) throws Exception {
        CookieStore cookieStore = new BasicCookieStore();
        CloseableHttpClient httpclient = HttpClients.custom().setDefaultCookieStore(cookieStore).build();
        HttpPost httpPost = new HttpPost(url);
        httpPost.addHeader("Content-Type", "application/json");
        httpPost.addHeader("Accept", "application/json");
        httpPost.addHeader("Client-Business-Group-Source", "003");
        httpPost.addHeader("Client-Business-Source", "0031001");
        httpPost.addHeader("Client-Business-Activity-Source", "0");
        httpPost.addHeader("Client-Env-Source", "2");
        httpPost.addHeader("Client-Platform-Source", "java jdk");
        httpPost.addHeader("Client-Start-Time", String.valueOf(System.currentTimeMillis()));
        httpPost.addHeader("Client-Version-Source", "1.0.0");
        Gson gson = new Gson();
        String body = gson.toJson(postData);
        httpPost.setEntity(new StringEntity(body));
        CloseableHttpResponse response = httpclient.execute(httpPost);
        try {
            System.out.println(response.getStatusLine());
            // 读取返回信息
            HttpEntity entity = response.getEntity();
            ByteArrayOutputStream buf = new ByteArrayOutputStream();
            entity.writeTo(buf);
            EntityUtils.consume(entity);
            System.out.println(new String(buf.toByteArray(), "utf-8") + "#################");
            // 反序列化
        } finally {
            response.close();
        }
        Header[] headers = response.getAllHeaders();
        for (int i = 0; i < headers.length; i++) {
            if (headers[i].getName().equals("Set-Cookie")) {
                return headers[i].getValue();
            }
        }
        return null;
    }

    public static String postEncrypt(String url, Object postData) throws Exception {
        CookieStore cookieStore = new BasicCookieStore();
        CloseableHttpClient httpclient = HttpClients.custom().setDefaultCookieStore(cookieStore).build();
        HttpPost httpPost = new HttpPost(url);
        httpPost.addHeader("Content-Type", "application/json");
        httpPost.addHeader("Accept", "application/json");
        httpPost.addHeader("Client-Business-Group-Source", "003");
        httpPost.addHeader("Client-Business-Source", "0031001");
        httpPost.addHeader("Client-Business-Activity-Source", "0");
        httpPost.addHeader("Client-Env-Source", "2");
        httpPost.addHeader("Client-Platform-Source", "java jdk");
        httpPost.addHeader("Client-Start-Time", String.valueOf(System.currentTimeMillis()));
        httpPost.addHeader("Client-Version-Source", "1.0.0");
        Gson gson = new GsonBuilder().disableHtmlEscaping().create();
        String body = gson.toJson(postData);
        System.out.println(body);
        HttpBodyEncryptBodyDto httpBodyEncryptBodyDto = HttpBodyEncryptBodyDto.builder().data(body).build();
        httpBodyEncryptBodyDto.init();
        body = gson.toJson(httpBodyEncryptBodyDto);
        System.out.println(body);
        httpPost.setEntity(new StringEntity(body));
        CloseableHttpResponse response = httpclient.execute(httpPost);
        try {
            System.out.println(response.getStatusLine());
            // 读取返回信息
            HttpEntity entity = response.getEntity();
            ByteArrayOutputStream buf = new ByteArrayOutputStream();
            entity.writeTo(buf);
            EntityUtils.consume(entity);
            System.out.println(new String(buf.toByteArray(), "utf-8") + "#################");
            // 反序列化
        } finally {
            response.close();
        }
        Header[] headers = response.getAllHeaders();
        for (int i = 0; i < headers.length; i++) {
            if (headers[i].getName().equals(JwtTokenUtils.TOKEN_HEADER)) {
                System.out.println(JwtTokenUtils.TOKEN_PREFIX+headers[i].getValue());
                return headers[i].getValue();
            }
        }
        return null;
    }

    public static String postEncrypt(String url, String jwt, Object postData) throws Exception {
        CookieStore cookieStore = new BasicCookieStore();
        CloseableHttpClient httpclient = HttpClients.custom().setDefaultCookieStore(cookieStore).build();
        HttpPost httpPost = new HttpPost(url);
        httpPost.addHeader("Content-Type", "application/json");
        httpPost.addHeader("Accept", "application/json");
        httpPost.addHeader("Authorization", "Bearer " + jwt);
        httpPost.addHeader("Client-Business-Group-Source", "003");
        httpPost.addHeader("Client-Business-Source", "0031001");
        httpPost.addHeader("Client-Business-Activity-Source", "0");
        httpPost.addHeader("Client-Env-Source", "2");
        httpPost.addHeader("Client-Platform-Source", "java jdk");
        httpPost.addHeader("Client-Start-Time", String.valueOf(System.currentTimeMillis()));
        httpPost.addHeader("Client-Version-Source", "1.0.0");
        Gson gson = new GsonBuilder().disableHtmlEscaping().create();
        String body = gson.toJson(postData);
        System.out.println(body);
        HttpBodyEncryptBodyDto httpBodyEncryptBodyDto = HttpBodyEncryptBodyDto.builder().data(body).build();
        httpBodyEncryptBodyDto.init();
        body = gson.toJson(httpBodyEncryptBodyDto);
        System.out.println(body);
        httpPost.setEntity(new StringEntity(body));
        CloseableHttpResponse response = httpclient.execute(httpPost);
        try {
            System.out.println(response.getStatusLine());
            // 读取返回信息
            HttpEntity entity = response.getEntity();
            ByteArrayOutputStream buf = new ByteArrayOutputStream();
            entity.writeTo(buf);
            EntityUtils.consume(entity);
            System.out.println(new String(buf.toByteArray(), "utf-8") + "#################");
            // 反序列化
        } finally {
            response.close();
        }
        Header[] headers = response.getAllHeaders();
        for (int i = 0; i < headers.length; i++) {
            if (headers[i].getName().equals("Set-Cookie")) {
                return headers[i].getValue();
            }
        }
        return null;
    }

}
