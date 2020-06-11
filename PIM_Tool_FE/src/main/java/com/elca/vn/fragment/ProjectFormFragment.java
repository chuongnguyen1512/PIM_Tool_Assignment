package com.elca.vn.fragment;

import com.elca.vn.component.BaseComponent;
import com.elca.vn.configuration.JacpFXConfiguration;
import com.elca.vn.converter.ChoiceBoxGroupStringConverter;
import com.elca.vn.model.GUIEventMessage;
import com.elca.vn.model.GUIStatusModel;
import com.elca.vn.model.TagBarWithListView;
import com.elca.vn.proto.model.PimEmployee;
import com.elca.vn.proto.model.PimEmployeeQueryRequest;
import com.elca.vn.proto.model.PimEmployeeQueryResponse;
import com.elca.vn.proto.model.PimGroup;
import com.elca.vn.proto.model.PimGroupRequest;
import com.elca.vn.proto.model.PimGroupResponse;
import com.elca.vn.proto.model.PimProject;
import com.elca.vn.proto.model.PimProjectPersistRequest;
import com.elca.vn.proto.model.PimProjectPersistResponse;
import com.elca.vn.proto.model.ProcessingStatus;
import com.elca.vn.service.EmployeeGRPCService;
import com.elca.vn.service.GroupGRPCService;
import com.elca.vn.service.ProjectGRPCService;
import com.elca.vn.util.ApplicationBundleUtils;
import com.elca.vn.util.GuiUtils;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;
import org.jacpfx.api.annotations.Resource;
import org.jacpfx.api.annotations.fragment.Fragment;
import org.jacpfx.api.fragment.Scope;
import org.jacpfx.rcp.context.Context;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.util.CollectionUtils;

import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.ResourceBundle;
import java.util.UUID;
import java.util.stream.Collectors;

import static com.elca.vn.configuration.JacpFXConfiguration.DEFAULT_RESOURCE_BUNDLE;
import static com.elca.vn.configuration.JacpFXConfiguration.DOUBLE_DOTS_SEPARATOR_CHARACTER;
import static com.elca.vn.configuration.JacpFXConfiguration.INTERNAL_ERROR_FRAGMENT_ID;
import static com.elca.vn.configuration.JacpFXConfiguration.OPEN_PROJECT_LIST_MESSAGE;
import static com.elca.vn.configuration.JacpFXConfiguration.PROJECT_FORM_FRAGMENT_FXML_URL;
import static com.elca.vn.configuration.JacpFXConfiguration.PROJECT_FORM_FRAGMENT_ID;
import static com.elca.vn.configuration.PIMAppConfiguration.BEAN_EMPLOYEE_GRPC_SERVICE;
import static com.elca.vn.configuration.PIMAppConfiguration.BEAN_GROUP_GRPC_SERVICE;
import static com.elca.vn.configuration.PIMAppConfiguration.BEAN_PROJECT_GRPC_SERVICE;
import static com.elca.vn.constant.BundleConstant.EMPLOYEES_NOT_EXIST_SYSTEM_MSG_BUNDLE_ID;
import static com.elca.vn.constant.BundleConstant.PROJECT_EXISTING_SYSTEM_MSG_BUNDLE_ID;
import static com.elca.vn.constant.BundleConstant.PROJECT_NOT_EXIST_SYSTEM_MSG_BUNDLE_ID;
import static com.elca.vn.constant.StylesheetConstant.STYLE_BORDER_FAILED;

/**
 * Fragment for update project information
 */
@Fragment(id = PROJECT_FORM_FRAGMENT_ID,
        viewLocation = PROJECT_FORM_FRAGMENT_FXML_URL,
        scope = Scope.PROTOTYPE,
        resourceBundleLocation = DEFAULT_RESOURCE_BUNDLE)
public class ProjectFormFragment extends BaseComponent implements Initializable {

    private final ProjectGRPCService projectGRPCService;
    private final GroupGRPCService groupGRPCService;
    private final EmployeeGRPCService employeeGRPCService;

    @Getter
    @Setter
    private boolean isUpdateForm = false;

    @Autowired
    public ProjectFormFragment(@Qualifier(BEAN_PROJECT_GRPC_SERVICE) ProjectGRPCService projectGRPCService,
                               @Qualifier(BEAN_GROUP_GRPC_SERVICE) GroupGRPCService groupGRPCService,
                               @Qualifier(BEAN_EMPLOYEE_GRPC_SERVICE) EmployeeGRPCService employeeGRPCService) {
        this.projectGRPCService = projectGRPCService;
        this.groupGRPCService = groupGRPCService;
        this.employeeGRPCService = employeeGRPCService;
    }

    @Resource
    private Context context;

    @Resource
    private ResourceBundle bundle;

    @FXML
    @Getter
    @Setter
    private Label lbProject;

    @FXML
    @Getter
    @Setter
    private TextField tfProjectNum;

    @FXML
    @Getter
    @Setter
    private TextField tfProjectName;

    @FXML
    @Getter
    @Setter
    private TextField tfCustomer;

    @FXML
    @Getter
    @Setter
    private VBox lvMembers;

    @Getter
    @Setter
    private TagBarWithListView tagBarWithListView;

    @FXML
    @Getter
    @Setter
    private ChoiceBox cbGroup;

    @FXML
    @Getter
    @Setter
    private ChoiceBox cbStatus;

    @FXML
    @Getter
    @Setter
    private DatePicker dpStartDate;

    @FXML
    @Getter
    @Setter
    private DatePicker dpEndDate;

    @FXML
    @Getter
    @Setter
    private Button btnSubmit;

    @FXML
    private Label lbFieldValidation;

    @FXML
    private Label lbProjectNumValidation;

    @FXML
    private Label lbCustomerValidation;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        ApplicationBundleUtils.reloadBundle(context, resources);
        loadDataForGroupChoiceBox();
        loadStatusData(cbStatus);
        loadTagBarMembers();
        tfProjectNum.setOnKeyReleased(event -> tfProjectNum.setStyle(null));
        tfProjectName.setOnKeyReleased(event -> tfProjectName.setStyle(null));
        tfCustomer.setOnKeyReleased(event -> tfCustomer.setStyle(null));
        dpStartDate.setOnKeyReleased(event -> dpStartDate.setStyle(null));
        dpStartDate.valueProperty().addListener((observable, oldValue, newValue) -> {
            if (Objects.nonNull(newValue)) {
                dpStartDate.setStyle(null);
            }
        });
    }

    @FXML
    private void submitProject() {
        lbCustomerValidation.setText(StringUtils.EMPTY);
        lbProjectNumValidation.setText(StringUtils.EMPTY);

        grpcHandling(context, () -> {
            ProcessingStatus status = ProcessingStatus.INSERT;
            if (isUpdateForm) {
                status = ProcessingStatus.UPDATE;
            }
            if (isInvalidForm(STYLE_BORDER_FAILED, tfProjectName, tfCustomer, tfProjectNum, dpStartDate, cbStatus, cbGroup)) {
                lbFieldValidation.setVisible(true);
                return;
            }
            PimProjectPersistRequest projectRequest = collectGUIData(status);
            PimProjectPersistResponse projectResponse = projectGRPCService.sendingRPCRequest(projectRequest);
            verifyResponse(projectResponse);
        });
    }

    @FXML
    private void cancelProject() {
        context.send(JacpFXConfiguration.CENTER_COMPONENT_ID, new GUIEventMessage().setMessageID(OPEN_PROJECT_LIST_MESSAGE));
    }

    private PimProjectPersistRequest collectGUIData(ProcessingStatus processingStatus) {
        PimProject project;
        ObservableList<String> data = tagBarWithListView.getTagBar().getTagItems();
        if (GuiUtils.isEmptyGUIData(Arrays.asList(dpEndDate), StringUtils.EMPTY)) {
            project = PimProject.newBuilder()
                    .setProjectNumber(Integer.parseInt(tfProjectNum.getText()))
                    .setProjectName(tfProjectName.getText())
                    .setCustomer(tfCustomer.getText())
                    .setProcessingStatus(processingStatus)
                    .setStatus(((GUIStatusModel) cbStatus.getSelectionModel().getSelectedItem()).status)
                    .setStartDate(GuiUtils.getDatePickerGoogleTimestampValue(dpStartDate))
                    .setGroupID(((PimGroup) cbGroup.getSelectionModel().getSelectedItem()).getGroupID())
                    .addAllMemberVISAs(GuiUtils.getDividedValues(data, DOUBLE_DOTS_SEPARATOR_CHARACTER))
                    .build();
        } else {
            project = PimProject.newBuilder()
                    .setProjectNumber(Integer.parseInt(tfProjectNum.getText()))
                    .setProjectName(tfProjectName.getText())
                    .setCustomer(tfCustomer.getText())
                    .setProcessingStatus(processingStatus)
                    .setStatus(((GUIStatusModel) cbStatus.getSelectionModel().getSelectedItem()).status)
                    .setStartDate(GuiUtils.getDatePickerGoogleTimestampValue(dpStartDate))
                    .setEndDate(GuiUtils.getDatePickerGoogleTimestampValue(dpEndDate))
                    .setGroupID(((PimGroup) cbGroup.getSelectionModel().getSelectedItem()).getGroupID())
                    .addAllMemberVISAs(GuiUtils.getDividedValues(data, DOUBLE_DOTS_SEPARATOR_CHARACTER))
                    .build();
        }

        return PimProjectPersistRequest.newBuilder()
                .setProject(project)
                .setTransactionID(UUID.randomUUID().toString())
                .build();
    }

    private void verifyResponse(PimProjectPersistResponse projectResponse) {
        if (Objects.isNull(projectResponse)) {
            // If fail, redirect to internal error page
            context.send(JacpFXConfiguration.CENTER_COMPONENT_ID, new GUIEventMessage().setMessageID(INTERNAL_ERROR_FRAGMENT_ID));
        }

        if (projectResponse.getIsSuccess()) {
            // If sending request to server successfully, redirect to project list UI.
            context.send(JacpFXConfiguration.CENTER_COMPONENT_ID, new GUIEventMessage().setMessageID(OPEN_PROJECT_LIST_MESSAGE));
        }

        String bundleID = projectResponse.getBundleID();
        switch (bundleID) {
            case PROJECT_EXISTING_SYSTEM_MSG_BUNDLE_ID:
                lbProjectNumValidation.setText(GuiUtils.getAndResolveBundleResource(bundle, bundleID));
                lbProjectNumValidation.setVisible(true);
                break;
            case PROJECT_NOT_EXIST_SYSTEM_MSG_BUNDLE_ID:
                lbProjectNumValidation.setText(GuiUtils.getAndResolveBundleResource(bundle, bundleID));
                lbProjectNumValidation.setVisible(true);
                break;
            case EMPLOYEES_NOT_EXIST_SYSTEM_MSG_BUNDLE_ID:
                String[] arrayData = tagBarWithListView.getTagBar().getTagItems().toArray(new String[]{});
                lbCustomerValidation.setText(GuiUtils.getAndResolveBundleResource(bundle, bundleID, arrayData));
                lbCustomerValidation.setVisible(true);
                break;
            default:
                break;
        }
    }

    private void loadDataForGroupChoiceBox() {
        PimGroupRequest request = PimGroupRequest.newBuilder()
                .setTransactionID(UUID.randomUUID().toString())
                .build();
        List<PimGroup> groupList = gettingGroupData(request);
        cbGroup.setItems(FXCollections.observableList(groupList));
        cbGroup.setConverter(new ChoiceBoxGroupStringConverter(groupList));
        cbGroup.getSelectionModel().selectFirst();
    }

    private List<PimGroup> gettingGroupData(PimGroupRequest request) {
        PimGroupResponse response = groupGRPCService.sendingRPCRequest(request);
        if (Objects.isNull(response) || !response.getIsSuccess() || CollectionUtils.isEmpty(response.getGroupsList())) {
            return new ArrayList();
        }
        return response.getGroupsList().stream().map(x -> PimGroup.newBuilder()
                .setGroupID(x.getGroupID())
                .setGroupName(x.getGroupName())
                .build())
                .collect(Collectors.toList());
    }

    private void loadTagBarMembers() {
        lvMembers.toFront();
        tagBarWithListView = new TagBarWithListView() {
            @Override
            public List<String> findDataInfo(String searchingContent) {
                List<PimEmployee> employees = new ArrayList<>();
                grpcHandling(context, () -> {
                    PimEmployeeQueryResponse response = employeeGRPCService.sendingRPCRequest(PimEmployeeQueryRequest.newBuilder()
                            .setTransactionID(UUID.randomUUID().toString())
                            .setContentSearch(searchingContent)
                            .build());

                    if (Objects.isNull(response)) {
                        context.send(JacpFXConfiguration.CENTER_COMPONENT_ID, new GUIEventMessage().setMessageID(INTERNAL_ERROR_FRAGMENT_ID));
                    }
                    if (response.getIsSuccess()) {
                        employees.addAll(response.getEmployeesList());
                    }
                });
                return GuiUtils.transformToTextFieldValue(employees);
            }
        };
        lvMembers.getChildren().add(tagBarWithListView);
    }

}