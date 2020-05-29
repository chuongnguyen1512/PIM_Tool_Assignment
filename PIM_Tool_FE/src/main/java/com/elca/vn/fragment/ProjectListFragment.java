package com.elca.vn.fragment;

import com.elca.vn.model.GUIProjectTableModel;
import com.elca.vn.proto.model.PimProject;
import com.elca.vn.proto.model.PimProjectCountingRequest;
import com.elca.vn.proto.model.PimProjectCountingResponse;
import com.elca.vn.proto.model.PimProjectQueryRequest;
import com.elca.vn.proto.model.PimProjectQueryResponse;
import com.elca.vn.service.BaseGRPCService;
import com.elca.vn.service.ProjectGRPCService;
import com.elca.vn.transform.GUITransform;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.Pagination;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import org.jacpfx.api.annotations.Resource;
import org.jacpfx.api.annotations.fragment.Fragment;
import org.jacpfx.api.fragment.Scope;
import org.jacpfx.rcp.context.Context;
import org.springframework.util.CollectionUtils;

import java.net.URL;
import java.util.List;
import java.util.Objects;
import java.util.ResourceBundle;
import java.util.UUID;
import java.util.stream.Collectors;

import static com.elca.vn.configuration.JacpFXConfiguration.DEFAULT_RESOURCE_BUNDLE;
import static com.elca.vn.configuration.JacpFXConfiguration.PROJECT_LIST_FRAGMENT_FXML_URL;
import static com.elca.vn.configuration.JacpFXConfiguration.PROJECT_LIST_FRAGMENT_ID;
import static com.elca.vn.constant.StylesheetConstant.PAGING_SIZE;

/**
 * Fragment for listing projects
 */
@Fragment(id = PROJECT_LIST_FRAGMENT_ID,
        viewLocation = PROJECT_LIST_FRAGMENT_FXML_URL,
        scope = Scope.PROTOTYPE,
        resourceBundleLocation = DEFAULT_RESOURCE_BUNDLE)
public class ProjectListFragment implements BaseFragment, Initializable {

    private int currentPage = 0;

    @Resource
    private Context context;

    @FXML
    private ChoiceBox cbStatus;

    @FXML
    private Label lbResetSearch;

    @FXML
    private TableView tvProjects;

    @FXML
    private TableColumn tcProjectNum;

    @FXML
    private TableColumn tcProjectName;

    @FXML
    private TableColumn tcStatus;

    @FXML
    private TableColumn tcCustomer;

    @FXML
    private TableColumn tcStartDate;

    @FXML
    private Pagination pageProject;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        tcProjectNum.setCellValueFactory(new PropertyValueFactory<GUIProjectTableModel, Integer>("projectNumber"));
        tcProjectName.setCellValueFactory(new PropertyValueFactory<GUIProjectTableModel, String>("projectName"));
        tcStatus.setCellValueFactory(new PropertyValueFactory<GUIProjectTableModel, String>("status"));
        tcStartDate.setCellValueFactory(new PropertyValueFactory<GUIProjectTableModel, String>("startDate"));
        tcCustomer.setCellValueFactory(new PropertyValueFactory<GUIProjectTableModel, String>("customer"));

        loadStatusData(cbStatus);
        loadProjectsData();
    }

    private void loadProjectsData() {
        grpcHandling(context, () -> {
            BaseGRPCService projectGRPCService = ProjectGRPCService.getInstance("localhost", 8084);
            PimProjectCountingResponse response = projectGRPCService.sendingRPCRequest(PimProjectCountingRequest.newBuilder()
                    .setTransactionID(UUID.randomUUID().toString())
                    .build());
            verifyResponse(response);
        });
    }

    private void verifyResponse(PimProjectCountingResponse response) {
        if (Objects.isNull(response) || !response.getIsSuccess()) {
            return;
        }

        int totalRecordsNum = Long.valueOf(response.getTotalRecordsNum()).intValue();
        currentPage = 0;
        pageProject.setCurrentPageIndex(currentPage);
        pageProject.setPageCount(Long.valueOf(totalRecordsNum).intValue());
        pageProject.setMaxPageIndicatorCount(calculateIndicator(totalRecordsNum));
        pageProject.currentPageIndexProperty().addListener((observable, oldValue, newValue) -> grpcHandling(context, () -> fillingDataPerPage(newValue.intValue())));
        fillingDataPerPage(currentPage);
    }

    private int calculateIndicator(int totalRecordsNum) {
        return (totalRecordsNum + PAGING_SIZE - 1) / PAGING_SIZE;
    }

    private void fillingDataPerPage(int page) {
        BaseGRPCService projectGRPCService = ProjectGRPCService.getInstance("localhost", 8084);
        PimProjectQueryResponse queryResponse = projectGRPCService.sendingRPCRequest(PimProjectQueryRequest.newBuilder()
                .setTransactionID(UUID.randomUUID().toString())
                .setPage(page)
                .build());

        if (Objects.isNull(queryResponse) || !queryResponse.getIsSuccess()) {
            return;
        }
        registerToTable(queryResponse.getProjectList());
    }

    private void registerToTable(List<PimProject> projectList) {
        if (CollectionUtils.isEmpty(projectList)) {
            return;
        }
        List<GUIProjectTableModel> guiModels = projectList.stream().map(x -> GUITransform.transformToGUIModel(x)).collect(Collectors.toList());
        tvProjects.setItems(FXCollections.observableList(guiModels));
        tvProjects.refresh();
    }
}
