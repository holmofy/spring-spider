package io.github.holmofy.spider.downloader;

import com.microsoft.playwright.*;
import io.github.holmofy.spider.CrawlerRequest;
import io.github.holmofy.spider.CrawlerResponse;
import io.github.holmofy.spider.Downloader;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.util.StringUtils;

import java.net.URI;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Slf4j
@AllArgsConstructor
public class PlaywrightDownloader implements Downloader {

    DownloaderConfig downloaderConfig;

    @Override
    public CrawlerResponse download(CrawlerRequest request) {
        Playwright playwright = Playwright.create();
        Browser browser = playwright.webkit().launch();
        try (playwright; browser) {
            BrowserContext context = browser.newContext();
            context.storageState();
            Page page = context.newPage();
            String url = request.getUri().toString();
            CrawlerResponse.CrawlerResponseBuilder response = CrawlerResponse.builder();
            AtomicBoolean isOk = new AtomicBoolean(false);
            page.onResponse(r -> {
                log.debug("---> {} {}\n<=== {} {}", r.request().method(), r.request().url(), r.status(), r.statusText());
                if (Objects.equals(r.url(), url)) {
                    isOk.set(r.ok());
                    response.status(HttpStatus.valueOf(r.status()))
                            .statusText(r.statusText())
                            .headers(r.allHeaders())
                            .realUrl(URI.create(r.url()))
                            .body(r.body());
                }
            });
            page.route(url, route -> {
                Route.ResumeOptions options = new Route.ResumeOptions();
                options.setMethod(request.getMethod().name());
                options.setHeaders(buildHeaderMap(downloaderConfig, request.getHeaders()));
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

    private Map<String, String> buildHeaderMap(DownloaderConfig downloaderConfig, HttpHeaders headers) {
        HttpHeaders defaultHeaders = downloaderConfig == null ? HttpHeaders.EMPTY : downloaderConfig.getHeaders();
        if (headers == null) {
            return defaultHeaders.entrySet().stream().collect(Collectors.toMap(
                    Map.Entry::getKey,
                    e -> String.join(";", e.getValue())
            ));
        }
        return Stream.concat(defaultHeaders.entrySet().stream(), headers.entrySet().stream()).collect(Collectors.toMap(
                Map.Entry::getKey,
                e -> String.join(";", e.getValue()),
                (dh1, h2) -> h2
        ));
    }
}
