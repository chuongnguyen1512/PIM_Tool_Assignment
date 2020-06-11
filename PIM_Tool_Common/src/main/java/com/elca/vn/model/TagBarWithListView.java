package com.elca.vn.model;

import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Objects;

public abstract class TagBarWithListView<T> extends VBox {

    private final ObservableList<String> listItems;
    private final ListView listView;
    private final TagBar tagBar;

    public TagBarWithListView() {
        // Init components
        listItems = FXCollections.observableArrayList();
        tagBar = new TagBar();
        listView = new ListView();
        VBox.setVgrow(listView, Priority.ALWAYS);
        listView.setPrefHeight(100);
        listView.setItems(listItems);

        // Register events
        registerEventForListItems();
        registerEventForListView();
        registerEventForInputTextFieldOfTagBar();

        // Only need add tagBar due to users haven't typed any values
        this.getChildren().add(tagBar);
    }

    public abstract List<String> findDataInfo(String searchingContent);

    private void registerEventForListView() {
        listView.setOnMouseClicked(x -> {
            Object selectedItem = listView.getSelectionModel().getSelectedItem();
            if (Objects.isNull(selectedItem)) {
                return;
            }
            tagBar.getTagItems().addAll(String.valueOf(selectedItem));
            cleanUpListView();
        });
    }

    private void registerEventForListItems() {
        listItems.addListener((ListChangeListener.Change<? extends String> change) -> {
            while (change.next()) {
                if (change.wasRemoved() && CollectionUtils.isEmpty(tagBar.getTagItems())) {
                    cleanUpListView();
                }
            }
        });
    }

    private void registerEventForInputTextFieldOfTagBar() {
        TextField searchTextField = tagBar.getInputTextField();
        tagBar.getInputTextField().setOnKeyReleased(x -> {
            String searchValue = searchTextField.getText();
            if (StringUtils.isBlank(searchValue)) {
                cleanUpListView();
                return;
            }

            listItems.clear();
            listItems.addAll(findDataInfo(searchValue));
            reloadListView();

            if (!getChildren().contains(listView)) {
                getChildren().add(listView);
            }
        });
    }

    private void cleanUpListView() {
        listView.setItems(null);
        listView.refresh();
        listView.setVisible(false);
        listView.setPrefHeight(0);
    }

    private void reloadListView() {
        listView.setItems(FXCollections.observableArrayList(listItems));
        listView.setVisible(true);
        listView.setPrefHeight(100);
        listView.refresh();
    }

    public ObservableList<String> getListItems() {
        return listItems;
    }

    public ListView getListView() {
        return listView;
    }

    public TagBar getTagBar() {
        return tagBar;
    }
}
