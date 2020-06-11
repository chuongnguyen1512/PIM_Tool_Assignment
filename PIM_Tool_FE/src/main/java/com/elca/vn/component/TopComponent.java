package com.elca.vn.component;

import com.elca.vn.fragment.TopMenuFragment;
import com.elca.vn.model.GUIEventMessage;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.layout.AnchorPane;
import org.jacpfx.api.annotations.Resource;
import org.jacpfx.api.annotations.component.DeclarativeView;
import org.jacpfx.api.annotations.lifecycle.PostConstruct;
import org.jacpfx.api.message.Message;
import org.jacpfx.rcp.component.FXComponent;
import org.jacpfx.rcp.componentLayout.FXComponentLayout;
import org.jacpfx.rcp.components.managedFragment.ManagedFragmentHandler;
import org.jacpfx.rcp.context.Context;

import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

import static com.elca.vn.configuration.JacpFXConfiguration.DEFAULT_RESOURCE_BUNDLE;
import static com.elca.vn.configuration.JacpFXConfiguration.RELOAD_TOP_MENU_MESSAGE;
import static com.elca.vn.configuration.JacpFXConfiguration.TOP_COMPONENT_FXML_URL;
import static com.elca.vn.configuration.JacpFXConfiguration.TOP_COMPONENT_ID;
import static com.elca.vn.configuration.JacpFXConfiguration.TOP_COMPONENT_NAME;
import static com.elca.vn.configuration.JacpFXConfiguration.TOP_COMPONENT_TARGET_LAYOUT_ID;

/**
 * Component for top layout. Prefer FXML file @{link TOP_COMPONENT_FXML_URL}
 */
@DeclarativeView(id = TOP_COMPONENT_ID,
        name = TOP_COMPONENT_NAME,
        viewLocation = TOP_COMPONENT_FXML_URL,
        initialTargetLayoutId = TOP_COMPONENT_TARGET_LAYOUT_ID,
        resourceBundleLocation = DEFAULT_RESOURCE_BUNDLE)
public class TopComponent extends BaseComponent implements FXComponent {

    private static String currentEventID = "";
    private static Map<String, Object> currentEventParams = new HashMap();

    @Resource
    private Context context;

    @FXML
    private AnchorPane topAnchorPane;

    @PostConstruct
    public void onStartComponent(final FXComponentLayout arg0, final ResourceBundle resourceBundle) {
        reloadTopMenu();
    }

    @Override
    public Node postHandle(Node node, Message<Event, Object> message) throws Exception {
        if (message.getMessageBody() instanceof GUIEventMessage) {
            GUIEventMessage event = (GUIEventMessage) message.getMessageBody();
            setCurrentEventID(event.getMessageID());
            setCurrentEventParams(event.getParams());

            switch (event.getMessageID()) {
                case RELOAD_TOP_MENU_MESSAGE:
                    reloadTopMenu();
                    break;
                default:
                    reloadTopMenu();
                    break;
            }
        }
        return null;
    }

    @Override
    public Node handle(Message<Event, Object> message) throws Exception {
        return null;
    }

    private void reloadTopMenu() {
        ManagedFragmentHandler<TopMenuFragment> topMenuFragmentHandler = context.getManagedFragmentHandler(TopMenuFragment.class);
        topAnchorPane.getChildren().clear();
        topAnchorPane.getChildren().addAll(topMenuFragmentHandler.getFragmentNode());
    }

    public static String getCurrentEventID() {
        return currentEventID;
    }

    public static void setCurrentEventID(String currentEventID) {
        TopComponent.currentEventID = currentEventID;
    }

    public static Map<String, Object> getCurrentEventParams() {
        return currentEventParams;
    }

    public static void setCurrentEventParams(Map<String, Object> currentEventParams) {
        TopComponent.currentEventParams = currentEventParams;
    }
}
