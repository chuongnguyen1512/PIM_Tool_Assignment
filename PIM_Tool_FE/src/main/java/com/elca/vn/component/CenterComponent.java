package com.elca.vn.component;

import com.elca.vn.configuration.JacpFXConfiguration;
import com.elca.vn.fragment.InternalErrorFragment;
import com.elca.vn.fragment.ProjectFormFragment;
import com.elca.vn.fragment.ProjectListFragment;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.layout.AnchorPane;
import org.jacpfx.api.annotations.Resource;
import org.jacpfx.api.annotations.component.DeclarativeView;
import org.jacpfx.api.message.Message;
import org.jacpfx.rcp.component.FXComponent;
import org.jacpfx.rcp.components.managedFragment.ManagedFragmentHandler;
import org.jacpfx.rcp.context.Context;

import java.util.ResourceBundle;

import static com.elca.vn.configuration.JacpFXConfiguration.CENTER_COMPONENT_FXML_URL;
import static com.elca.vn.configuration.JacpFXConfiguration.CENTER_COMPONENT_ID;
import static com.elca.vn.configuration.JacpFXConfiguration.CENTER_COMPONENT_NAME;
import static com.elca.vn.configuration.JacpFXConfiguration.CENTER_COMPONENT_TARGET_LAYOUT_ID;
import static com.elca.vn.configuration.JacpFXConfiguration.CLOSE_CHILD_COMPONENTS_MESSAGE;
import static com.elca.vn.configuration.JacpFXConfiguration.DEFAULT_RESOURCE_BUNDLE;
import static com.elca.vn.configuration.JacpFXConfiguration.OPEN_INTERNAL_ERROR_MESSAGE;
import static com.elca.vn.configuration.JacpFXConfiguration.OPEN_PROJECT_FORM_MESSAGE;
import static com.elca.vn.configuration.JacpFXConfiguration.OPEN_PROJECT_LIST_MESSAGE;

/**
 * Component for center layout. Prefer for FXML file {@link JacpFXConfiguration.CENTER_COMPONENT_FXML_URL}
 */
@DeclarativeView(id = CENTER_COMPONENT_ID,
        name = CENTER_COMPONENT_NAME,
        viewLocation = CENTER_COMPONENT_FXML_URL,
        initialTargetLayoutId = CENTER_COMPONENT_TARGET_LAYOUT_ID,
        resourceBundleLocation = DEFAULT_RESOURCE_BUNDLE)
public class CenterComponent implements FXComponent {

    @Resource
    private Context context;

    @Resource
    private ResourceBundle bundle;

    @FXML
    private AnchorPane centerPane;

    @Override
    public Node postHandle(Node node, Message<Event, Object> message) throws Exception {
        switch ((String) message.getMessageBody()) {
            case OPEN_PROJECT_FORM_MESSAGE:
                openProjectForm();
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
        return null;
    }

    @Override
    public Node handle(Message<Event, Object> message) throws Exception {
        return null;
    }

    private void openProjectForm() {
        ManagedFragmentHandler<ProjectFormFragment> projectFragment = context.getManagedFragmentHandler(ProjectFormFragment.class);
        verifyUsingUpdateFormOrNot(projectFragment);
        centerPane.getChildren().clear();
        centerPane.getChildren().addAll(projectFragment.getFragmentNode());
    }

    private void verifyUsingUpdateFormOrNot(ManagedFragmentHandler<ProjectFormFragment> projectFragment) {
        projectFragment.getController().setUpdateForm(false);

        // Look up ui components and change to update functional if needed
//        GridPane fragmentNode = (GridPane) projectFragment.getFragmentNode();
//        VBox itemContainer = (VBox) fragmentNode.getChildren().get(0);
//        HBox buttonContainer = (HBox) itemContainer.getChildren().get(15);
//        Button button = (Button) buttonContainer.getChildren().get(3);
//        button.setText(GuiUtils.getAndResolveBundleResource(bundle, PROJECT_BUTTON_UPDATE_SUBMIT_BUNDLE_ID));
    }

    private void openProjectList() {
        context.send(JacpFXConfiguration.LEFT_COMPONENT_ID, CLOSE_CHILD_COMPONENTS_MESSAGE);
        ManagedFragmentHandler<ProjectListFragment> projectListFragment = context.getManagedFragmentHandler(ProjectListFragment.class);
        centerPane.getChildren().clear();
        centerPane.getChildren().addAll(projectListFragment.getFragmentNode());
    }

    private void openInternalError() {
        ManagedFragmentHandler<InternalErrorFragment> internalErrorFragment = context.getManagedFragmentHandler(InternalErrorFragment.class);
        centerPane.getChildren().clear();
        centerPane.getChildren().addAll(internalErrorFragment.getFragmentNode());
    }
}
