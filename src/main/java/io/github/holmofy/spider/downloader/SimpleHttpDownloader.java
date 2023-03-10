package io.github.holmofy.spider.downloader;

import io.github.holmofy.spider.CrawlerRequest;
import io.github.holmofy.spider.CrawlerResponse;
import lombok.SneakyThrows;
import org.springframework.http.HttpStatus;
import org.springframework.http.client.*;
import org.springframework.util.CollectionUtils;

import java.io.IOException;

public class SimpleHttpDownloader extends AbstractDownloader {

    protected ClientHttpRequestFactory requestFactory;

    private SimpleHttpDownloader(DownloaderConfig config, ClientHttpRequestFactory requestFactory) {
        super(config);
        this.requestFactory = requestFactory;
    }

    @Override
    @SneakyThrows
    protected CrawlerResponse innerDownload(CrawlerRequest r) {
        ClientHttpRequest chq = this.createRequest(r);
        try (ClientHttpResponse response = chq.execute()) {
            return toResponse(chq, response);
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
                .headers(response.getHeaders())
                .body(response.getBody().readAllBytes())
                .realUrl(request.getURI())
                .build();
    }

    public static class SimpleHttpDownloaderBuilder {
        private DownloaderConfig config;
        private ClientHttpRequestFactory requestFactory;

        private SimpleHttpDownloaderBuilder() {
        }

        public SimpleHttpDownloader.SimpleHttpDownloaderBuilder config(final DownloaderConfig config) {
            this.config = config;
            return this;
        }

        public SimpleHttpDownloader.SimpleHttpDownloaderBuilder requestFactory(final ClientHttpRequestFactory requestFactory) {
            this.requestFactory = requestFactory;
            return this;
        }

        public SimpleHttpDownloader.SimpleHttpDownloaderBuilder requestFactory(SimpleClient clientType) {
            if (clientType == SimpleClient.OK_HTTP) {
                this.requestFactory = new OkHttp3ClientHttpRequestFactory();
            } else if (clientType == SimpleClient.HTTP_COMPONENTS) {
                this.requestFactory = new HttpComponentsClientHttpRequestFactory();
            } else {
                this.requestFactory = new SimpleClientHttpRequestFactory();
            }
            return this;
        }

        public SimpleHttpDownloader build() {
            return new SimpleHttpDownloader(this.config, this.requestFactory);
        }
    }

    public static SimpleHttpDownloader.SimpleHttpDownloaderBuilder builder() {
        return new SimpleHttpDownloader.SimpleHttpDownloaderBuilder();
    }
}
