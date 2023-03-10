package io.github.holmofy.spider.downloader;

public enum SimpleClient {

    /**
     * http url connection
     *
     * @see java.net.HttpURLConnection
     * @see org.springframework.http.client.SimpleClientHttpRequestFactory
     */
    URL_CONNECTION,

    /**
     * okhttp
     *
     * @see org.springframework.http.client.OkHttp3ClientHttpRequestFactory
     */
    OK_HTTP,

    /**
     * apache http client
     *
     * @see org.springframework.http.client.HttpComponentsClientHttpRequestFactory
     */
    HTTP_COMPONENTS;


}
