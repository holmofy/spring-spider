package io.github.holmofy.spider.downloader;

import com.microsoft.playwright.Browser;
import com.microsoft.playwright.BrowserType.LaunchOptions;
import com.microsoft.playwright.Playwright;
import com.microsoft.playwright.Playwright.CreateOptions;
import lombok.Getter;
import lombok.experimental.Delegate;

public class BrowserCrawler implements AutoCloseable {

    @Getter
    private final BrowserCrawlerType browserType;
    private final Playwright playwright;
    @Delegate(excludes = AutoCloseable.class)
    private final Browser browser;

    public BrowserCrawler(BrowserCrawlerType browserType) {
        this(browserType, null, null);
    }

    public BrowserCrawler(BrowserCrawlerType browserType, LaunchOptions launchOptions) {
        this(browserType, null, launchOptions);
    }

    public BrowserCrawler(BrowserCrawlerType browserType, CreateOptions options, LaunchOptions launchOptions) {
        this.browserType = browserType;
        this.playwright = Playwright.create(options);
        this.browser = browserType.launch(playwright, launchOptions);
    }

    @Override
    public void close() {
        browser.close();
        playwright.close();
    }
}
