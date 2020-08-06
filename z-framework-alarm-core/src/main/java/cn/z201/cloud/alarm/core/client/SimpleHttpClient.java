package cn.z201.cloud.alarm.core.client;

import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;

@Slf4j
public class SimpleHttpClient {

    private static final String DEFAULT_CONTENT_TYPE = "application/json; charset=utf-8";

    private static final String UTF_8 = "utf-8";

    public static <K> String doPost(String url, K jsonParam, Map<String, String> headers)
            throws IOException {
        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpPost post = new HttpPost(url);
        headers.forEach((x, y) -> post.setHeader(x, y));
        StringEntity entity;
        CloseableHttpResponse response = null;
        try {
            if (jsonParam != null) {
                Gson gson = new Gson();
                String jsonStr = gson.toJson(jsonParam);
                entity = new StringEntity(jsonStr, Charset.forName(UTF_8));
                entity.setContentType(DEFAULT_CONTENT_TYPE);
                post.setEntity(entity);
            }
            response = httpClient.execute(post);
            String respStr = EntityUtils.toString(response.getEntity(), Charset.forName(UTF_8));
            response.close();
            return respStr;
        } finally {
            if (null != response) {
                response.close();
            }
        }
    }

    public static <T, K> T post(String url, K jsonParam, Class<T> clazz) {
        return post(url, jsonParam, clazz, null);
    }

    public static <T, K> T post(String url, K jsonParam, Class<T> clazz, Map<String, String> header) {
        String json = null;
        header = header == null ? new HashMap<>() : header;
        if (!header.containsKey(HTTP.CONTENT_TYPE)) {
            header.put(HTTP.CONTENT_TYPE, DEFAULT_CONTENT_TYPE);
        }
        try {
            json = doPost(url, jsonParam, header);
        } catch (IOException e) {
            log.error("钉钉通知失败拉 json {} e.message {} ", json, e.getMessage());
        }
        Gson gson = new Gson();
        T res = json == null ? null : gson.fromJson(json, clazz);
        return res;
    }

}
