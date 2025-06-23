package com.github.cloudgyb.javafxtest.view;

import javafx.event.Event;
import javafx.event.EventTarget;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;

/**
 * @author cloudgyb
 * @since 2025/6/23 20:27
 */
public class MainViewController {

    public TabPane mainTabPane;
    public Tab urlTestTab;
    public Tab urlTestHistoryTab;

    public void initialize() {
        mainTabPane.getSelectionModel().selectedItemProperty().addListener(
                (obs, oldTab, newTab) -> {
                    if (newTab != null) {
                        System.out.println("切换到: " + newTab.getText());
                        if (newTab == urlTestHistoryTab) {
                            Event.fireEvent(newTab.getContent(), new Event(Tab.SELECTION_CHANGED_EVENT));
                        }
                    }
                });
    }
}
