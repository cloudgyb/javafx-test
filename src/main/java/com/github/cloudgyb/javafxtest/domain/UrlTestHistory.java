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

    public UrlTestHistory() {
    }

    public UrlTestHistory(String url, int statusCode, long loadTime,
                          Timestamp testTime, String testErrorInfo) {
        this.url = url;
        this.statusCode = statusCode;
        this.loadTime = loadTime;
        this.testTime = testTime;
        this.testErrorInfo = testErrorInfo;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    public void setLoadTime(long loadTime) {
        this.loadTime = loadTime;
    }

    public void setTestTime(Timestamp testTime) {
        this.testTime = testTime;
    }

    public void setTestErrorInfo(String testErrorInfo) {
        this.testErrorInfo = testErrorInfo;
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
