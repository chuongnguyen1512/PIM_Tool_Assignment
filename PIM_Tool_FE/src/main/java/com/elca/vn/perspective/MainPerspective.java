package com.elca.vn.perspective;

import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import org.jacpfx.api.annotations.lifecycle.PostConstruct;
import org.jacpfx.api.annotations.perspective.Perspective;
import org.jacpfx.api.message.Message;
import org.jacpfx.rcp.componentLayout.FXComponentLayout;
import org.jacpfx.rcp.componentLayout.PerspectiveLayout;
import org.jacpfx.rcp.perspective.FXPerspective;

import java.util.ResourceBundle;

import static com.elca.vn.configuration.UIConfiguration.CENTER_COMPONENT_ID;
import static com.elca.vn.configuration.UIConfiguration.CENTER_COMPONENT_TARGET_LAYOUT_ID;
import static com.elca.vn.configuration.UIConfiguration.DEFAULT_RESOURCE_BUNDLE;
import static com.elca.vn.configuration.UIConfiguration.LEFT_COMPONENT_ID;
import static com.elca.vn.configuration.UIConfiguration.LEFT_COMPONENT_TARGET_LAYOUT_ID;
import static com.elca.vn.configuration.UIConfiguration.MAIN_PERSPECTIVE_FXML_URL;
import static com.elca.vn.configuration.UIConfiguration.MAIN_PERSPECTIVE_ID;
import static com.elca.vn.configuration.UIConfiguration.MAIN_PERSPECTIVE_NAME;
import static com.elca.vn.configuration.UIConfiguration.TOP_COMPONENT_ID;
import static com.elca.vn.configuration.UIConfiguration.TOP_COMPONENT_TARGET_LAYOUT_ID;

/**
 * Main perspective layout for application
 */
@Perspective(id = MAIN_PERSPECTIVE_ID,
        name = MAIN_PERSPECTIVE_NAME,
        viewLocation = MAIN_PERSPECTIVE_FXML_URL,
        components = {
                TOP_COMPONENT_ID,
                LEFT_COMPONENT_ID,
                CENTER_COMPONENT_ID
        },
        resourceBundleLocation = DEFAULT_RESOURCE_BUNDLE)
public class MainPerspective implements FXPerspective {

    @FXML
    private BorderPane mainPane;

    @FXML
    private GridPane topGridPane;

    @FXML
    private GridPane leftGridPane;

    @FXML
    private GridPane centerGridPane;

    @Override
    public void handlePerspective(Message<Event, Object> message, PerspectiveLayout perspectiveLayout) {
        // Do nothing
    }

    @PostConstruct
    public void onStartPerspective(final PerspectiveLayout perspectiveLayout, final FXComponentLayout layout,
                                   final ResourceBundle resourceBundle) {
        perspectiveLayout.registerRootComponent(mainPane);
        perspectiveLayout.registerTargetLayoutComponent(TOP_COMPONENT_TARGET_LAYOUT_ID, topGridPane);
        perspectiveLayout.registerTargetLayoutComponent(LEFT_COMPONENT_TARGET_LAYOUT_ID, leftGridPane);
        perspectiveLayout.registerTargetLayoutComponent(CENTER_COMPONENT_TARGET_LAYOUT_ID, centerGridPane);
    }
}
