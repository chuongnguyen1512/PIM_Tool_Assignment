package com.elca.vn.component;

import javafx.event.Event;
import javafx.scene.Node;
import org.jacpfx.api.annotations.component.DeclarativeView;
import org.jacpfx.api.message.Message;
import org.jacpfx.rcp.component.FXComponent;

import static com.elca.vn.configuration.UIConfiguration.TOP_COMPONENT_FXML_URL;
import static com.elca.vn.configuration.UIConfiguration.TOP_COMPONENT_ID;
import static com.elca.vn.configuration.UIConfiguration.TOP_COMPONENT_NAME;
import static com.elca.vn.configuration.UIConfiguration.TOP_COMPONENT_TARGET_LAYOUT_ID;

/**
 * Component for top layout. Prefer FXML file @{link TOP_COMPONENT_FXML_URL}
 */
@DeclarativeView(id = TOP_COMPONENT_ID,
        name = TOP_COMPONENT_NAME,
        viewLocation = TOP_COMPONENT_FXML_URL,
        initialTargetLayoutId = TOP_COMPONENT_TARGET_LAYOUT_ID)
public class TopComponent implements FXComponent {

    @Override
    public Node postHandle(Node node, Message<Event, Object> message) throws Exception {
        return null;
    }

    @Override
    public Node handle(Message<Event, Object> message) throws Exception {
        return null;
    }
}
