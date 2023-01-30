[![Build Status(https://github.com/holmofy/spring-spider/actions/workflows/package.yaml/badge.svg)](https://github.com/holmofy/spring-spider/actions/workflows/package.yaml/badge.svg)](https://repo1.maven.org/maven2/io/github/holmofy/spring-spider)

Spring Spider App Utility Library.

# road map

[x] support jsonpath & jsoup & xpath
[] Integrate playwright to support pages included js, such as single-page application

## how to use

1. add dependency
```xml
<dependency>
    <groupId>io.github.holmofy</groupId>
    <artifactId>spring-spider</artifactId>
    <version>1.0</version>
</dependency>
```
2. support jsonpath & Jsoup & Xpath
```java
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
```
