package io.github.holmofy.spider;

import com.jayway.jsonpath.JsonPath;
import com.jayway.jsonpath.ReadContext;
import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;
import lombok.SneakyThrows;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.http.HttpStatus;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathFactory;
import java.io.ByteArrayInputStream;
import java.io.Serializable;
import java.net.URI;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Map;

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
    private final Map<String, String> headers;

    @Getter
    private final byte[] body;

    /**
     * 请求可能被重定向了，这个字段是响应请求的真实服务器url
     */
    @Getter
    private volatile URI realUrl;

    private volatile ReadContext jsonPath;

    private volatile Document jsoup;

    private volatile XPath xPath;

    public String body() {
        return new String(body, DEFAULT_CHARSET);
    }

    public String body(Charset charset) {
        return new String(body, charset);
    }

    public ReadContext jsonPath() {
        return jsonPath(DEFAULT_CHARSET);
    }

    public ReadContext jsonPath(Charset charset) {
        if (jsonPath == null) {
            jsonPath = JsonPath.parse(body(charset));
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

}
