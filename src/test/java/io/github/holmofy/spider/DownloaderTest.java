package io.github.holmofy.spider;

import org.junit.Assert;
import org.junit.Test;

import java.util.List;

public class DownloaderTest {

    @Test
    public void test_jsonpath() {
        Downloader downloader = Downloader.builder().simple();
        String current_user_url = downloader.download(CrawlerRequest.get("https://api.github.com/").build())
                .jsonPath()
                .read("$.current_user_url");
        Assert.assertEquals("https://api.github.com/user", current_user_url);
    }

    @Test
    public void test_jsoup() {
        Downloader downloader = Downloader.builder().simple();
        List<String> repos = downloader.download(CrawlerRequest.get("https://github.com/search?q=spider").build())
                .jsoup()
                .select("div.application-main ul.repo-list > li > div.mt-n1.flex-auto > div.d-flex > div > a")
                .eachText();
        Assert.assertEquals(10, repos.size());
        System.out.println(repos);
    }

    @Test
    public void test_xpath() {
        Downloader downloader = Downloader.builder().simple();
        String location = downloader.download(CrawlerRequest.get("https://www.douban.com/sitemap_index.xml").build())
                .xPath()
                .select("/sitemapindex/sitemap/loc")
                .item(0)
                .getTextContent();
        Assert.assertEquals("https://www.douban.com/sitemap.xml.gz", location);
    }

}
