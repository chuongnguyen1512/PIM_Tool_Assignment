package com.elca.vn.component;

import com.elca.vn.configuration.JacpFXConfiguration;
import com.elca.vn.fragment.InternalErrorFragment;
import com.elca.vn.fragment.ProjectFormComponent;
import com.elca.vn.fragment.ProjectListComponent;
import com.elca.vn.model.GUIEventMessage;
import com.elca.vn.proto.model.PimProjectQueryRequest;
import com.elca.vn.proto.model.PimProjectQueryResponse;
import com.elca.vn.service.ProjectGRPCService;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import org.jacpfx.api.annotations.Resource;
import org.jacpfx.api.annotations.component.DeclarativeView;
import org.jacpfx.api.message.Message;
import org.jacpfx.rcp.component.FXComponent;
import org.jacpfx.rcp.components.managedFragment.ManagedFragmentHandler;
import org.jacpfx.rcp.context.Context;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.util.CollectionUtils;

import java.util.Objects;
import java.util.ResourceBundle;
import java.util.UUID;

import static com.elca.vn.configuration.JacpFXConfiguration.CENTER_COMPONENT_FXML_URL;
import static com.elca.vn.configuration.JacpFXConfiguration.CENTER_COMPONENT_ID;
import static com.elca.vn.configuration.JacpFXConfiguration.CENTER_COMPONENT_NAME;
import static com.elca.vn.configuration.JacpFXConfiguration.CENTER_COMPONENT_TARGET_LAYOUT_ID;
import static com.elca.vn.configuration.JacpFXConfiguration.CLOSE_CHILD_COMPONENTS_MESSAGE;
import static com.elca.vn.configuration.JacpFXConfiguration.DEFAULT_RESOURCE_BUNDLE;
import static com.elca.vn.configuration.JacpFXConfiguration.OPEN_INTERNAL_ERROR_MESSAGE;
import static com.elca.vn.configuration.JacpFXConfiguration.OPEN_PROJECT_INSERT_FORM_MESSAGE;
import static com.elca.vn.configuration.JacpFXConfiguration.OPEN_PROJECT_LIST_MESSAGE;
import static com.elca.vn.configuration.JacpFXConfiguration.OPEN_PROJECT_UPDATE_FORM_MESSAGE;
import static com.elca.vn.configuration.JacpFXConfiguration.UPDATE_PROJECT_NUMBER;
import static com.elca.vn.configuration.PIMAppConfiguration.BEAN_PROJECT_GRPC_SERVICE;

/**
 * Component for center layout. Prefer for FXML file {@link JacpFXConfiguration.CENTER_COMPONENT_FXML_URL}
 */
@DeclarativeView(id = CENTER_COMPONENT_ID,
        name = CENTER_COMPONENT_NAME,
        viewLocation = CENTER_COMPONENT_FXML_URL,
        initialTargetLayoutId = CENTER_COMPONENT_TARGET_LAYOUT_ID,
        resourceBundleLocation = DEFAULT_RESOURCE_BUNDLE)
public class CenterComponent implements FXComponent, BaseComponent {

    private final ProjectGRPCService projectGRPCService;

    @Autowired
    public CenterComponent(@Qualifier(BEAN_PROJECT_GRPC_SERVICE) ProjectGRPCService projectGRPCService) {
        this.projectGRPCService = projectGRPCService;
    }

    @Resource
    private Context context;

    @Resource
    private ResourceBundle bundle;

    @FXML
    private AnchorPane centerPane;

    @Override
    public Node postHandle(Node node, Message<Event, Object> message) throws Exception {
        if (message.getMessageBody() instanceof GUIEventMessage) {
            GUIEventMessage event = (GUIEventMessage) message.getMessageBody();
            switch (event.getMessageID()) {
                case OPEN_PROJECT_INSERT_FORM_MESSAGE:
                    openProjectForm(event);
                    break;
                case OPEN_PROJECT_UPDATE_FORM_MESSAGE:
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
        return null;
    }

    private void openProjectForm(GUIEventMessage event) {
        ManagedFragmentHandler<ProjectFormComponent> projectFragment = context.getManagedFragmentHandler(ProjectFormComponent.class);
        verifyUsingUpdateFormOrNot(event, projectFragment);
        centerPane.getChildren().clear();
        centerPane.getChildren().addAll(projectFragment.getFragmentNode());
    }

    private void verifyUsingUpdateFormOrNot(GUIEventMessage event, ManagedFragmentHandler<ProjectFormComponent> projectFragment) {
        if (CollectionUtils.isEmpty(event.getParams())) {
            return;
        }
        projectFragment.getController().setUpdateForm(true);
        AnchorPane rootNode = (AnchorPane) projectFragment.getFragmentNode();
        HBox hbox = (HBox) ((VBox) rootNode.getChildren().get(0)).getChildren().get(2);
        TextField tfProjectNum = (TextField) hbox.getChildren().get(2);
        String updateProjectNum = String.valueOf(event.getParams().get(UPDATE_PROJECT_NUMBER));
        tfProjectNum.setEditable(false);
        tfProjectNum.setText(updateProjectNum);
        grpcHandling(context, () -> {
            PimProjectQueryResponse response = projectGRPCService.sendingRPCRequest(PimProjectQueryRequest.newBuilder()
                    .setTransactionID(UUID.randomUUID().toString())
                    .setSearchContent(updateProjectNum)
                    .build());

            if (Objects.isNull(response) || !response.getIsSuccess()) {
                return;
            }
            // TODO: Load to field
        });

    }

    private void openProjectList() {
        context.send(JacpFXConfiguration.LEFT_COMPONENT_ID, CLOSE_CHILD_COMPONENTS_MESSAGE);
        ManagedFragmentHandler<ProjectListComponent> projectListFragment = context.getManagedFragmentHandler(ProjectListComponent.class);
        centerPane.getChildren().clear();
        centerPane.getChildren().addAll(projectListFragment.getFragmentNode());
    }

    private void openInternalError() {
        ManagedFragmentHandler<InternalErrorFragment> internalErrorFragment = context.getManagedFragmentHandler(InternalErrorFragment.class);
        centerPane.getChildren().clear();
        centerPane.getChildren().addAll(internalErrorFragment.getFragmentNode());
    }
}
