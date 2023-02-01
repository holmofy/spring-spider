package io.github.holmofy.spider;

import lombok.Getter;
import lombok.NonNull;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;

import java.io.Serializable;
import java.net.URI;
import java.util.Map;

import static java.util.Collections.singletonList;

@Getter
public class CrawlerRequest implements Serializable {

    private final HttpMethod method;

    private final URI uri;

    private final HttpHeaders headers;

    private final byte[] body;

    private CrawlerRequest(@NonNull HttpMethod method, @NonNull URI uri, HttpHeaders headers, byte[] body) {
        this.method = method;
        this.uri = uri;
        this.headers = headers;
        this.body = body;
    }

    public static CrawlerRequestBuilder get(String url) {
        return CrawlerRequest.builder().method(HttpMethod.GET).uri(URI.create(url));
    }

    public static CrawlerRequestBuilder post(String url) {
        return CrawlerRequest.builder().method(HttpMethod.POST).uri(URI.create(url));
    }

    public static CrawlerRequestBuilder delete(String url) {
        return CrawlerRequest.builder().method(HttpMethod.DELETE).uri(URI.create(url));
    }

    public static CrawlerRequestBuilder put(String url) {
        return CrawlerRequest.builder().method(HttpMethod.PUT).uri(URI.create(url));
    }

    public static CrawlerRequestBuilder builder() {
        return new CrawlerRequestBuilder();
    }

    public static class CrawlerRequestBuilder {
        private HttpMethod method = HttpMethod.GET;
        private URI uri;
        private HttpHeaders headers;
        private byte[] body;

        private CrawlerRequestBuilder() {
        }

        public CrawlerRequestBuilder method(@NonNull HttpMethod method) {
            this.method = method;
            return this;
        }

        public CrawlerRequestBuilder uri(@NonNull URI uri) {
            this.uri = uri;
            return this;
        }

        public CrawlerRequestBuilder headers(HttpHeaders headers) {
            this.headers = headers;
            return this;
        }

        public CrawlerRequestBuilder headers(Map<String, String> headers) {
            this.headers = this.headers == null ? new HttpHeaders() : this.headers;
            headers.forEach((k, v) -> this.headers.put(k, singletonList(v)));
            return this;
        }

        public CrawlerRequestBuilder body(byte[] body) {
            this.body = body;
            return this;
        }

        public CrawlerRequest build() {
            return new CrawlerRequest(method, this.uri, this.headers, this.body);
        }
    }
}
