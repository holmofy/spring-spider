package io.github.holmofy.spider;

import lombok.Getter;
import lombok.NonNull;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;

import java.io.Serializable;
import java.net.URI;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.Objects;

import static java.util.Collections.singletonList;

@Getter
public class CrawlerRequest implements Serializable {

    public static final MediaType DEFAULT_CONTENT_TYPE = MediaType.TEXT_PLAIN;
    public static final Charset DEFAULT_CHARSET = StandardCharsets.UTF_8;
    private final HttpMethod method;

    private final URI uri;

    private final HttpHeaders headers;

    private final byte[] body;

    private CrawlerRequest(@NonNull HttpMethod method, @NonNull URI uri, HttpHeaders headers, byte[] body) {
        this.method = method;
        this.uri = uri;
        this.headers = Objects.requireNonNullElseGet(headers, HttpHeaders::new);
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

    public static CrawlerRequest parseRaw(String rawRequest) {
        rawRequest = Objects.requireNonNull(rawRequest, "rawRequest must not be null").trim();
        int bodyGapIndex = rawRequest.indexOf("\n\n");
        CrawlerRequestBuilder builder = CrawlerRequest.builder();
        String[] lines;
        String body = null;
        if (bodyGapIndex > 0) {
            body = rawRequest.substring(bodyGapIndex + 2);
            lines = rawRequest.substring(0, bodyGapIndex + 1).split("[\r\n]+");
        } else {
            lines = rawRequest.split("[\r\n]+");
        }
        String[] requestLine = lines[0].split(" ");
        builder.method(HttpMethod.valueOf(requestLine[0])).uri(URI.create(requestLine[1]));
        for (int i = 1; i < lines.length; i++) {
            String header = lines[i];
            int colon = header.indexOf(":");
            builder.header(header.substring(0, colon).trim(), header.substring(colon + 1).trim());
        }
        MediaType contentType = DEFAULT_CONTENT_TYPE;
        Charset charset = DEFAULT_CHARSET;
        if (builder.headers != null && body != null) {
            contentType = Objects.requireNonNullElse(builder.headers.getContentType(), contentType);
            charset = Objects.requireNonNullElse(contentType.getCharset(), charset);
        }
        if (body != null) {
            builder.body(body.getBytes(charset)).header(HttpHeaders.CONTENT_TYPE, contentType.toString());
        }
        return builder.build();
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

        public CrawlerRequestBuilder header(String key, String value) {
            this.headers = this.headers == null ? new HttpHeaders() : this.headers;
            headers.put(key, singletonList(value));
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
