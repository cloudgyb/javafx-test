package com.github.cloudgyb.javafxtest;

import com.github.cloudgyb.javafxtest.database.DBUtil;
import com.github.cloudgyb.javafxtest.domain.UrlTestHistory;
import javafx.application.Platform;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.sql.SQLException;
import java.sql.Timestamp;

/**
 * @author cloudgyb
 * @since 2025/6/21 17:29
 */
public class URLTesterModel {
    private final HttpClient client = HttpClient.newHttpClient();
    private final Notifications notifications = Notifications.getInstance();

    public void test(String url) {
        URLTestResult result;
        try {
            HttpRequest request = HttpRequest.newBuilder().GET().uri(URI.create(url)).build();
            URLTestProgress urlTestProgress = new URLTestProgress(0.2);
            URLTestProgress finalUrlTestProgress = urlTestProgress;
            Platform.runLater(
                    () -> notifications.publish(Notifications.TEST_PROGRESS_EVENT, finalUrlTestProgress)
            );
            long start = System.currentTimeMillis();
            HttpResponse<Void> resp = client.send(request, HttpResponse.BodyHandlers.discarding());
            urlTestProgress = new URLTestProgress(1.0);

            URLTestProgress finalUrlTestProgress1 = urlTestProgress;
            Platform.runLater(
                    () -> notifications.publish(Notifications.TEST_PROGRESS_EVENT, finalUrlTestProgress1)
            );
            long end = System.currentTimeMillis();
            int statusCode = resp.statusCode();
            result = new URLTestResult(statusCode, end - start, true, "");
        } catch (IOException | InterruptedException | IllegalArgumentException e) {
            String errorMsg = exceptionToErrorMsg(e);
            result = new URLTestResult(-1, -1, false, errorMsg);
            System.out.println(e.getMessage());
        }
        URLTestResult finalResult = result;
        Platform.runLater(() -> notifications.publish(Notifications.TEST_RESULT_EVENT, finalResult));
        try {
            DBUtil.insert(new UrlTestHistory(url, finalResult.statusCode(), finalResult.loadTime(),
                    new Timestamp(System.currentTimeMillis()), finalResult.errorInfo()));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private String exceptionToErrorMsg(Exception e) {
        return switch (e.getClass().getSimpleName()) {
            case "ConnectException" -> "无法连接到服务器";
            case "SocketTimeoutException" -> "请求超时";
            case "IllegalArgumentException" -> "无效的URL";
            default -> "未知错误";
        };
    }
}
