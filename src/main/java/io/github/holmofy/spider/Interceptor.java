package io.github.holmofy.spider;

/**
 * 拦截器
 */
public interface Interceptor {

    /**
     * 发送请求前回调
     */
    void preRequest(CrawlerRequest request);

    /**
     * 发送请求前回调
     */
    void postResponse(CrawlerResponse response);

}
