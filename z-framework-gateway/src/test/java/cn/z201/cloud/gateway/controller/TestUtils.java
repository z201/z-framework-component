package cn.z201.cloud.gateway.controller;

import com.google.gson.Gson;
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


}
