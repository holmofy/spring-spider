package io.github.holmofy.spider.downloader;

import com.microsoft.playwright.*;
import io.github.holmofy.spider.CrawlerRequest;
import io.github.holmofy.spider.CrawlerResponse;
import io.github.holmofy.spider.Downloader;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.util.MultiValueMapAdapter;
import org.springframework.util.StringUtils;

import java.net.URI;
import java.util.Collections;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;

@Slf4j
@AllArgsConstructor
public class PlaywrightDownloader implements Downloader {

    DownloaderConfig downloaderConfig;

    @Override
    public CrawlerResponse download(CrawlerRequest request) {
        try (Playwright playwright = Playwright.create();
             Browser browser = playwright.webkit().launch();
             BrowserContext context = browser.newContext()) {
            Page page = context.newPage();
            String url = request.getUri().toString();
            CrawlerResponse.CrawlerResponseBuilder response = CrawlerResponse.builder();
            AtomicBoolean isOk = new AtomicBoolean(false);
            page.onResponse(r -> {
                log.trace("\n---> {} {}\n<=== {} {}",
                        r.request().method(), r.request().url(),
                        r.status(), r.statusText());
                if (Objects.equals(r.url(), url)) {
                    isOk.set(r.ok());
                    response.status(HttpStatus.valueOf(r.status()))
                            .statusText(r.statusText())
                            .headers(HttpHeaders.readOnlyHttpHeaders(new SingletonMultiValueMapAdapter(r.allHeaders())))
                            .realUrl(URI.create(r.url()))
                            .body(r.body());
                }
            });
            page.route(url, route -> {
                Route.ResumeOptions options = new Route.ResumeOptions();
                options.setMethod(request.getMethod().name());
                options.setHeaders(DownloaderConfig.buildHeaderMap(downloaderConfig, request.getHeaders()));
                options.setPostData(request.getBody());
                route.resume(options);
            });
            page.navigate(url);
            int retryCount = downloaderConfig == null ? 0 : downloaderConfig.getRetryCount();
            while (retryCount-- > 0) {
                if (isOk.get()) {
                    break;
                } else {
                    page.reload();
                }
            }
            String rawResponse = page.content();
            if (StringUtils.hasText(rawResponse)) {
                response.body(rawResponse.getBytes());
            }
            return response.build();
        }
    }

    private static class SingletonMultiValueMapAdapter extends MultiValueMapAdapter<String, String> {

        public SingletonMultiValueMapAdapter(Map<String, String> targetMap) {
            super(targetMap.entrySet().stream().collect(
                    Collectors.toMap(Entry::getKey,
                            e -> Collections.singletonList(e.getValue()))
            ));
        }
    }

}
