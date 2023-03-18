package io.github.holmofy.spider;

import com.alibaba.fastjson2.JSONPath;
import com.alibaba.fastjson2.JSONReader;
import org.junit.Test;

public class JsonPathTest {

    @Test
    public void test_jsonPath() {
        String json = """
                {
                   "a":"b"
                }
                """;
        JSONReader reader = JSONReader.of(json);
        String a1 = (String) JSONPath.of("$.a").extract(reader);
        String a2 = (String) JSONPath.of("$.a").extract(reader);
        System.out.println(a1);
        System.out.println(a2);
    }
}
