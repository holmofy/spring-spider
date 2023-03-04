package io.github.holmofy.spider.downloader;

import io.github.holmofy.spider.Interceptor;
import lombok.Data;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Data
public class DownloaderConfig {

    public static final String DEFAULT_USER_AGENT = "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/108.0.0.0 Safari/537.36 Edg/108.0.1462.54";

    public static final DownloaderConfig DEFAULT = buildDefault();

    private static DownloaderConfig buildDefault() {
        DownloaderConfig config = new DownloaderConfig();
        HttpHeaders headers = new HttpHeaders();
        headers.put(HttpHeaders.USER_AGENT, Collections.singletonList(DEFAULT_USER_AGENT));
        headers.put(HttpHeaders.ACCEPT, Collections.singletonList(MediaType.ALL_VALUE));
        config.setHeaders(headers);
        return config;
    }

    private int retryCount;

    private HttpHeaders headers = HttpHeaders.EMPTY;

    private List<Interceptor> interceptors;

    static Map<String, String> buildHeaderMap(DownloaderConfig downloaderConfig, HttpHeaders headers) {
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
