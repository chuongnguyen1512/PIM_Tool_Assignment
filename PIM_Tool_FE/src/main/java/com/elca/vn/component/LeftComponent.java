package com.elca.vn.component;

import com.elca.vn.configuration.JacpFXConfiguration;
import com.elca.vn.model.GUIEventMessage;
import javafx.event.Event;
import javafx.event.EventType;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBase;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Paint;
import javafx.scene.text.Font;
import org.jacpfx.api.annotations.Resource;
import org.jacpfx.api.annotations.component.DeclarativeView;
import org.jacpfx.api.message.Message;
import org.jacpfx.rcp.component.FXComponent;
import org.jacpfx.rcp.context.Context;

import java.net.URL;
import java.util.ResourceBundle;

import static com.elca.vn.configuration.JacpFXConfiguration.CLOSE_CHILD_COMPONENTS_MESSAGE;
import static com.elca.vn.configuration.JacpFXConfiguration.DEFAULT_RESOURCE_BUNDLE;
import static com.elca.vn.configuration.JacpFXConfiguration.OPEN_PROJECT_INSERT_FORM_MESSAGE;
import static com.elca.vn.configuration.JacpFXConfiguration.OPEN_PROJECT_LIST_MESSAGE;
import static com.elca.vn.constant.StylesheetConstant.STYLE_FONT_COLOR;
import static com.elca.vn.constant.StylesheetConstant.STYLE_FONT_COLOR_WHEN_CURSOR_POINTING;

/**
 * Component for left layout. Prefer for FXML file {@link JacpFXConfiguration.LEFT_COMPONENT_FXML_URL}
 */
@DeclarativeView(id = JacpFXConfiguration.LEFT_COMPONENT_ID,
        name = JacpFXConfiguration.LEFT_COMPONENT_NAME,
        viewLocation = JacpFXConfiguration.LEFT_COMPONENT_FXML_URL,
        initialTargetLayoutId = JacpFXConfiguration.LEFT_COMPONENT_TARGET_LAYOUT_ID,
        resourceBundleLocation = DEFAULT_RESOURCE_BUNDLE)
public class LeftComponent implements FXComponent, Initializable {

    @Resource
    private Context context;

    @FXML
    private VBox boxLeft;

    @FXML
    private Button btnOpenProject;

    @FXML
    private Button btnNew;

    @FXML
    private Button btnNewProject;

    @FXML
    private VBox vboxChildNew;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Event for button new
        btnNew.setOnMouseExited(x -> mouseEventForButton(btnNew, MouseEvent.MOUSE_EXITED, true));
        btnNew.setOnMouseClicked(x -> vboxChildNew.setVisible(true));
    }

    @Override
    public Node postHandle(Node node, Message<Event, Object> message) throws Exception {
        switch ((String) message.getMessageBody()) {
            case CLOSE_CHILD_COMPONENTS_MESSAGE:
                closeChildComponents();
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

    /**
     * Handle event when we click on project list button.
     * Send message to center component to open project list fragment.
     *
     * @param e event
     */
    @FXML
    public void openProjectList(Event e) {
        context.send(JacpFXConfiguration.CENTER_COMPONENT_ID, new GUIEventMessage().setMessageID(OPEN_PROJECT_LIST_MESSAGE));
    }

    /**
     * Handle event when we click on new project menu item.
     * Send message to center component to open project form fragment.
     *
     * @param e event
     */
    @FXML
    public void openNewProject(Event e) {
        context.send(JacpFXConfiguration.CENTER_COMPONENT_ID, new GUIEventMessage().setMessageID(OPEN_PROJECT_INSERT_FORM_MESSAGE));
    }

    private void closeChildComponents() {
        vboxChildNew.setVisible(false);
    }

    private void mouseEventForButton(ButtonBase btn, EventType mouseEvent, boolean isParentButton) {
        if (mouseEvent.equals(MouseEvent.MOUSE_CLICKED)) {
            btn.setTextFill(Paint.valueOf(STYLE_FONT_COLOR_WHEN_CURSOR_POINTING));
            if (isParentButton) {
                btn.setFont(new Font("System Bold", 18));
            } else {
                btn.setFont(new Font("System Bold", 14));
            }
        }
        if (mouseEvent.equals(MouseEvent.MOUSE_EXITED)) {
            if (isParentButton) {
                btn.setFont(new Font("System", 18));
            } else {
                btn.setFont(new Font("System", 14));
            }
            btn.setTextFill(Paint.valueOf(STYLE_FONT_COLOR));
        }
    }
}
