package com.elca.vn.component;

import com.elca.vn.configuration.JacpFXConfiguration;
import com.elca.vn.fragment.LeftMenuFragment;
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

import static com.elca.vn.configuration.JacpFXConfiguration.CLOSE_CHILD_COMPONENTS_MESSAGE;
import static com.elca.vn.configuration.JacpFXConfiguration.DEFAULT_RESOURCE_BUNDLE;

/**
 * Component for left layout. Prefer for FXML file {@link JacpFXConfiguration.LEFT_COMPONENT_FXML_URL}
 */
@DeclarativeView(id = JacpFXConfiguration.LEFT_COMPONENT_ID,
        name = JacpFXConfiguration.LEFT_COMPONENT_NAME,
        viewLocation = JacpFXConfiguration.LEFT_COMPONENT_FXML_URL,
        initialTargetLayoutId = JacpFXConfiguration.LEFT_COMPONENT_TARGET_LAYOUT_ID,
        resourceBundleLocation = DEFAULT_RESOURCE_BUNDLE)
public class LeftComponent extends BaseComponent implements FXComponent {

    @Resource
    private Context context;

    @FXML
    private AnchorPane leftAnchorPane;

    private static String currentEventID = "";
    private static Map<String, Object> currentEventParams = new HashMap<>();

    @PostConstruct
    public void onStartComponent(final FXComponentLayout arg0, final ResourceBundle resourceBundle) {
        reloadLeftMenu();
    }

    @Override
    public Node postHandle(Node node, Message<Event, Object> message) throws Exception {
        if (message.getMessageBody() instanceof GUIEventMessage) {
            GUIEventMessage event = (GUIEventMessage) message.getMessageBody();
            setCurrentEventID(event.getMessageID());
            setCurrentEventParams(event.getParams());

            switch (event.getMessageID()) {
                case CLOSE_CHILD_COMPONENTS_MESSAGE:
                    closeChildComponents();
                    break;
                default:
                    reloadLeftMenu();
                    break;
            }
        }
        return null;
    }

    @Override
    public Node handle(Message<Event, Object> message) throws Exception {
        return null;
    }

    private void closeChildComponents() {
        ManagedFragmentHandler<LeftMenuFragment> leftMenuFragmentHandler = context.getManagedFragmentHandler(LeftMenuFragment.class);
        LeftMenuFragment leftMenu = leftMenuFragmentHandler.getController();
        leftMenu.getVboxChildNew().setVisible(false);
        leftAnchorPane.getChildren().clear();
        leftAnchorPane.getChildren().addAll(leftMenuFragmentHandler.getFragmentNode());
    }

    private void reloadLeftMenu() {
        ManagedFragmentHandler<LeftMenuFragment> leftMenuFragmentHandler = context.getManagedFragmentHandler(LeftMenuFragment.class);
        leftAnchorPane.getChildren().clear();
        leftAnchorPane.getChildren().addAll(leftMenuFragmentHandler.getFragmentNode());
    }

    public static String getCurrentEventID() {
        return currentEventID;
    }

    public static void setCurrentEventID(String currentEventID) {
        LeftComponent.currentEventID = currentEventID;
    }

    public static Map<String, Object> getCurrentEventParams() {
        return currentEventParams;
    }

    public static void setCurrentEventParams(Map<String, Object> currentEventParams) {
        LeftComponent.currentEventParams = currentEventParams;
    }
}
