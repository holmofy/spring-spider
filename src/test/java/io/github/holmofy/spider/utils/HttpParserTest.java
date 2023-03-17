package io.github.holmofy.spider.utils;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.http.HttpHeaders;

public class HttpParserTest {

    @Test
    public void test_parse_http() {
        String[] lines = """
                Accept: */*
                Content-Encoding:gzip
                   Accept-Language: zh-cn
                Accept-Encoding: gzip, deflate, br""".split("\n");
        HttpHeaders headers = HttpParser.parseHeader(lines);
        Assert.assertEquals(headers.getFirst(HttpHeaders.ACCEPT), "*/*");
        Assert.assertEquals(headers.getFirst(HttpHeaders.CONTENT_ENCODING), "gzip");
        Assert.assertEquals(headers.getFirst(HttpHeaders.ACCEPT_LANGUAGE), "zh-cn");
        Assert.assertEquals(headers.getFirst(HttpHeaders.ACCEPT_ENCODING), "gzip, deflate, br");
    }
}
