package com.elca.vn.component;

import com.elca.vn.configuration.JacpFXConfiguration;
import com.elca.vn.fragment.InternalErrorFragment;
import com.elca.vn.fragment.ProjectFormFragment;
import com.elca.vn.fragment.ProjectListFragment;
import com.elca.vn.model.BaseWorker;
import com.elca.vn.model.GUIEventMessage;
import com.elca.vn.model.GUIStatusModel;
import com.elca.vn.model.TagBar;
import com.elca.vn.proto.model.PimEmployeeQueryRequest;
import com.elca.vn.proto.model.PimEmployeeQueryResponse;
import com.elca.vn.proto.model.PimGroup;
import com.elca.vn.proto.model.PimProject;
import com.elca.vn.proto.model.PimProjectQueryRequest;
import com.elca.vn.proto.model.PimProjectQueryResponse;
import com.elca.vn.service.EmployeeGRPCService;
import com.elca.vn.service.ProjectGRPCService;
import com.elca.vn.socket.MulticastReceiver;
import com.elca.vn.util.GuiUtils;
import com.elca.vn.util.PIMToolUtils;
import javafx.concurrent.Task;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import org.apache.commons.lang3.StringUtils;
import org.jacpfx.api.annotations.Resource;
import org.jacpfx.api.annotations.component.DeclarativeView;
import org.jacpfx.api.annotations.lifecycle.PostConstruct;
import org.jacpfx.api.annotations.lifecycle.PreDestroy;
import org.jacpfx.api.message.Message;
import org.jacpfx.rcp.component.FXComponent;
import org.jacpfx.rcp.componentLayout.FXComponentLayout;
import org.jacpfx.rcp.components.managedFragment.ManagedFragmentHandler;
import org.jacpfx.rcp.context.Context;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.UUID;

import static com.elca.vn.configuration.JacpFXConfiguration.CENTER_COMPONENT_FXML_URL;
import static com.elca.vn.configuration.JacpFXConfiguration.CENTER_COMPONENT_ID;
import static com.elca.vn.configuration.JacpFXConfiguration.CENTER_COMPONENT_NAME;
import static com.elca.vn.configuration.JacpFXConfiguration.CENTER_COMPONENT_TARGET_LAYOUT_ID;
import static com.elca.vn.configuration.JacpFXConfiguration.CLOSE_CHILD_COMPONENTS_MESSAGE;
import static com.elca.vn.configuration.JacpFXConfiguration.DEFAULT_RESOURCE_BUNDLE;
import static com.elca.vn.configuration.JacpFXConfiguration.OPEN_INTERNAL_ERROR_MESSAGE;
import static com.elca.vn.configuration.JacpFXConfiguration.OPEN_PROJECT_FORM_MESSAGE;
import static com.elca.vn.configuration.JacpFXConfiguration.OPEN_PROJECT_LIST_MESSAGE;
import static com.elca.vn.configuration.JacpFXConfiguration.UPDATE_PROJECT_NUMBER;
import static com.elca.vn.configuration.JacpFXConfiguration.UPDATE_PROJECT_STATUS;
import static com.elca.vn.configuration.PIMAppConfiguration.BEAN_EMPLOYEE_GRPC_SERVICE;
import static com.elca.vn.configuration.PIMAppConfiguration.BEAN_MULTICAST_RECEIVER;
import static com.elca.vn.configuration.PIMAppConfiguration.BEAN_PROJECT_GRPC_SERVICE;
import static com.elca.vn.constant.BundleConstant.LABEL_UPDATE_PROJECT_BUNDLE_ID;
import static com.elca.vn.constant.BundleConstant.PROJECT_BUTTON_UPDATE_SUBMIT_BUNDLE_ID;

/**
 * Component for center layout. Prefer for FXML file {@link JacpFXConfiguration.CENTER_COMPONENT_FXML_URL}
 */
@DeclarativeView(id = CENTER_COMPONENT_ID,
        name = CENTER_COMPONENT_NAME,
        viewLocation = CENTER_COMPONENT_FXML_URL,
        initialTargetLayoutId = CENTER_COMPONENT_TARGET_LAYOUT_ID,
        resourceBundleLocation = DEFAULT_RESOURCE_BUNDLE)
public class CenterComponent extends BaseComponent implements FXComponent {

    private final ProjectGRPCService projectGRPCService;
    private final EmployeeGRPCService employeeGRPCService;

    private static String currentEventID = "";
    private static Map<String, Object> currentEventParams = new HashMap<>();
    private MulticastReceiver multicastReceiver;
    private BaseWorker service;
    private ProjectListFragment projectListController;

    @Autowired
    public CenterComponent(@Qualifier(BEAN_PROJECT_GRPC_SERVICE) ProjectGRPCService projectGRPCService,
                           @Qualifier(BEAN_EMPLOYEE_GRPC_SERVICE) EmployeeGRPCService employeeGRPCService,
                           @Qualifier(BEAN_MULTICAST_RECEIVER) MulticastReceiver multicastReceiver) {
        this.projectGRPCService = projectGRPCService;
        this.employeeGRPCService = employeeGRPCService;
        this.multicastReceiver = multicastReceiver;
    }

    @Resource
    private Context context;

    @Resource
    private ResourceBundle bundle;

    @FXML
    private AnchorPane centerPane;

    @PostConstruct
    public void onPostConstructComponent(final FXComponentLayout layout, final ResourceBundle resourceBundle) {
        service = new BaseWorker() {
            @Override
            protected Task createTask() {
                return new Task() {
                    @Override
                    protected Object call() throws Exception {
                        multicastReceiver.receiveMessage(x -> reloadTableData());
                        return null;
                    }
                };
            }
        };
        service.start();
    }

    @PreDestroy
    public void onPreDestroyComponent(final FXComponentLayout arg0) {
        if (Objects.nonNull(service)) {
            service.cancel();
        }
    }

    @Override
    public Node postHandle(Node node, Message<Event, Object> message) throws Exception {
        if (message.getMessageBody() instanceof GUIEventMessage) {
            GUIEventMessage event = (GUIEventMessage) message.getMessageBody();
            setCurrentEventID(event.getMessageID());
            setCurrentEventParams(event.getParams());

            switch (event.getMessageID()) {
                case OPEN_PROJECT_FORM_MESSAGE:
                    openProjectForm(event);
                    break;
                case OPEN_PROJECT_LIST_MESSAGE:
                    openProjectList();
                    break;
                case OPEN_INTERNAL_ERROR_MESSAGE:
                    openInternalError();
                    break;
                default:
                    break;
            }
        }
        return null;
    }

    @Override
    public Node handle(Message<Event, Object> message) throws Exception {
        return centerPane;
    }

    private void reloadTableData() {
        if (!StringUtils.equals(currentEventID, OPEN_PROJECT_LIST_MESSAGE) && Objects.isNull(projectListController)) {
            return;
        }
        projectListController.loadProjectsData();
    }

    private void openProjectForm(GUIEventMessage event) {
        if (!CollectionUtils.isEmpty(event.getParams())) {
            openUpdateForm(event);
            return;
        }
        ManagedFragmentHandler<ProjectFormFragment> projectFragment = context.getManagedFragmentHandler(ProjectFormFragment.class);
        centerPane.getChildren().clear();
        centerPane.getChildren().addAll(projectFragment.getFragmentNode());
    }

    private void openUpdateForm(GUIEventMessage event) {
        int updateProjectNum = (int) event.getParams().get(UPDATE_PROJECT_NUMBER);
        String updateProjectStatus = (String) event.getParams().get(UPDATE_PROJECT_STATUS);

        // Fill in existing info to gui input fields
        grpcHandling(context, () -> {
            PimProjectQueryResponse response = projectGRPCService.sendingRPCRequest(PimProjectQueryRequest.newBuilder()
                    .setTransactionID(UUID.randomUUID().toString())
                    .setSearchContent(String.valueOf(updateProjectNum))
                    .setStatus(GUIStatusModel.valueOfStatusValue(updateProjectStatus).status.name())
                    .build());
            fillInExistingInfo(response);
        });
    }

    private void openProjectList() {
        context.send(JacpFXConfiguration.LEFT_COMPONENT_ID, new GUIEventMessage().setMessageID(CLOSE_CHILD_COMPONENTS_MESSAGE));
        ManagedFragmentHandler<ProjectListFragment> projectListFragment = context.getManagedFragmentHandler(ProjectListFragment.class);
        projectListController = projectListFragment.getController();
        centerPane.getChildren().clear();
        centerPane.getChildren().addAll(projectListFragment.getFragmentNode());
    }

    private void openInternalError() {
        ManagedFragmentHandler<InternalErrorFragment> internalErrorFragment = context.getManagedFragmentHandler(InternalErrorFragment.class);
        centerPane.getChildren().clear();
        centerPane.getChildren().addAll(internalErrorFragment.getFragmentNode());
    }

    private void fillInExistingInfo(PimProjectQueryResponse response) {
        if (Objects.isNull(response) || !response.getIsSuccess()
                || CollectionUtils.isEmpty(response.getProjectList())) {
            return;
        }
        ManagedFragmentHandler<ProjectFormFragment> projectFragment = context.getManagedFragmentHandler(ProjectFormFragment.class);
        ProjectFormFragment projectForm = projectFragment.getController();

        PimProject currentEditProject = response.getProjectList().get(0);
        projectForm.setUpdateForm(true);
        // Get input fields of fragments
        TextField tfProjectNum = projectForm.getTfProjectNum();
        TextField tfProjectName = projectForm.getTfProjectName();
        TextField tfCustomer = projectForm.getTfCustomer();
        TagBar tagBar = projectForm.getTagBarWithListView().getTagBar();
        DatePicker dpStartDate = projectForm.getDpStartDate();
        DatePicker dpEndDate = projectForm.getDpEndDate();
        ChoiceBox cbStatus = projectForm.getCbStatus();
        ChoiceBox cbGroup = projectForm.getCbGroup();
        Button btnSubmit = projectForm.getBtnSubmit();
        Label lbProject = projectForm.getLbProject();

        tfProjectNum.setDisable(true);
        lbProject.setText(GuiUtils.getAndResolveBundleResource(context.getResourceBundle(), LABEL_UPDATE_PROJECT_BUNDLE_ID));
        btnSubmit.setText(GuiUtils.getAndResolveBundleResource(context.getResourceBundle(), PROJECT_BUTTON_UPDATE_SUBMIT_BUNDLE_ID));
        tfProjectNum.setText(String.valueOf(currentEditProject.getProjectNumber()));
        tfProjectName.setText(currentEditProject.getProjectName());
        tfCustomer.setText(currentEditProject.getCustomer());
        tagBar.getTagItems().clear();
        tagBar.getTagItems().addAll(lookupVisaMembers(currentEditProject.getMemberVISAsList()));
        dpStartDate.setValue(PIMToolUtils.convertToLocalDate(currentEditProject.getStartDate()));
        dpEndDate.setValue(PIMToolUtils.convertToLocalDate(currentEditProject.getEndDate()));
        cbStatus.getSelectionModel().select(GUIStatusModel.valueOfStatus(currentEditProject.getStatus()));
        Optional selectGroup = cbGroup.getItems().stream().filter(x -> ((PimGroup) x).getGroupID() == currentEditProject.getGroupID()).findFirst();
        if (selectGroup.isPresent()) {
            cbGroup.getSelectionModel().select(selectGroup.get());
        }

        centerPane.getChildren().clear();
        centerPane.getChildren().addAll(projectFragment.getFragmentNode());
    }

    private List<String> lookupVisaMembers(List<String> visas) {
        List<String> dataItems = new ArrayList<>();
        grpcHandling(context, () -> {
            PimEmployeeQueryResponse response = employeeGRPCService.sendingRPCRequest(PimEmployeeQueryRequest.newBuilder()
                    .setTransactionID(UUID.randomUUID().toString())
                    .addAllVisas(visas)
                    .build());
            if (Objects.isNull(response) || !response.getIsSuccess()) {
                return;
            }
            dataItems.addAll(GuiUtils.transformToTextFieldValue(response.getEmployeesList()));
        });
        return dataItems;
    }

    public static String getCurrentEventID() {
        return currentEventID;
    }

    public static void setCurrentEventID(String currentEventID) {
        CenterComponent.currentEventID = currentEventID;
    }

    public static Map<String, Object> getCurrentEventParams() {
        return currentEventParams;
    }

    public static void setCurrentEventParams(Map<String, Object> currentEventParams) {
        CenterComponent.currentEventParams = currentEventParams;
    }
}
