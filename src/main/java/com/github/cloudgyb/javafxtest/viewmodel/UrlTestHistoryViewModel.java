package com.github.cloudgyb.javafxtest.viewmodel;

import com.github.cloudgyb.javafxtest.domain.UrlTestHistory;
import javafx.beans.property.*;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;

/**
 * @author cloudgyb
 * @since 2025/6/23 21:57
 */
public class UrlTestHistoryViewModel {
    private final SimpleIntegerProperty id = new SimpleIntegerProperty(0);
    private final SimpleStringProperty url = new SimpleStringProperty("");
    private final SimpleIntegerProperty status = new SimpleIntegerProperty(200);
    private final SimpleLongProperty loadTime = new SimpleLongProperty(0L);
    private final SimpleStringProperty testTime = new SimpleStringProperty("");
    private final SimpleStringProperty testErrorInfo = new SimpleStringProperty("");
    private final SimpleStringProperty success = new SimpleStringProperty("");
    private final SimpleBooleanProperty checked = new SimpleBooleanProperty(false);

    public UrlTestHistoryViewModel(UrlTestHistory urlTestHistory) {
        this.id.set(urlTestHistory.getId());
        this.url.set(urlTestHistory.getUrl());
        this.status.set(urlTestHistory.getStatusCode());
        this.loadTime.set(urlTestHistory.getLoadTime());
        Timestamp testTimestamp = urlTestHistory.getTestTime();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String testTime = simpleDateFormat.format(testTimestamp);
        this.testTime.set(testTime);
        this.testErrorInfo.set(urlTestHistory.getTestErrorInfo());
        this.success.set(urlTestHistory.getTestErrorInfo() == null ? "成功" : "失败");
    }

    public SimpleIntegerProperty idProperty() {
        return id;
    }

    public SimpleStringProperty urlProperty() {
        return url;
    }

    public SimpleIntegerProperty statusProperty() {
        return status;
    }

    public SimpleLongProperty loadTimeProperty() {
        return loadTime;
    }

    public SimpleStringProperty testTimeProperty() {
        return testTime;
    }

    public SimpleStringProperty testErrorInfoProperty() {
        return testErrorInfo;
    }


    public SimpleStringProperty successProperty() {
        return success;
    }

    public boolean isChecked() {
        return checked.get();
    }

    public SimpleBooleanProperty checkedProperty() {
        return checked;
    }

    public UrlTestHistory toUrlTestHistory() {
        return new UrlTestHistory(url.get(), status.get(),
                loadTime.get(),
                Timestamp.valueOf(testTime.get()), testErrorInfo.get());
    }
}
