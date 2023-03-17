package io.github.holmofy.spider.utils;

import org.springframework.http.HttpHeaders;

public class HttpParser {

    public static HttpHeaders parseHeader(String lines) {
        return parseHeader(lines.split("\n"));
    }

    public static HttpHeaders parseHeader(String[] lines) {
        return parseHeader(lines, 0, lines.length);
    }

    public static HttpHeaders parseHeader(String[] lines, int startLineNumInclude, int endLineNumExclude) {
        HttpHeaders headers = new HttpHeaders();
        for (int i = startLineNumInclude; i < endLineNumExclude; i++) {
            String header = lines[i];
            int colon = header.indexOf(":");
            headers.add(header.substring(0, colon).trim(), header.substring(colon + 1).trim());
        }
        return headers;
    }
}
