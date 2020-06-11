package com.elca.vn.fragment;

import com.elca.vn.component.BaseComponent;
import com.elca.vn.configuration.JacpFXConfiguration;
import com.elca.vn.model.GUIEventMessage;
import com.elca.vn.util.ApplicationBundleUtils;
import javafx.event.Event;
import javafx.event.EventType;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBase;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Paint;
import javafx.scene.text.Font;
import lombok.Getter;
import lombok.Setter;
import org.jacpfx.api.annotations.Resource;
import org.jacpfx.api.annotations.fragment.Fragment;
import org.jacpfx.api.fragment.Scope;
import org.jacpfx.rcp.context.Context;

import java.net.URL;
import java.util.ResourceBundle;

import static com.elca.vn.configuration.JacpFXConfiguration.CENTER_COMPONENT_ID;
import static com.elca.vn.configuration.JacpFXConfiguration.DEFAULT_RESOURCE_BUNDLE;
import static com.elca.vn.configuration.JacpFXConfiguration.OPEN_PROJECT_FORM_MESSAGE;
import static com.elca.vn.configuration.JacpFXConfiguration.OPEN_PROJECT_LIST_MESSAGE;
import static com.elca.vn.constant.StylesheetConstant.STYLE_FONT_COLOR;

@Fragment(id = JacpFXConfiguration.LEFT_MENU_FRAGMENT_ID,
        viewLocation = JacpFXConfiguration.LEFT_MENU_FRAGMENT_FXML_URL,
        scope = Scope.PROTOTYPE,
        resourceBundleLocation = DEFAULT_RESOURCE_BUNDLE)
public class LeftMenuFragment extends BaseComponent implements Initializable {

    @Resource
    private Context context;

    @FXML
    @Getter
    @Setter
    private VBox boxLeft;

    @FXML
    @Getter
    @Setter
    private Button btnOpenProject;

    @FXML
    @Getter
    @Setter
    private Button btnNew;

    @FXML
    @Getter
    @Setter
    private Button btnNewProject;

    @FXML
    @Getter
    @Setter
    private VBox vboxChildNew;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        ApplicationBundleUtils.reloadBundle(context, resources);
        // Event for buttons
        btnOpenProject.setOnMouseExited(x -> mouseEventForButton(btnNew, MouseEvent.MOUSE_EXITED, true));
        btnNew.setOnMouseExited(x -> mouseEventForButton(btnNew, MouseEvent.MOUSE_EXITED, true));
        btnNew.setOnMouseClicked(x -> vboxChildNew.setVisible(true));
    }

    /**
     * Handle event when we click on project list button.
     * Send message to center component to open project list fragment.
     *
     * @param e event
     */
    @FXML
    public void openProjectList(Event e) {
        context.send(CENTER_COMPONENT_ID, new GUIEventMessage().setMessageID(OPEN_PROJECT_LIST_MESSAGE));
    }

    /**
     * Handle event when we click on new project menu item.
     * Send message to center component to open project form fragment.
     *
     * @param e event
     */
    @FXML
    public void openNewProject(Event e) {
        context.send(CENTER_COMPONENT_ID, new GUIEventMessage().setMessageID(OPEN_PROJECT_FORM_MESSAGE));
    }

    private void mouseEventForButton(ButtonBase btn, EventType mouseEvent, boolean isParentButton) {
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
