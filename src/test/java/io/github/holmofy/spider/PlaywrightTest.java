package io.github.holmofy.spider;

import io.github.holmofy.spider.downloader.DownloaderConfig;
import io.github.holmofy.spider.downloader.PlaywrightDownloader;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.http.HttpStatus;


public class PlaywrightTest {

    @Test
    public void test_download() {
        DownloaderConfig config = new DownloaderConfig();
        config.setRetryCount(1);
        PlaywrightDownloader downloader = new PlaywrightDownloader(config);
        CrawlerResponse response = downloader.download(CrawlerRequest.get("https://www.baidu.com/").build());
        System.out.println(response.getStatus());
        System.out.println(response.getHeaders());
        System.out.println(response.body());
        Assert.assertEquals(HttpStatus.OK, response.getStatus());
    }
}
