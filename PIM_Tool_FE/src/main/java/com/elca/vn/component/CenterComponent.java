package com.elca.vn.component;

import com.elca.vn.configuration.UIConfiguration;
import com.elca.vn.fragment.ProjectFormFragment;
import com.elca.vn.fragment.ProjectListFragment;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.layout.GridPane;
import org.jacpfx.api.annotations.Resource;
import org.jacpfx.api.annotations.component.DeclarativeView;
import org.jacpfx.api.message.Message;
import org.jacpfx.rcp.component.FXComponent;
import org.jacpfx.rcp.components.managedFragment.ManagedFragmentHandler;
import org.jacpfx.rcp.context.Context;

import static com.elca.vn.configuration.UIConfiguration.CENTER_COMPONENT_FXML_URL;
import static com.elca.vn.configuration.UIConfiguration.CENTER_COMPONENT_ID;
import static com.elca.vn.configuration.UIConfiguration.CENTER_COMPONENT_NAME;
import static com.elca.vn.configuration.UIConfiguration.CENTER_COMPONENT_TARGET_LAYOUT_ID;
import static com.elca.vn.configuration.UIConfiguration.OPEN_PROJECT_FORM_MESSAGE;
import static com.elca.vn.configuration.UIConfiguration.OPEN_PROJECT_LIST_MESSAGE;

/**
 * Component for center layout. Prefer for FXML file {@link UIConfiguration.CENTER_COMPONENT_FXML_URL}
 */
@DeclarativeView(id = CENTER_COMPONENT_ID,
        name = CENTER_COMPONENT_NAME,
        viewLocation = CENTER_COMPONENT_FXML_URL,
        initialTargetLayoutId = CENTER_COMPONENT_TARGET_LAYOUT_ID)
public class CenterComponent implements FXComponent {

    @Resource
    private Context context;

    @FXML
    private GridPane gridPane;

    @Override
    public Node postHandle(Node node, Message<Event, Object> message) throws Exception {
        switch ((String) message.getMessageBody()) {
            case OPEN_PROJECT_FORM_MESSAGE:
                openProjectForm();
                break;
            case OPEN_PROJECT_LIST_MESSAGE:
                openProjectList();
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
        gridPane.getChildren().clear();
        gridPane.getChildren().addAll(projectFragment.getFragmentNode());
    }

    private void openProjectList() {
        ManagedFragmentHandler<ProjectListFragment> projectListFragment = context.getManagedFragmentHandler(ProjectListFragment.class);
        gridPane.getChildren().clear();
        gridPane.getChildren().addAll(projectListFragment.getFragmentNode());
    }
}
