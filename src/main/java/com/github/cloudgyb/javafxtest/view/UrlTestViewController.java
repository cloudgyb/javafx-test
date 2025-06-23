package com.github.cloudgyb.javafxtest.view;

import com.github.cloudgyb.javafxtest.URLTesterViewModel;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;

public class UrlTestViewController {
    // 测试输入元素
    @FXML
    public TextField urlInput;
    @FXML
    public Button urlTestBtn;
    // 测试结果元素
    @FXML
    public HBox testResultHBox;
    @FXML
    public Label statusCodeLabel;
    @FXML
    public Label loadTimeLabel;
    // 测试失败信息展示元素
    @FXML
    public HBox testErrorInfoHBox;
    @FXML
    public Label testErrorInfoLabel;
    // 状态栏元素
    @FXML
    public ToolBar statusToolBar;
    @FXML
    public ProgressBar statusProgressBar;
    @FXML
    public Label statusLabel;

    // view model
    private final URLTesterViewModel viewModel = new URLTesterViewModel();


    public void initialize() {
        System.out.println("HelloController.initialize()");
        urlInput.textProperty().bindBidirectional(viewModel.urlProperty());
        urlInput.disableProperty().bind(viewModel.testingProperty());
        urlTestBtn.disableProperty().bind(viewModel.testingProperty());
        testResultHBox.visibleProperty().bind(viewModel.testSuccessProperty());
        statusCodeLabel.textProperty().bind(viewModel.statusCodeProperty());
        loadTimeLabel.textProperty().bind(viewModel.loadTimeProperty());
        testErrorInfoHBox.visibleProperty().bind(viewModel.testingProperty().not());
        testErrorInfoLabel.textProperty().bind(viewModel.testErrorInfoProperty());
        statusToolBar.visibleProperty().bind(viewModel.testingProperty());
        statusLabel.textProperty().bind(viewModel.statusProperty());
        statusProgressBar.progressProperty().bind(viewModel.progressProperty());
    }

    @FXML
    protected void onUrlTestBtnClick() {
        System.out.println("onUrlTestBtnClick");
        if (viewModel.urlProperty().get().isBlank()) {
            new Alert(Alert.AlertType.ERROR, "请输入URL").showAndWait();
            viewModel.reset();
            return;
        }
        viewModel.test();
    }

    public void destroy() {
        System.out.println("HelloController.destroy()");
        viewModel.destroy();
    }
}