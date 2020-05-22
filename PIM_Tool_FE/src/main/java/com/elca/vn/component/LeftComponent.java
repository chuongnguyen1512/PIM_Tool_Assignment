package com.elca.vn.component;

import com.elca.vn.configuration.UIConfiguration;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.layout.GridPane;
import org.jacpfx.api.annotations.Resource;
import org.jacpfx.api.annotations.component.DeclarativeView;
import org.jacpfx.api.message.Message;
import org.jacpfx.rcp.component.FXComponent;
import org.jacpfx.rcp.context.Context;

import static com.elca.vn.configuration.UIConfiguration.OPEN_PROJECT_FORM_MESSAGE;
import static com.elca.vn.configuration.UIConfiguration.OPEN_PROJECT_LIST_MESSAGE;

/**
 * Component for left layout. Prefer for FXML file {@link UIConfiguration.LEFT_COMPONENT_FXML_URL}
 */
@DeclarativeView(id = UIConfiguration.LEFT_COMPONENT_ID,
        name = UIConfiguration.LEFT_COMPONENT_NAME,
        viewLocation = UIConfiguration.LEFT_COMPONENT_FXML_URL,
        initialTargetLayoutId = UIConfiguration.LEFT_COMPONENT_TARGET_LAYOUT_ID)
public class LeftComponent implements FXComponent {

    @Resource
    private Context context;

    @FXML
    private GridPane gridPane;

    @Override
    public Node postHandle(Node node, Message<Event, Object> message) throws Exception {
        return null;
    }

    @Override
    public Node handle(Message<Event, Object> message) throws Exception {
        return null;
    }

    /**
     * Handle event when we click on project list button.
     * Send message to center component to open project list fragment.
     *
     * @param e event
     */
    @FXML
    public void openProjectList(Event e) {
        context.send(UIConfiguration.CENTER_COMPONENT_ID, OPEN_PROJECT_LIST_MESSAGE);
    }

    /**
     * Handle event when we click on new project menu item.
     * Send message to center component to open project form fragment.
     *
     * @param e event
     */
    @FXML
    public void openProjectComponent(Event e) {
        context.send(UIConfiguration.CENTER_COMPONENT_ID, OPEN_PROJECT_FORM_MESSAGE);
    }
}
