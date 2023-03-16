package io.github.holmofy.spider;

import com.google.gson.Gson;
import com.jayway.jsonpath.Configuration;
import com.jayway.jsonpath.JsonPath;
import com.jayway.jsonpath.ReadContext;
import com.jayway.jsonpath.spi.json.GsonJsonProvider;
import com.jayway.jsonpath.spi.mapper.GsonMappingProvider;
import lombok.*;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathFactory;
import java.io.ByteArrayInputStream;
import java.io.Serializable;
import java.lang.reflect.Type;
import java.net.URI;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

@Builder
public class CrawlerResponse implements Serializable {

    public static final Charset DEFAULT_CHARSET = StandardCharsets.UTF_8;

    @Setter
    private static Gson gson = new Gson();
    @Setter
    private static Configuration configuration = Configuration.builder()
            .jsonProvider(new GsonJsonProvider(gson))
            .mappingProvider(new GsonMappingProvider(gson))
            .build();

    @Getter
    @NonNull
    private final HttpStatus status;

    @Getter
    @NonNull
    private final String statusText;

    @Getter
    private final HttpHeaders headers;

    @Getter
    private final byte[] body;

    /**
     * 请求可能被重定向了，这个字段是响应请求的真实服务器url
     */
    @Getter
    private transient URI realUrl;

    private transient ReadContext jsonPath;

    private transient Document jsoup;

    private transient XPath xPath;

    public String body() {
        return new String(body, DEFAULT_CHARSET);
    }

    public String body(Charset charset) {
        return new String(body, charset);
    }

    public <T> T json(Type type) {
        return gson.fromJson(body(), type);
    }

    public ReadContext jsonPath() {
        return jsonPath(DEFAULT_CHARSET);
    }

    public ReadContext jsonPath(Charset charset) {
        if (jsonPath == null) {
            jsonPath = JsonPath.parse(body(charset), configuration);
        }
        return jsonPath;
    }

    public Document jsoup() {
        return jsoup(DEFAULT_CHARSET);
    }

    public Document jsoup(Charset charset) {
        if (jsoup == null) {
            jsoup = Jsoup.parse(body(charset), realUrl.toString());
        }
        return jsoup;
    }

    public XPath xPath() {
        return xPath(DEFAULT_CHARSET);
    }

    @SneakyThrows
    public XPath xPath(Charset charset) {
        if (xPath == null) {
            DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = builderFactory.newDocumentBuilder();
            String s = new String(body, charset);
            xPath = new XPath(builder.parse(new ByteArrayInputStream(body)));
        }
        return xPath;
    }

    public static class XPath {

        private final org.w3c.dom.Document dom;
        private final javax.xml.xpath.XPath xpath;

        public XPath(org.w3c.dom.Document dom) {
            this.dom = dom;
            this.xpath = XPathFactory.newInstance().newXPath();
        }

        @SneakyThrows
        public NodeList select(String expression) {
            return (NodeList) xpath.evaluate(expression, dom, XPathConstants.NODESET);
        }

        @SneakyThrows
        public Node selectFirst(String expression) {
            return (Node) xpath.evaluate(expression, dom, XPathConstants.NODE);
        }

    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder().append(status.value()).append(" ").append(statusText).append("\n");
        headers.forEach((k, v) -> {
            builder.append(k).append(": ");
            for (String s : v) {
                builder.append(s).append(',');
            }
            builder.deleteCharAt(builder.length() - 1).append('\n');
        });
        builder.append('\n').append(new String(body));
        return builder.toString();
    }
}
