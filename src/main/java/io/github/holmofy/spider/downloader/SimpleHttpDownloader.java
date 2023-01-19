package io.github.holmofy.spider.downloader;

import io.github.holmofy.spider.CrawlerRequest;
import io.github.holmofy.spider.CrawlerResponse;
import io.github.holmofy.spider.Downloader;
import lombok.Builder;
import lombok.SneakyThrows;
import org.springframework.http.HttpStatus;
import org.springframework.http.client.ClientHttpRequest;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.util.CollectionUtils;

import java.io.IOException;

@Builder
public class SimpleHttpDownloader implements Downloader {

    protected DownloaderConfig config;

    protected ClientHttpRequestFactory requestFactory;

    @SneakyThrows
    public CrawlerResponse download(CrawlerRequest request) {
        ClientHttpRequest r = this.createRequest(request);
        try (ClientHttpResponse response = r.execute()) {
            return toResponse(r, response);
        }
    }

    protected ClientHttpRequest createRequest(CrawlerRequest request) throws IOException {
        ClientHttpRequest r = requestFactory.createRequest(request.getUri(), request.getMethod());
        if (config.getHeaders() != null) {
            r.getHeaders().putAll(config.getHeaders());
        }
        if (!CollectionUtils.isEmpty(request.getHeaders())) {
            r.getHeaders().putAll(request.getHeaders());
        }
        if (request.getBody() != null) {
            r.getBody().write(request.getBody());
        }
        return r;
    }

    @SneakyThrows
    protected CrawlerResponse toResponse(ClientHttpRequest request, ClientHttpResponse response) {
        return CrawlerResponse.builder()
                .status(HttpStatus.valueOf(response.getStatusCode().value()))
                .statusText(response.getStatusText())
                .headers(response.getHeaders().toSingleValueMap())
                .body(response.getBody().readAllBytes())
                .realUrl(request.getURI())
                .build();
    }
}