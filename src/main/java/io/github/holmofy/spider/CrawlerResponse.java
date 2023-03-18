package io.github.holmofy.spider;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONPath;
import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;
import lombok.SneakyThrows;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.lang.Nullable;
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
import java.util.Objects;

@Builder
public class CrawlerResponse implements Serializable {

    public static final Charset DEFAULT_CHARSET = StandardCharsets.UTF_8;

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

    private transient JsonPath jsonPath;
    private transient Document jsoup;
    private transient XPath xPath;

    public String body() {
        return new String(body, Objects.requireNonNullElse(getContentCharset(), DEFAULT_CHARSET));
    }

    public String body(Charset charset) {
        return new String(body, charset);
    }

    public <T> T json(Type type) {
        return JSON.parseObject(body(), type);
    }

    public JsonPath jsonPath() {
        return jsonPath(Objects.requireNonNullElse(getContentCharset(), DEFAULT_CHARSET));
    }

    public JsonPath jsonPath(Charset charset) {
        if (jsonPath == null) {
            jsonPath = new JsonPath(body(charset));
        }
        return jsonPath;
    }

    public Document jsoup() {
        return jsoup(Objects.requireNonNullElse(getContentCharset(), DEFAULT_CHARSET));
    }

    public Document jsoup(Charset charset) {
        if (jsoup == null) {
            jsoup = Jsoup.parse(body(charset), realUrl.toString());
        }
        return jsoup;
    }

    public XPath xPath() {
        if (xPath == null) {
            xPath = new XPath(body);
        }
        return xPath;
    }

    @Nullable
    public Charset getContentCharset() {
        MediaType contentType = headers.getContentType();
        return contentType == null ? null : contentType.getCharset();
    }

    public static class JsonPath {
        private final String context;

        public JsonPath(String context) {
            this.context = context;
        }

        @SuppressWarnings("unchecked")
        public <T> T read(String path) {
            return (T) JSONPath.of(path).extract(context);
        }

        @SuppressWarnings("unchecked")
        public <T> T read(String path, Type type) {
            return (T) JSONPath.of(path, type).extract(context);
        }

    }

    public static class XPath {

        private final org.w3c.dom.Document dom;
        private final javax.xml.xpath.XPath xpath;

        @SneakyThrows
        public XPath(byte[] body) {
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = dbf.newDocumentBuilder();
            this.dom = builder.parse(new ByteArrayInputStream(body));
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

        @SneakyThrows
        public <T> T select(String expression, Class<T> type) {
            return xpath.compile(expression).evaluateExpression(dom, type);
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
        builder.append('\n').append(body());
        return builder.toString();
    }
}
