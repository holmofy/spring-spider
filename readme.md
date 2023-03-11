[![Build Status(https://github.com/holmofy/spring-spider/actions/workflows/package.yaml/badge.svg)](https://github.com/holmofy/spring-spider/actions/workflows/package.yaml/badge.svg)](https://repo1.maven.org/maven2/io/github/holmofy/spring-spider)
![coverage](https://github.com/holmofy/spring-spider/actions/workflows/coverage.yaml/badge.svg)

Spring Spider App Utility Library.

# feature

* [x] support jsonpath & jsoup & xpath
* [x] Integrate [playwright](https://github.com/microsoft/playwright-java) to support pages included js, such as
  single-page application
* [x] support raw http message

## how to use

0. Requirements: **spring boot 3.0, java17**

1. add dependency

```xml

<dependency>
    <groupId>io.github.holmofy</groupId>
    <artifactId>spring-spider</artifactId>
    <version>1.2.1</version>
</dependency>
```

2. support jsonpath & Jsoup & Xpath

```java
public class Example {
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
```

## playwright

```java
public class Example {
    public static void main(String[] args) {
        Downloader playwright = Downloader.builder().playwright();
        //...
    }
}
```

## raw http request

```java
import io.github.holmofy.spider.CrawlerResponse;
import io.github.holmofy.spider.Downloader;

public class Example {
    public static void main(String[] args) {
        CrawlerRequest request = CrawlerRequest.parseRaw("""
                POST https://login.example.com/api/users/login
                Accept: application/json, text/plain, */*
                Content-Type: application/x-www-form-urlencoded;charset=UTF-8
                Cookie: 0bd17c6216775852668436416eaee18367962376820602ec6d9cbff1f07b4c
                User-Agent: Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36
                      
                user=admin&password=123456
                """);
        CrawlerResponse response = Downloader.builder().simple().download(request);
        // ...
    }
}
```
