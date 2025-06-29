package com.github.cloudgyb.javafxtest;

import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.concurrent.Service;
import javafx.concurrent.Task;

import static com.github.cloudgyb.javafxtest.Notifications.TEST_PROGRESS_EVENT;
import static com.github.cloudgyb.javafxtest.Notifications.TEST_RESULT_EVENT;

/**
 * @author cloudgyb
 * @since 2025/6/21 16:47
 */
public class URLTesterViewModel {
    // 事件通知器
    private static final Notifications NOTIFICATIONS = Notifications.getInstance();
    private final Notifications.Subscriber progressSubscriber;
    private final Notifications.Subscriber testResultSubscriber;
    // view
    private final SimpleStringProperty urlProperty;
    private final SimpleStringProperty statusProperty;
    private final SimpleBooleanProperty testingProperty;
    private final SimpleBooleanProperty testSuccessProperty;
    private final SimpleStringProperty statusCodeProperty;
    private final SimpleStringProperty loadTimeProperty;
    private final SimpleStringProperty testErrorInfoProperty;
    private final SimpleDoubleProperty progressProperty;
    // model
    private final URLTesterModel model = new URLTesterModel();

    public URLTesterViewModel() {
        this.urlProperty = new SimpleStringProperty("https://www.baidu.com");
        this.statusProperty = new SimpleStringProperty("");
        this.testingProperty = new SimpleBooleanProperty(false);
        this.testSuccessProperty = new SimpleBooleanProperty(false);
        this.statusCodeProperty = new SimpleStringProperty("0");
        this.loadTimeProperty = new SimpleStringProperty("0 ms");
        this.testErrorInfoProperty = new SimpleStringProperty("");
        this.progressProperty = new SimpleDoubleProperty(0);
        this.progressSubscriber = new Notifications.Subscriber(this, this::OnProgressUpdate);
        NOTIFICATIONS.subscribe(TEST_PROGRESS_EVENT, progressSubscriber);
        this.testResultSubscriber = new Notifications.Subscriber(this, this::OnTestCompleted);
        NOTIFICATIONS.subscribe(TEST_RESULT_EVENT, testResultSubscriber);
    }

    public void OnTestCompleted(Object eventResult) {
        URLTestResult result = (URLTestResult) eventResult;
        this.testingProperty.set(false);
        this.testSuccessProperty.set(result.success());
        this.statusCodeProperty.set(String.valueOf(result.statusCode()));
        this.loadTimeProperty.set(result.loadTime() + " ms");
        this.testErrorInfoProperty.set(result.success() ? "" : result.errorInfo());
    }

    public void OnProgressUpdate(Object eventResult) {
        URLTestProgress progress = (URLTestProgress) eventResult;
        progressProperty.set(progress.progress());
    }


    public SimpleStringProperty urlProperty() {
        return urlProperty;
    }

    public SimpleStringProperty statusProperty() {
        return statusProperty;
    }

    public SimpleBooleanProperty testingProperty() {
        return testingProperty;
    }

    public SimpleBooleanProperty testSuccessProperty() {
        return testSuccessProperty;
    }

    public SimpleStringProperty statusCodeProperty() {
        return statusCodeProperty;
    }

    public SimpleStringProperty loadTimeProperty() {
        return loadTimeProperty;
    }

    public SimpleStringProperty testErrorInfoProperty() {
        return testErrorInfoProperty;
    }

    public SimpleDoubleProperty progressProperty() {
        return progressProperty;
    }

    public void test() {
        this.testingProperty.set(true);
        this.statusProperty.set("正在测试" + this.urlProperty.get() + "...");
        service.restart();
    }

    public void reset() {
        this.testingProperty.set(false);
        this.testSuccessProperty.set(false);
    }

    private final Service<Void> service = new Service<>() {
        @Override
        protected Task<Void> createTask() {
            return new Task<>() {
                @Override
                protected Void call() {
                    model.test(urlProperty().get());
                    updateProgress(1, 1);
                    updateMessage("测试完成");
                    return null;
                }
            };
        }
    };

    public void destroy() {
        NOTIFICATIONS.unsubscribe(TEST_PROGRESS_EVENT, progressSubscriber);
        NOTIFICATIONS.unsubscribe(TEST_RESULT_EVENT, testResultSubscriber);
    }
}
