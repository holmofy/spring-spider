package io.github.holmofy.spider;

import io.github.holmofy.spider.downloader.DownloaderBuilder;

/**
 * download page
 */
public interface Downloader {

    CrawlerResponse download(CrawlerRequest request);

    static DownloaderBuilder builder() {
        return new DownloaderBuilder();
    }

}
