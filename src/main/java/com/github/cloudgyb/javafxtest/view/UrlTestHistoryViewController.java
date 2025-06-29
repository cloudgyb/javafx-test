package com.github.cloudgyb.javafxtest.view;

import com.github.cloudgyb.javafxtest.database.DBUtil;
import com.github.cloudgyb.javafxtest.database.Page;
import com.github.cloudgyb.javafxtest.domain.UrlTestHistory;
import com.github.cloudgyb.javafxtest.viewmodel.UrlTestHistoryViewModel;
import javafx.collections.transformation.FilteredList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.util.Callback;

import java.sql.SQLException;
import java.util.Optional;

/**
 * @author cloudgyb
 * @since 2025/6/23 20:38
 */
public class UrlTestHistoryViewController {
    private final static int PAGE_SIZE = 20;
    public StackPane stackPane;
    public TableView<UrlTestHistoryViewModel> table;
    public CheckBox selectAllCheckBox;
    public TableColumn<UrlTestHistoryViewModel, Boolean> selectColumn;
    public TableColumn<UrlTestHistoryViewModel, String> urlColumn;
    public TableColumn<UrlTestHistoryViewModel, Integer> statusCodeColumn;
    public TableColumn<UrlTestHistoryViewModel, Long> loadTimeColumn;
    public TableColumn<UrlTestHistoryViewModel, String> successColumn;
    public TableColumn<UrlTestHistoryViewModel, Void> actionColumn;
    public Button deleteSelectedBtn;
    public Pagination pagination;

    public void initialize() {
        System.out.println("UrlTestHistoryViewController.initialize()");
        selectAllCheckBox.selectedProperty().addListener(
                (obs, oldValue, newValue) ->
                        table.getItems()
                                .forEach(viewModel -> viewModel.checkedProperty().set(newValue)));

        deleteSelectedBtn.setOnAction(e -> {
            FilteredList<UrlTestHistoryViewModel> checkedViewModels =
                    table.getItems().filtered(UrlTestHistoryViewModel::isChecked);
            if (checkedViewModels.isEmpty()) {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("提示");
                alert.setHeaderText("请选择要删除的项");
                alert.showAndWait();
                return;
            }

            if (confirmDelete(checkedViewModels.size())) {
                checkedViewModels.forEach(UrlTestHistoryViewModel::delete);
                table.getItems().removeIf(UrlTestHistoryViewModel::isChecked);
                refreshDataIfCurrPageIsEmpty();
            }
        });
        table.getSortOrder().add(urlColumn);
        // select column
        selectColumn.setCellFactory(CheckBoxTableCell.forTableColumn(selectColumn));
        selectColumn.setCellValueFactory(
                row -> row.getValue().checkedProperty()
        );
        urlColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        urlColumn.setCellValueFactory(new PropertyValueFactory<>("url"));
        statusCodeColumn.setCellValueFactory(new PropertyValueFactory<>("status"));
        loadTimeColumn.setCellValueFactory(new PropertyValueFactory<>("loadTime"));
        successColumn.setCellValueFactory(new PropertyValueFactory<>("success"));
        actionColumn.setCellFactory(createActionCellFactory());
        stackPane.addEventHandler(Tab.SELECTION_CHANGED_EVENT, event -> {
            event.consume();
            System.out.println("UrlTestHistoryViewController.table.addEventHandler()");
            selectAllCheckBox.setSelected(false);
            pagination.setCurrentPageIndex(0);
            getPageData(0);
        });
        pagination.setPageFactory(this::getPageData);
    }

    /**
     * 注意：pageIndex 从0开始
     *
     * @param pageIndex 页码索引
     * @return 表格table
     */
    public Node getPageData(int pageIndex) {
        try {
            table.getItems().clear();
            Page<UrlTestHistory> page = DBUtil.selectPage(pageIndex, PAGE_SIZE);
            int totalPage = page.getTotalPage();
            pagination.setPageCount(totalPage);
            // 会导致加载数据死循环，导致栈溢出，所以注掉了
            /*if (totalPage == 0) {
                pagination.setMaxPageIndicatorCount(1);
                pagination.setDisable(true);
            } else {
                pagination.setMaxPageIndicatorCount(10);
                pagination.setDisable(false);
            }*/
            page.getList().forEach(urlTestHistory -> {
                UrlTestHistoryViewModel urlTestHistoryViewModel = new UrlTestHistoryViewModel(urlTestHistory);
                table.getItems().add(urlTestHistoryViewModel);
            });
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return table;
    }


    // 创建操作列的单元格工厂
    private Callback<TableColumn<UrlTestHistoryViewModel, Void>, TableCell<UrlTestHistoryViewModel, Void>> createActionCellFactory() {
        return new Callback<>() {
            @Override
            public TableCell<UrlTestHistoryViewModel, Void> call(final TableColumn<UrlTestHistoryViewModel, Void> param) {
                return new TableCell<>() {
                    private final Button deleteBtn = new Button("删除");
                    private final Button detailsBtn = new Button("详情");
                    private final HBox buttonsBox = new HBox(5, deleteBtn, detailsBtn);

                    {
                        buttonsBox.setAlignment(Pos.CENTER);

                        // 删除按钮样式
                        deleteBtn.setStyle("-fx-background-color: #ff4d4d; -fx-text-fill: white;");

                        // 详情按钮样式
                        detailsBtn.setStyle("-fx-background-color: #4da6ff; -fx-text-fill: white;");
                        // 设置按钮动作
                        deleteBtn.setOnMouseClicked(event -> {
                            UrlTestHistoryViewModel viewModel = getTableView().getItems().get(getIndex());
                            delete(viewModel);
                        });

                        detailsBtn.setOnMouseClicked(event -> {
                            UrlTestHistoryViewModel testHistoryViewModel = getTableView().getItems().get(getIndex());
                            showDetails(testHistoryViewModel);
                        });
                    }

                    @Override
                    protected void updateItem(Void item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty) {
                            setGraphic(null);
                        } else {
                            setGraphic(buttonsBox);
                        }
                    }
                };
            }
        };
    }

    private void delete(UrlTestHistoryViewModel testHistoryViewModel) {
        if (confirmDelete(1)) {
            testHistoryViewModel.delete();
            table.getItems().remove(testHistoryViewModel);
            refreshDataIfCurrPageIsEmpty();
        }
    }

    private void refreshDataIfCurrPageIsEmpty() {
        if (table.getItems().isEmpty()) {
            int prePageNumber = pagination.getCurrentPageIndex() - 1;
            int currPageNumber = Math.max(prePageNumber, 0);
            pagination.setCurrentPageIndex(currPageNumber);
            getPageData(currPageNumber);
        }
    }

    private void showDetails(UrlTestHistoryViewModel testHistoryViewModel) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("URL 测试详情");
        alert.setHeaderText(testHistoryViewModel.urlProperty().get());

        VBox content = new VBox(10);
        content.setPrefWidth(300);
        content.setPadding(new Insets(10));
        content.getChildren().addAll(
                new Label("ID: " + testHistoryViewModel.idProperty().get()),
                new Label("URL: " + testHistoryViewModel.urlProperty().get()),
                new Label("消耗时间: " + testHistoryViewModel.loadTimeProperty().get()),
                new Label("测试结果: " + testHistoryViewModel.successProperty().get()),
                new Label("测试时间: " + testHistoryViewModel.testTimeProperty().get()),
                new Label("错误信息: " + testHistoryViewModel.testErrorInfoProperty().get())
        );

        alert.getDialogPane().setContent(content);
        alert.showAndWait();
    }

    // 确认删除对话框
    private boolean confirmDelete(int count) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("确认删除");
        alert.setHeaderText(null);
        alert.setContentText("确定要删除这" + count + "项吗?");

        Optional<ButtonType> result = alert.showAndWait();
        return result.isPresent() && result.get() == ButtonType.OK;
    }

}
