package io.github.holmofy.spider;

/**
 * 拦截器
 */
public interface Interceptor {

    /**
     * 发送请求前回调
     */
    default void preRequest(CrawlerRequest request) {
    }

    /**
     * 发送请求前回调
     */
    default void postResponse(CrawlerResponse response) {
    }

}
