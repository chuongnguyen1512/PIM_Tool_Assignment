package com.elca.vn.fragment;

import com.elca.vn.component.BaseComponent;
import com.elca.vn.configuration.JacpFXConfiguration;
import com.elca.vn.model.BaseWorker;
import com.elca.vn.model.GUIEventMessage;
import com.elca.vn.model.GUIProjectTableModel;
import com.elca.vn.model.GUIStatusModel;
import com.elca.vn.proto.model.PimProjectCountingRequest;
import com.elca.vn.proto.model.PimProjectCountingResponse;
import com.elca.vn.proto.model.PimProjectDeleteRequest;
import com.elca.vn.proto.model.PimProjectDeleteResponse;
import com.elca.vn.proto.model.PimProjectQueryRequest;
import com.elca.vn.proto.model.PimProjectQueryResponse;
import com.elca.vn.proto.model.Status;
import com.elca.vn.service.ProjectGRPCService;
import com.elca.vn.transform.GUITransform;
import com.elca.vn.util.ApplicationBundleUtils;
import com.elca.vn.util.GuiUtils;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableSet;
import javafx.collections.SetChangeListener;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.Pagination;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import org.apache.commons.lang3.StringUtils;
import org.jacpfx.api.annotations.Resource;
import org.jacpfx.api.annotations.fragment.Fragment;
import org.jacpfx.api.fragment.Scope;
import org.jacpfx.rcp.context.Context;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.util.CollectionUtils;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.ResourceBundle;
import java.util.UUID;
import java.util.stream.Collectors;

import static com.elca.vn.configuration.JacpFXConfiguration.DEFAULT_RESOURCE_BUNDLE;
import static com.elca.vn.configuration.JacpFXConfiguration.OPEN_PROJECT_FORM_MESSAGE;
import static com.elca.vn.configuration.JacpFXConfiguration.PROJECT_LIST_FRAGMENT_FXML_URL;
import static com.elca.vn.configuration.JacpFXConfiguration.PROJECT_LIST_FRAGMENT_ID;
import static com.elca.vn.configuration.JacpFXConfiguration.UPDATE_PROJECT_NUMBER;
import static com.elca.vn.configuration.JacpFXConfiguration.UPDATE_PROJECT_STATUS;
import static com.elca.vn.configuration.PIMAppConfiguration.BEAN_PROJECT_GRPC_SERVICE;
import static com.elca.vn.constant.BundleConstant.ERROR_ALERT_HEADER_BUNDLE_ID;
import static com.elca.vn.constant.BundleConstant.ERROR_ALERT_TITLE_BUNDLE_ID;
import static com.elca.vn.constant.BundleConstant.ITEMS_SELECTED_BUNDLE_ID;
import static com.elca.vn.constant.BundleConstant.PROJECT_CONTAINED_RESTRICTED_STATUS_MSG_BUNDLE_ID;
import static com.elca.vn.constant.StylesheetConstant.ALLOW_DELETE_STATUS;
import static com.elca.vn.constant.StylesheetConstant.PAGING_MAX_INDICATOR;
import static com.elca.vn.constant.StylesheetConstant.STYLE_DELETE_ICON;

/**
 * Fragment for listing projects
 */
@Fragment(id = PROJECT_LIST_FRAGMENT_ID,
        viewLocation = PROJECT_LIST_FRAGMENT_FXML_URL,
        scope = Scope.PROTOTYPE,
        resourceBundleLocation = DEFAULT_RESOURCE_BUNDLE)
public class ProjectListFragment extends BaseComponent implements DisposableBean, Initializable {

    private static final Logger LOGGER = LoggerFactory.getLogger(ProjectListFragment.class);

    private final ProjectGRPCService projectGRPCService;

    private int currentPage = 0;
    private String searchContent = null;
    private Status selectedStatus = null;
    private ObservableSet<GUIProjectTableModel> selectedProjects = FXCollections.observableSet(new HashSet());
    private BaseWorker worker;

    public ProjectListFragment(@Qualifier(BEAN_PROJECT_GRPC_SERVICE) ProjectGRPCService projectGRPCService) {
        this.projectGRPCService = projectGRPCService;
    }

    @Resource
    private Context context;

    @FXML
    private TextField tfSearchContent;

    @FXML
    private ChoiceBox cbStatus;

    @FXML
    private TableView tvProjects;

    @FXML
    private TableColumn<GUIProjectTableModel, GUIProjectTableModel> tcCheckBox;

    @FXML
    private TableColumn<GUIProjectTableModel, GUIProjectTableModel> tcProjectNum;

    @FXML
    private TableColumn tcProjectName;

    @FXML
    private TableColumn tcStatus;

    @FXML
    private TableColumn tcCustomer;

    @FXML
    private TableColumn tcStartDate;

    @FXML
    private TableColumn<GUIProjectTableModel, GUIProjectTableModel> tcBtnDelete;

    @FXML
    private Pagination pageProject;

    @FXML
    private Label lbItemsSelected;

    @FXML
    private Label lbDeleteSelectedItems;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        ApplicationBundleUtils.reloadBundle(context, resources);
        tcProjectName.setCellValueFactory(new PropertyValueFactory<GUIProjectTableModel, String>("projectName"));
        tcStatus.setCellValueFactory(new PropertyValueFactory<GUIProjectTableModel, String>("status"));
        tcStartDate.setCellValueFactory(new PropertyValueFactory<GUIProjectTableModel, String>("startDate"));
        tcCustomer.setCellValueFactory(new PropertyValueFactory<GUIProjectTableModel, String>("customer"));
        loadHyperlink();
        loadCheckBox();
        loadButtonDelete();
        loadStatusData(cbStatus);
        loadProjectsData();
        selectedProjects.addListener((SetChangeListener<GUIProjectTableModel>) change -> {
            if (selectedProjects.size() == 0) {
                lbDeleteSelectedItems.setVisible(false);
            } else {
                lbDeleteSelectedItems.setVisible(true);
            }
        });
    }

    @Override
    public void destroy() throws Exception {
        if (Objects.isNull(worker)) {
            return;
        }
        if (worker.isRunning()) {
            worker.cancel();
        }
    }

    private void loadHyperlink() {
        tcProjectNum.setCellValueFactory(x -> new ReadOnlyObjectWrapper(x.getValue()));
        tcProjectNum.setCellFactory(x -> {
            Hyperlink hyperlink = new Hyperlink();
            TableCell<GUIProjectTableModel, GUIProjectTableModel> tableCell = new TableCell<GUIProjectTableModel, GUIProjectTableModel>() {
                @Override
                protected void updateItem(GUIProjectTableModel item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty) {
                        setGraphic(null);
                    } else {
                        hyperlink.setText(String.valueOf(item.getProjectNumber()));
                        hyperlink.setOnAction(x -> {
                            Map<String, Object> params = new HashMap();
                            params.put(UPDATE_PROJECT_NUMBER, item.getProjectNumber());
                            params.put(UPDATE_PROJECT_STATUS, item.getStatus());
                            context.send(JacpFXConfiguration.CENTER_COMPONENT_ID,
                                    new GUIEventMessage()
                                            .setMessageID(OPEN_PROJECT_FORM_MESSAGE)
                                            .setParams(params));
                        });
                        setGraphic(hyperlink);
                    }
                }
            };
            tableCell.setAlignment(Pos.CENTER_LEFT);
            return tableCell;
        });
    }

    private void loadButtonDelete() {
        tcBtnDelete.setCellValueFactory(x -> new ReadOnlyObjectWrapper(x.getValue()));
        tcBtnDelete.setCellFactory(x -> {
            Button buttonDelete = new Button();
            try {
                String urlDeleteIcon = new File("src/main/resources/images/delete_icon.png").toURI().toURL().toExternalForm();
                buttonDelete.setStyle(String.format(STYLE_DELETE_ICON, urlDeleteIcon));
            } catch (MalformedURLException e) {
                LOGGER.warn("Cannot load delete icon");
            }
            TableCell<GUIProjectTableModel, GUIProjectTableModel> tableCell = new TableCell<GUIProjectTableModel, GUIProjectTableModel>() {
                @Override
                protected void updateItem(GUIProjectTableModel item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty) {
                        setGraphic(null);
                    } else if (Objects.nonNull(item) && GUIStatusModel.NEW.statusValue.equals(item.getStatus())) {
                        setGraphic(buttonDelete);
                    }
                    buttonDelete.setOnAction(x -> deleteProjects(Collections.singletonList(item.getProjectNumber())));
                }
            };
            tableCell.setAlignment(Pos.CENTER);
            return tableCell;
        });
    }

    private void loadCheckBox() {
        tcCheckBox.setCellValueFactory(x -> new ReadOnlyObjectWrapper(x.getValue()));
        tcCheckBox.setCellFactory(x -> {
            CheckBox checkBox = new CheckBox();
            TableCell tableCell = new TableCell<GUIProjectTableModel, GUIProjectTableModel>() {
                @Override
                protected void updateItem(GUIProjectTableModel item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty) {
                        setGraphic(null);
                    } else {
                        setGraphic(checkBox);
                    }
                    checkBox.selectedProperty().addListener((observable, oldValue, newValue) -> {
                        lbItemsSelected.setVisible(true);
                        if (newValue && !oldValue) {
                            selectedProjects.add(item);
                        }
                        if (oldValue && !newValue) {
                            selectedProjects.remove(item);
                        }
                        lbItemsSelected.setText(GuiUtils.getAndResolveBundleResource(
                                context.getResourceBundle(), ITEMS_SELECTED_BUNDLE_ID, String.valueOf(selectedProjects.size())));
                    });
                }
            };
            tableCell.setAlignment(Pos.CENTER);
            return tableCell;
        });
    }

    @FXML
    public void resetSearchInputs() {
        selectedProjects.clear();
        lbItemsSelected.setVisible(false);
        lbDeleteSelectedItems.setVisible(false);
        tfSearchContent.setText(StringUtils.EMPTY);
        cbStatus.getSelectionModel().selectFirst();
        searchContent = null;
        selectedStatus = null;
        loadProjectsData();
    }

    @FXML
    public void searchProjects() {
        searchContent = tfSearchContent.getText();
        selectedStatus = ((GUIStatusModel) cbStatus.getSelectionModel().getSelectedItem()).status;
        loadProjectsData();
    }

    @FXML
    public void deleteMultipleItems() {
        boolean containedRestrictedProjects = selectedProjects.stream()
                .filter(x -> !ALLOW_DELETE_STATUS.contains(GUIStatusModel.valueOfStatusValue(x.getStatus())))
                .findFirst()
                .isPresent();

        if (CollectionUtils.isEmpty(selectedProjects) || containedRestrictedProjects) {
            GuiUtils.showAlert(Alert.AlertType.ERROR,
                    GuiUtils.getAndResolveBundleResource(context.getResourceBundle(), PROJECT_CONTAINED_RESTRICTED_STATUS_MSG_BUNDLE_ID),
                    GuiUtils.getAndResolveBundleResource(context.getResourceBundle(), ERROR_ALERT_TITLE_BUNDLE_ID),
                    GuiUtils.getAndResolveBundleResource(context.getResourceBundle(), ERROR_ALERT_HEADER_BUNDLE_ID));
            return;
        }
        List<Integer> projectNums = selectedProjects.stream().map(GUIProjectTableModel::getProjectNumber).collect(Collectors.toList());
        deleteProjects(projectNums);
    }

    public void loadProjectsData() {
        pageProject.setMaxPageIndicatorCount(1);
        pageProject.setPageCount(1);
        pageProject.setCurrentPageIndex(0);
        tvProjects.setItems(null);
        grpcHandling(context, () -> {
            PimProjectCountingResponse response = prepareAndSendCountingRequest();
            if (Objects.isNull(response) || !response.getIsSuccess()) {
                return;
            }
            registerDataToPagination(response.getTotalRecordsNum());
        });
    }

    private void deleteProjects(List<Integer> selectedProjectNums) {
        if (CollectionUtils.isEmpty(selectedProjectNums)) {
            return;
        }
        grpcHandling(context, () -> {
            PimProjectDeleteResponse response = projectGRPCService.sendingRPCRequest(PimProjectDeleteRequest.newBuilder()
                    .setTransactionID(UUID.randomUUID().toString())
                    .addAllProjectNumbers(selectedProjectNums)
                    .build());
            if (Objects.isNull(response) || !response.getIsSuccess()) {
                return;
            }
        });
        resetSearchInputs();
    }

    private PimProjectCountingResponse prepareAndSendCountingRequest() {
        if (StringUtils.isNotBlank(searchContent) && Objects.nonNull(selectedStatus)) {
            return projectGRPCService.sendingRPCRequest(PimProjectCountingRequest.newBuilder()
                    .setTransactionID(UUID.randomUUID().toString())
                    .setSearchContent(searchContent)
                    .setStatus(selectedStatus.name())
                    .build());
        }
        if (StringUtils.isNotBlank(searchContent)) {
            return projectGRPCService.sendingRPCRequest(PimProjectCountingRequest.newBuilder()
                    .setTransactionID(UUID.randomUUID().toString())
                    .setSearchContent(searchContent)
                    .build());
        }
        if (Objects.nonNull(selectedStatus)) {
            return projectGRPCService.sendingRPCRequest(PimProjectCountingRequest.newBuilder()
                    .setTransactionID(UUID.randomUUID().toString())
                    .setStatus(selectedStatus.name())
                    .build());
        }
        return projectGRPCService.sendingRPCRequest(PimProjectCountingRequest.newBuilder()
                .setTransactionID(UUID.randomUUID().toString())
                .build());
    }

    private PimProjectQueryResponse prepareAndSendQueryRequest(int page) {
        if (StringUtils.isNotBlank(searchContent) && Objects.nonNull(selectedStatus)) {
            return projectGRPCService.sendingRPCRequest(PimProjectQueryRequest.newBuilder()
                    .setTransactionID(UUID.randomUUID().toString())
                    .setSearchContent(searchContent)
                    .setStatus(selectedStatus.name())
                    .setPage(page)
                    .build());
        }
        if (StringUtils.isNotBlank(searchContent)) {
            return projectGRPCService.sendingRPCRequest(PimProjectQueryRequest.newBuilder()
                    .setTransactionID(UUID.randomUUID().toString())
                    .setSearchContent(searchContent)
                    .setPage(page)
                    .build());
        }
        if (Objects.nonNull(selectedStatus)) {
            return projectGRPCService.sendingRPCRequest(PimProjectQueryRequest.newBuilder()
                    .setTransactionID(UUID.randomUUID().toString())
                    .setStatus(selectedStatus.name())
                    .setPage(page)
                    .build());
        }
        return projectGRPCService.sendingRPCRequest(PimProjectQueryRequest.newBuilder()
                .setTransactionID(UUID.randomUUID().toString())
                .setPage(page)
                .build());
    }

    private void registerDataToPagination(long totalRecords) {
        currentPage = 0;
        int totalRecordsNum = Long.valueOf(totalRecords).intValue();
        int pageCount = GuiUtils.calculatePageCount(totalRecordsNum);
        pageProject.setCurrentPageIndex(currentPage);
        pageProject.setPageCount(GuiUtils.calculatePageCount(totalRecordsNum));
        pageProject.setMaxPageIndicatorCount(pageCount < PAGING_MAX_INDICATOR ? pageCount : PAGING_MAX_INDICATOR);
        pageProject.currentPageIndexProperty().addListener((observable, oldValue, newValue) -> grpcHandling(context, () -> fillingDataToTableView(newValue.intValue())));
        fillingDataToTableView(currentPage);
    }

    private void fillingDataToTableView(int page) {
        PimProjectQueryResponse queryResponse = prepareAndSendQueryRequest(page);
        if (Objects.isNull(queryResponse) || !queryResponse.getIsSuccess()) {
            return;
        }

        if (CollectionUtils.isEmpty(queryResponse.getProjectList())) {
            return;
        }

        List<GUIProjectTableModel> guiModels = queryResponse.getProjectList().stream().map(x -> GUITransform.transformToGUIModel(x)).collect(Collectors.toList());
        tvProjects.setItems(FXCollections.observableList(guiModels));
        tvProjects.refresh();
    }
}
