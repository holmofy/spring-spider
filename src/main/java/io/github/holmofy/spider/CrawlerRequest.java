package io.github.holmofy.spider;

import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;

import java.io.Serializable;
import java.net.URI;

@Getter
@Builder
public class CrawlerRequest implements Serializable {

    @NonNull
    @Builder.Default
    private final HttpMethod method = HttpMethod.GET;

    @NonNull
    private final URI uri;

    private final HttpHeaders headers;

    private final byte[] body;

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

}
