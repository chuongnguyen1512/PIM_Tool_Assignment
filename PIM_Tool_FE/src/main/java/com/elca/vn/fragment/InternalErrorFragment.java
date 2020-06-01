package com.elca.vn.fragment;

import com.elca.vn.configuration.JacpFXConfiguration;
import com.elca.vn.model.GUIEventMessage;
import javafx.event.EventType;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Cursor;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Font;
import org.jacpfx.api.annotations.Resource;
import org.jacpfx.api.annotations.fragment.Fragment;
import org.jacpfx.api.fragment.Scope;
import org.jacpfx.rcp.context.Context;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ResourceBundle;

import static com.elca.vn.configuration.JacpFXConfiguration.DEFAULT_RESOURCE_BUNDLE;
import static com.elca.vn.configuration.JacpFXConfiguration.INTERNAL_ERROR_FRAGMENT_FXML_URL;
import static com.elca.vn.configuration.JacpFXConfiguration.INTERNAL_ERROR_FRAGMENT_ID;
import static com.elca.vn.configuration.JacpFXConfiguration.OPEN_PROJECT_LIST_MESSAGE;

/**
 * Fragment class for showing internal error when we are facing exceptions
 */
@Fragment(id = INTERNAL_ERROR_FRAGMENT_ID,
        viewLocation = INTERNAL_ERROR_FRAGMENT_FXML_URL,
        scope = Scope.PROTOTYPE,
        resourceBundleLocation = DEFAULT_RESOURCE_BUNDLE)
public class InternalErrorFragment implements Initializable {

    private static final Logger LOGGER = LoggerFactory.getLogger(InternalErrorFragment.class);

    @FXML
    private ImageView imgViewErrorIcon;

    @FXML
    private Label lbBackToProjectList;

    @Resource
    private Context context;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            imgViewErrorIcon.setImage(new Image(new File("src/main/resources/images/error_icon.png").toURI().toURL().toExternalForm()));
        } catch (MalformedURLException e) {
            LOGGER.warn("Cannot load error icon");
        }

        lbBackToProjectList.setOnMouseEntered(x -> mouseEventForLabelBackToProjectList(MouseEvent.MOUSE_ENTERED));
        lbBackToProjectList.setOnMouseExited(x -> mouseEventForLabelBackToProjectList(MouseEvent.MOUSE_EXITED));
        lbBackToProjectList.setOnMouseClicked(x -> mouseEventForLabelBackToProjectList(MouseEvent.MOUSE_CLICKED));
    }

    private void mouseEventForLabelBackToProjectList(EventType mouseEvent) {
        if (mouseEvent.equals(MouseEvent.MOUSE_ENTERED)) {
            lbBackToProjectList.getScene().setCursor(Cursor.HAND);
            lbBackToProjectList.setUnderline(true);
        }

        if (mouseEvent.equals(MouseEvent.MOUSE_EXITED)) {
            lbBackToProjectList.getScene().setCursor(null);
            lbBackToProjectList.setUnderline(false);
        }

        if (mouseEvent.equals(MouseEvent.MOUSE_CLICKED)) {
            lbBackToProjectList.getScene().setCursor(null);
            context.send(JacpFXConfiguration.CENTER_COMPONENT_ID, new GUIEventMessage().setMessageID(OPEN_PROJECT_LIST_MESSAGE));
        }
    }
}
