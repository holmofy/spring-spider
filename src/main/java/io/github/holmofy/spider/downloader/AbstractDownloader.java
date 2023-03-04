package io.github.holmofy.spider.downloader;

import io.github.holmofy.spider.CrawlerRequest;
import io.github.holmofy.spider.CrawlerResponse;
import io.github.holmofy.spider.Downloader;
import io.github.holmofy.spider.Interceptor;
import lombok.AllArgsConstructor;

import java.util.List;

@AllArgsConstructor
public abstract class AbstractDownloader implements Downloader {

    protected DownloaderConfig config;

    @Override
    public CrawlerResponse download(CrawlerRequest request) {
        List<Interceptor> interceptors = config.getInterceptors();
        if (interceptors != null) {
            interceptors.forEach(interceptor -> interceptor.preRequest(request));
        }
        CrawlerResponse resp = innerDownload(request);
        if (interceptors != null) {
            interceptors.forEach(interceptor -> interceptor.postResponse(resp));
        }
        return resp;
    }

    protected abstract CrawlerResponse innerDownload(CrawlerRequest crawlerRequest);
}
