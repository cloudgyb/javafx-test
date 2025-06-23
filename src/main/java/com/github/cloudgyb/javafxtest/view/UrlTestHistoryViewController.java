package com.github.cloudgyb.javafxtest.view;

import com.github.cloudgyb.javafxtest.viewmodel.UrlTestHistoryViewModel;
import javafx.scene.control.Tab;
import javafx.scene.control.TableView;
import javafx.scene.layout.StackPane;

/**
 * @author cloudgyb
 * @since 2025/6/23 20:38
 */
public class UrlTestHistoryViewController {

    public StackPane stackPane;
    public TableView<UrlTestHistoryViewModel> table;

    public void initialize() {
        System.out.println("UrlTestHistoryViewController.initialize()");
        stackPane.addEventHandler(Tab.SELECTION_CHANGED_EVENT, event -> {
            System.out.println("UrlTestHistoryViewController.table.addEventHandler()");
            event.consume();
            refreshData();
        });
    }

    public void refreshData() {
        System.out.println("UrlTestHistoryViewController.refreshData()");
    }
}
