package io.github.holmofy.spider.downloader;

import com.microsoft.playwright.Browser;
import com.microsoft.playwright.BrowserType.LaunchOptions;
import com.microsoft.playwright.Playwright;

public enum BrowserCrawlerType {

    /**
     * @see Playwright#webkit()
     */
    webkit {
        @Override
        Browser launch(Playwright playwright, LaunchOptions options) {
            return playwright.webkit().launch(options);
        }
    },

    /**
     * @see Playwright#firefox() ()
     */
    firefox {
        @Override
        Browser launch(Playwright playwright, LaunchOptions options) {
            return playwright.firefox().launch(options);
        }
    },

    /**
     * @see Playwright#chromium()
     */
    chromium {
        @Override
        Browser launch(Playwright playwright, LaunchOptions options) {
            return playwright.chromium().launch(options);
        }
    };

    abstract Browser launch(Playwright playwright, LaunchOptions options);

}
