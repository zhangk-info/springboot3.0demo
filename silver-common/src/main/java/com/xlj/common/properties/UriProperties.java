package com.xlj.common.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author zhangkun
 */
@Component
@ConfigurationProperties(prefix = "url")
public class UriProperties {

    private List<String> ignores;

    private List<String> publicIgnores;

    public List<String> getIgnores() {
        return ignores;
    }

    public void setIgnores(List<String> ignores) {
        this.ignores = ignores;
    }

    public List<String> getPublicIgnores() {
        return publicIgnores;
    }

    public void setPublicIgnores(List<String> publicIgnores) {
        this.publicIgnores = publicIgnores;
    }
}
