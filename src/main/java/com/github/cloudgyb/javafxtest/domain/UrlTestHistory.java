package com.github.cloudgyb.javafxtest.domain;

import java.sql.Timestamp;

/**
 * @author cloudgyb
 * @since 2025/6/23 21:57
 */
public class UrlTestHistory {

    private int id;
    private String url;
    private int statusCode;
    private long loadTime;
    private Timestamp testTime;
    private String testErrorInfo;
    public void setId(int anInt) {
    }

    public void setUrl(String string) {
        
    }

    public void setStatusCode(int anInt) {
    }

    public void setLoadTime(long aLong) {
    }

    public void setTestTime(Timestamp timestamp) {
    }

    public void setTestErrorInfo(String string) {
    }

    public String getUrl() {
        return url;
    }
    public int getStatusCode() {
        return statusCode;
    }
    public long getLoadTime() {
        return loadTime;
    }
    public Timestamp getTestTime() {
        return testTime;
    }
    public String getTestErrorInfo() {
        return testErrorInfo;
    }
}
