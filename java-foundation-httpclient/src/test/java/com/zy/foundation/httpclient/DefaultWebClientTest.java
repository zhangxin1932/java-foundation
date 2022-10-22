package com.zy.foundation.httpclient;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.zy.foundation.httpclient.httpclient.apache.HttpClients;
import com.zy.foundation.httpclient.httpclient.apache.HttpProfile;
import com.zy.foundation.httpclient.httpclient.apache.SimpleHttpClientFactory;
import com.zy.foundation.httpclient.httpclient.vertx.DefaultWebClient;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.impl.client.CloseableHttpClient;

import java.nio.charset.StandardCharsets;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;

@Slf4j
public class DefaultWebClientTest {

    public static void main(String[] args) throws Exception {
        f4();
    }

    private static void f5() {
        String url = "https://mri.cts-mrp.eu/portal/v1/odata/ProductSearch?$filter=(UpdatedAt%20ge%202020-01-01T00%3A00%3A00%2B08%3A00)%20and%20(UpdatedAt%20le%202022-10-22T10%3A09%3A22%2B08%3A00)%20and%20(domainKey%20eq%20%27h%27)&$expand=activeSubstances,atcCodes,cms,documents,doseForms,rms,typeLevel,withdrawalReasons&$count=true&$skip=0&$top=10";
        CloseableHttpClient httpClient = SimpleHttpClientFactory.buildWithSSLOSecret(HttpProfile.getInstance());
        String body = HttpClients.doGet(httpClient, url);
        System.out.println(body);
    }

    private static void f4() {
        // 如果访问报错：java.net.UnknownHostException
        // 则可以在 C:\Windows\System32\drivers\etc\hosts 中构建 ip 到域名的映射，
        // 如： 192.124.251.50 mri.cts-mrp.eu
        String url = "https://mri.cts-mrp.eu/portal/v1/odata/ProductSearch?$filter=(UpdatedAt%20ge%202020-01-01T00%3A00%3A00%2B08%3A00)%20and%20(UpdatedAt%20le%202022-10-22T10%3A09%3A22%2B08%3A00)%20and%20(domainKey%20eq%20%27h%27)&$expand=activeSubstances,atcCodes,cms,documents,doseForms,rms,typeLevel,withdrawalReasons&$count=true&$skip=0&$top=";
        String body = DefaultWebClient.getInstance()
                .getAbs(url + 10)
                .putHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:96.0) Gecko/20100101 Firefox/96.0")
                .rxSend()
                .blockingGet()
                .bodyAsString(StandardCharsets.UTF_8.name());
        System.out.println(body);
    }

    /**
     * 阻塞获取同步结果
     */
    private static void f1() throws Exception {
        String body = DefaultWebClient.getInstance()
                .getAbs("https://cpapi.xinli001.com/cp/mservice/category/getCategoryScales?category=ff&pageVo.page=1&sortType=TIME&pageVo.pageSize=10&channelId=501")
                .putHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:96.0) Gecko/20100101 Firefox/96.0")
                .rxSend()
                .blockingGet()
                .bodyAsString(StandardCharsets.UTF_8.name());
        System.out.println(body);
    }

    /**
     * 阻塞获取同步结果: 借助 CompletableFuture
     * 注意观察 1. 和 2. 的打印顺序
     */
    private static void f2() throws Exception {
        CompletableFuture<String> future = async();
        nextSync(future);
    }

    /**
     * 非阻塞获取结果: 借助 CompletableFuture
     * 注意观察 1. 和 2. 的打印顺序
     */
    private static void f3() throws Exception {
        CompletableFuture<String> future = async();
        nextAsync(future);
    }

    private static void nextSync(CompletableFuture<String> async) throws Exception {
        System.out.println("1.-----------" + async.get());
        System.out.println("2.-----------");
    }

    private static void nextAsync(CompletableFuture<String> async) {
        async.whenComplete((result, error) -> {
            if (Objects.nonNull(error)) {
                log.error("failed to get result.", error);
                return;
            }
            JSONObject jsonObject = JSON.parseObject(result);
            System.out.println("1.async ----------------------> code: " + jsonObject.getString("code"));
        });
        System.out.println("2.-----------");
    }

    private static CompletableFuture<String> async() {
        CompletableFuture<String> future = new CompletableFuture<>();
        DefaultWebClient.getInstance()
                .getAbs("https://cpapi.xinli001.com/cp/mservice/category/getCategoryScales?category=ff&pageVo.page=1&sortType=TIME&pageVo.pageSize=10&channelId=501")
                .putHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:96.0) Gecko/20100101 Firefox/96.0")
                .rxSend()
                .subscribe((resp, error) -> {
                    if (Objects.isNull(error)) {
                        future.complete(resp.bodyAsString(StandardCharsets.UTF_8.name()));
                    } else {
                        future.completeExceptionally(error);
                    }
                });
        return future;
    }

}
