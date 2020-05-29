package com.elca.vn.fragment;

import com.elca.vn.configuration.JacpFXConfiguration;
import com.elca.vn.converter.ChoiceBoxGroupStringConverter;
import com.elca.vn.model.BaseWorker;
import com.elca.vn.model.GUIStatusModel;
import com.elca.vn.proto.model.PimGroup;
import com.elca.vn.proto.model.PimGroupRequest;
import com.elca.vn.proto.model.PimGroupResponse;
import com.elca.vn.proto.model.PimProject;
import com.elca.vn.proto.model.PimProjectPersistRequest;
import com.elca.vn.proto.model.PimProjectPersistResponse;
import com.elca.vn.proto.model.ProcessingStatus;
import com.elca.vn.service.GroupGRPCService;
import com.elca.vn.service.ProjectGRPCService;
import com.elca.vn.util.GuiUtils;
import io.grpc.ManagedChannel;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import org.apache.commons.lang3.StringUtils;
import org.jacpfx.api.annotations.Resource;
import org.jacpfx.api.annotations.fragment.Fragment;
import org.jacpfx.api.fragment.Scope;
import org.jacpfx.rcp.context.Context;

import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.ResourceBundle;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;

import static com.elca.vn.configuration.JacpFXConfiguration.DEFAULT_RESOURCE_BUNDLE;
import static com.elca.vn.configuration.JacpFXConfiguration.INTERNAL_ERROR_FRAGMENT_ID;
import static com.elca.vn.configuration.JacpFXConfiguration.OPEN_PROJECT_LIST_MESSAGE;
import static com.elca.vn.configuration.JacpFXConfiguration.PROJECT_FORM_FRAGMENT_FXML_URL;
import static com.elca.vn.configuration.JacpFXConfiguration.PROJECT_FORM_FRAGMENT_ID;
import static com.elca.vn.configuration.JacpFXConfiguration.SEPARATOR_CHARACTER;
import static com.elca.vn.constant.BundleConstant.EMPLOYEES_NOT_EXIST_SYSTEM_MSG_BUNDLE_ID;
import static com.elca.vn.constant.BundleConstant.PROJECT_EXISTING_SYSTEM_MSG_BUNDLE_ID;
import static com.elca.vn.constant.StylesheetConstant.STYLE_BORDER_FAILED;

/**
 * Fragment for update project information
 */
@Fragment(id = PROJECT_FORM_FRAGMENT_ID,
        viewLocation = PROJECT_FORM_FRAGMENT_FXML_URL,
        scope = Scope.PROTOTYPE,
        resourceBundleLocation = DEFAULT_RESOURCE_BUNDLE)
public class ProjectFormFragment implements BaseFragment, Initializable {

    private boolean isUpdateForm = false;

    @Resource
    private ResourceBundle bundle;

    @FXML
    private TextField tfProjectNum;

    @FXML
    private TextField tfProjectName;

    @FXML
    private TextField tfCustomer;

    @FXML
    private TextField tfMember;

    @FXML
    private ChoiceBox cbGroup;

    @FXML
    private ChoiceBox cbStatus;

    @FXML
    private DatePicker dpStartDate;

    @FXML
    private DatePicker dpEndDate;

    @Resource
    private Context context;

    @FXML
    private GridPane gridPane;

    @FXML
    private Label lbFieldValidation;

    @FXML
    private Label lbProjectNumValidation;

    @FXML
    private Label lbCustomerValidation;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        loadDataForGroupChoiceBox();
        loadStatusData(cbStatus);
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
            ProjectGRPCService projectGRPCService = ProjectGRPCService.getInstance("localhost", 8084);
            PimProjectPersistResponse projectResponse = projectGRPCService.sendingRPCRequest(projectRequest);
            verifyResponse(projectResponse);
        });
    }

    @FXML
    private void cancelProject() {
        cleanUpInputForm(tfProjectNum, tfProjectName, tfCustomer, tfMember, cbStatus, cbGroup, dpStartDate, dpEndDate, lbCustomerValidation, lbProjectNumValidation);
    }

    private PimProjectPersistRequest collectGUIData(ProcessingStatus processingStatus) {
        PimProject project;
        if (GuiUtils.isEmptyGUIData(Arrays.asList(dpEndDate), StringUtils.EMPTY)) {
            project = PimProject.newBuilder()
                    .setProjectNumber(Integer.parseInt(tfProjectNum.getText()))
                    .setProjectName(tfProjectName.getText())
                    .setCustomer(tfCustomer.getText())
                    .setProcessingStatus(processingStatus)
                    .setStatus(((GUIStatusModel) cbStatus.getSelectionModel().getSelectedItem()).getStatus())
                    .setStartDate(GuiUtils.getDatePickerGoogleTimestampValue(dpStartDate))
                    .setGroupID(((PimGroup) cbGroup.getSelectionModel().getSelectedItem()).getGroupID())
                    .addAllMemberVISAs(GuiUtils.getMultipleValuesFromTextField(tfMember, SEPARATOR_CHARACTER))
                    .build();
        } else {
            project = PimProject.newBuilder()
                    .setProjectNumber(Integer.parseInt(tfProjectNum.getText()))
                    .setProjectName(tfProjectName.getText())
                    .setCustomer(tfCustomer.getText())
                    .setProcessingStatus(processingStatus)
                    .setStatus(((GUIStatusModel) cbStatus.getSelectionModel().getSelectedItem()).getStatus())
                    .setStartDate(GuiUtils.getDatePickerGoogleTimestampValue(dpStartDate))
                    .setEndDate(GuiUtils.getDatePickerGoogleTimestampValue(dpEndDate))
                    .setGroupID(((PimGroup) cbGroup.getSelectionModel().getSelectedItem()).getGroupID())
                    .addAllMemberVISAs(GuiUtils.getMultipleValuesFromTextField(tfMember, SEPARATOR_CHARACTER))
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
            context.send(JacpFXConfiguration.CENTER_COMPONENT_ID, INTERNAL_ERROR_FRAGMENT_ID);
        }

        if (projectResponse.getIsSuccess()) {
            // If sending request to server successfully, redirect to project list UI.
            context.send(JacpFXConfiguration.CENTER_COMPONENT_ID, OPEN_PROJECT_LIST_MESSAGE);
        }

        String bundleID = projectResponse.getBundleID();
        switch (bundleID) {
            case PROJECT_EXISTING_SYSTEM_MSG_BUNDLE_ID:
                lbProjectNumValidation.setText(GuiUtils.getAndResolveBundleResource(bundle, bundleID));
                lbProjectNumValidation.setVisible(true);
                break;
            case EMPLOYEES_NOT_EXIST_SYSTEM_MSG_BUNDLE_ID:
                lbCustomerValidation.setText(GuiUtils.getAndResolveBundleResource(bundle, bundleID, tfCustomer.getText()));
                lbCustomerValidation.setVisible(true);
                break;
            default:
                break;
        }
    }

    private void loadDataForGroupChoiceBox() {
        Service service = new BaseWorker() {
            @Override
            protected Task createTask() {
                return new Task() {
                    @Override
                    protected Object call() throws Exception {
                        PimGroupRequest request = PimGroupRequest.newBuilder()
                                .setTransactionID(UUID.randomUUID().toString())
                                .build();
                        List<PimGroup> groupList = gettingGroupData(request);
                        cbGroup.setItems(FXCollections.observableList(groupList));
                        cbGroup.setConverter(new ChoiceBoxGroupStringConverter(groupList));
                        Platform.runLater(() -> {
                            cbGroup.getSelectionModel().selectFirst();
                        });
                        return null;
                    }
                };
            }
        };
        service.start();

    }

    private List<PimGroup> gettingGroupData(PimGroupRequest request) {
        GroupGRPCService baseGRPCService = GroupGRPCService.getInstance("localhost", 8084);
        try {
            List<PimGroup> groupList = new ArrayList<>();
            Iterator<PimGroupResponse> responses = baseGRPCService.sendingRPCRequestWithStreamingResponse(request);

            if (Objects.isNull(responses)) {
                return groupList;
            }

            while (responses.hasNext()) {
                PimGroupResponse response = responses.next();
                PimGroup pimGroupData = PimGroup.newBuilder()
                        .setGroupID(response.getGroup().getGroupID())
                        .setGroupName(response.getGroup().getGroupName())
                        .build();
                groupList.add(pimGroupData);
            }
            return groupList;
        } finally {
            // Streaming so have to handle shutdown channel outside
            ManagedChannel channel = baseGRPCService.getChannel();
            if (Objects.nonNull(channel) && !channel.isShutdown()) {
                channel.shutdown();
            }
        }
    }

    public boolean isUpdateForm() {
        return isUpdateForm;
    }

    public void setUpdateForm(boolean updateForm) {
        isUpdateForm = updateForm;
    }
}