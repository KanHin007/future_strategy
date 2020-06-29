package com.hb.strategy.lawrence.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@ConfigurationProperties(prefix = "sina.crawler")
public class SinaConfig {


    private List<String> futureTypes;

    private String innerUrlPrefix;

    private String outerUrlPrefix;

    public List<String> getFutureTypes() {
        return futureTypes;
    }

    public void setFutureTypes(List<String> futureTypes) {
        this.futureTypes = futureTypes;
    }


    public String getInnerUrlPrefix() {
        return innerUrlPrefix;
    }

    public void setInnerUrlPrefix(String innerUrlPrefix) {
        this.innerUrlPrefix = innerUrlPrefix;
    }


    public String getOuterUrlPrefix() {
        return outerUrlPrefix;
    }

    public void setOuterUrlPrefix(String outerUrlPrefix) {
        this.outerUrlPrefix = outerUrlPrefix;
    }
}
