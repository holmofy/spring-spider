package io.github.holmofy.spider.downloader;

import io.github.holmofy.spider.Downloader;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.http.client.OkHttp3ClientHttpRequestFactory;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.util.ClassUtils;

public class DownloaderBuilder {

    /**
     * 简单的网络请求
     *
     * @return SimpleHttpDownloader
     */
    public Downloader simple() {
        ClassLoader classLoader = DownloaderBuilder.class.getClassLoader();
        ClientHttpRequestFactory requestFactory;
        if (ClassUtils.isPresent("okhttp3.OkHttpClient", classLoader)) {
            requestFactory = new OkHttp3ClientHttpRequestFactory();
        } else if (ClassUtils.isPresent("org.apache.hc.client5.http.classic.HttpClient", classLoader)) {
            requestFactory = new HttpComponentsClientHttpRequestFactory();
        } else {
            requestFactory = new SimpleClientHttpRequestFactory();
        }
        return simpleBuilder()
                .config(DownloaderConfig.DEFAULT)
                .requestFactory(requestFactory)
                .build();
    }

    public Downloader playwright() {
        return playwright(DownloaderConfig.DEFAULT);
    }

    /**
     * microsoft/playwright浏览器，适合反爬虫措施较好的网站
     *
     * @return PlaywrightDownloader
     */
    public Downloader playwright(DownloaderConfig config) {
        return new PlaywrightDownloader(config);
    }

    public SimpleHttpDownloader.SimpleHttpDownloaderBuilder simpleBuilder() {
        return SimpleHttpDownloader.builder();
    }

}
