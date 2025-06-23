package com.github.cloudgyb.javafxtest;

/**
 * @author cloudgyb
 * @since 2025/6/21 19:08
 */
public record URLTestResult(int statusCode, long loadTime, boolean success, String errorInfo) {
}
