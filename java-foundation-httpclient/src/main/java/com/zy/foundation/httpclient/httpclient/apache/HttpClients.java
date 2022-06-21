package com.zy.foundation.httpclient.httpclient.apache;

import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.StatusLine;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.apache.http.HttpStatus.SC_OK;

@Slf4j
public class HttpClients {

    /**
     * GET 请求简单封装
     * @param client
     * @param url
     * @return
     */
    public static String doGet(CloseableHttpClient client, String url) {
        try {
            // 发送get请求
            HttpGet request = new HttpGet(url);
            HttpResponse response = client.execute(request);
            // 请求发送成功，并得到响应
            if (response.getStatusLine().getStatusCode() == SC_OK) {
                // 读取服务器返回过来的json字符串数据
                return EntityUtils.toString(response.getEntity());
            } else {
                log.error("response:" + response.getStatusLine().getStatusCode() + "(" + url + ")");
            }
        } catch (Exception e) {
            log.error("failed to get url:{}.", url, e);
        }
        return null;
    }

    /**
     * post请求(用于key-value格式的参数)
     */
    public static String doPost(CloseableHttpClient client, String url, Map params) {
        BufferedReader in;
        try {
            // 实例化HTTP方法
            HttpPost request = new HttpPost();
            request.setURI(new URI(url));

            // 设置参数
            List<NameValuePair> nvps = new ArrayList<>();
            for (Object o : params.keySet()) {
                String name = (String) o;
                String value = String.valueOf(params.get(name));
                nvps.add(new BasicNameValuePair(name, value));
            }
            request.setEntity(new UrlEncodedFormEntity(nvps, HTTP.UTF_8));
            HttpResponse response = client.execute(request);
            int code = response.getStatusLine().getStatusCode();
            // 请求成功
            if (code == SC_OK) {
                in = new BufferedReader(new InputStreamReader(response.getEntity().getContent(), StandardCharsets.UTF_8));
                StringBuilder sb = new StringBuilder();
                String line;
                String nL = System.getProperty("line.separator");
                while ((line = in.readLine()) != null) {
                    sb.append(line).append(nL);
                }
                in.close();
                return sb.toString();
            }
        } catch (Exception e) {
            log.error("failed to post url:{}", url, e);
        }
        return null;
    }

    /**
     * 适用于 请求和响应都是 json 的请求
     * @param httpclient
     * @param context
     * @param url
     * @param params
     * @return
     */
    public static String doPost(CloseableHttpClient httpclient, HttpClientContext context, String url, String params) {
        HttpPost httpPost = new HttpPost(url);
        // 创建 httpPost
        httpPost.setHeader("Accept", "application/json");
        httpPost.setHeader("Content-Type", "application/json");
        String charSet = "UTF-8";
        StringEntity entity = new StringEntity(params, charSet);
        httpPost.setEntity(entity);
        try (CloseableHttpResponse response = httpclient.execute(httpPost, context)) {
            StatusLine status = response.getStatusLine();
            int state = status.getStatusCode();
            if (state == SC_OK) {
                HttpEntity responseEntity = response.getEntity();
                return EntityUtils.toString(responseEntity);
            } else {
                log.error("response:" + state + "(" + url + ")");
            }
        } catch (Exception e) {
            log.error("failed to post url:{}.", url, e);
        }
        return null;
    }

}
