package com.zy.foundation.httpclient.httpclient.vertx;

import io.vertx.ext.web.client.WebClientOptions;
import io.vertx.rxjava3.core.Vertx;
import io.vertx.rxjava3.ext.web.client.WebClient;

public final class DefaultWebClient {
    private static final int CONNECTION_TIMEOUT_IN_MILLS = 60_000;
    private static final int IDLE_TIMEOUT_MILLS = 60_000;
    private static final int MAX_TOTAL_CONNECTION = 200;
    private static final WebClient WEB_CLIENT;
    private static final WebClientOptions OPTIONS;

    static {
        OPTIONS = new WebClientOptions()
                .setMaxPoolSize(MAX_TOTAL_CONNECTION)
                .setConnectTimeout(CONNECTION_TIMEOUT_IN_MILLS)
                //.setSsl(false)
                // FIXME 这里信任所有主机
                .setTrustAll(true)
                .setIdleTimeout(IDLE_TIMEOUT_MILLS)
                .setKeepAlive(true);
        WEB_CLIENT = WebClient.create(Vertx.vertx(), OPTIONS);
    }

    public static WebClient getInstance() {
        return WEB_CLIENT;
    }

}
