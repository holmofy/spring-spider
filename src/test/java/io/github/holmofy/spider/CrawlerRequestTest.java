package io.github.holmofy.spider;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.http.HttpMethod;

public class CrawlerRequestTest {

    @Test
    public void test_parseRaw() {
        CrawlerRequest request = CrawlerRequest.parseRaw("""
                POST https://login.fenbi.com/api/users/loginV2?app=web&kav=12&av=80&version=3.0.0.0
                Accept: application/json, text/plain, */*
                Content-Type: application/x-www-form-urlencoded;charset=UTF-8
                Cookie: sid=255746; sensorsdata2015jssdkcross=%7B%22distinct_id%22%3A%2285756571%22%2C%22first_id%22%3A%2218592221baa71b-0a7375dab36bf8-4f69117f-1764000-18592221bab757%22%2C%22props%22%3A%7B%22%24latest_traffic_source_type%22%3A%22%E7%9B%B4%E6%8E%A5%E6%B5%81%E9%87%8F%22%2C%22%24latest_search_keyword%22%3A%22%E6%9C%AA%E5%8F%96%E5%88%B0%E5%80%BC_%E7%9B%B4%E6%8E%A5%E6%89%93%E5%BC%80%22%2C%22%24latest_referrer%22%3A%22%22%7D%2C%22%24device_id%22%3A%2218592221baa71b-0a7375dab36bf8-4f69117f-1764000-18592221bab757%22%7D; acw_tc=0bd17c6216775852668436416eaee18367962376820602ec6d9cbff1f07b4c
                Origin: https://www.fenbi.com
                Referer: https://www.fenbi.com/
                User-Agent: Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/110.0.0.0 Safari/537.36 Edg/110.0.1587.41
                                
                phone=18720232389&password=rOMcTIrSUFPP4z43fFq2Ag%2BPh2wl2m5WeFeQ4J0WLfPVx4z5cUopuravqVqfhMxbTAJFBVBkr08uP0OzCOXJIxcWfkcMtizoWj%2F%2BIwuxuzKCHyjfyfomN8AGp6nxG%2B2pnva37sPD%2BMPOy08zmX%2FoYpZ%2BH5ztv6k6d%2BE%2B9FvKNKo%3D
                """);

        Assert.assertEquals(request.getMethod(), HttpMethod.POST);
        Assert.assertEquals(request.getUri().toString(), "https://login.fenbi.com/api/users/loginV2?app=web&kav=12&av=80&version=3.0.0.0");
        Assert.assertEquals(request.getHeaders().size(), 6);
        Assert.assertEquals(new String(request.getBody()), "phone=18720232389&password=rOMcTIrSUFPP4z43fFq2Ag%2BPh2wl2m5WeFeQ4J0WLfPVx4z5cUopuravqVqfhMxbTAJFBVBkr08uP0OzCOXJIxcWfkcMtizoWj%2F%2BIwuxuzKCHyjfyfomN8AGp6nxG%2B2pnva37sPD%2BMPOy08zmX%2FoYpZ%2BH5ztv6k6d%2BE%2B9FvKNKo%3D");
    }

}
