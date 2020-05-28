package com.elca.vn.component;

import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import org.jacpfx.api.annotations.component.DeclarativeView;
import org.jacpfx.api.message.Message;
import org.jacpfx.rcp.component.FXComponent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ResourceBundle;

import static com.elca.vn.configuration.JacpFXConfiguration.DEFAULT_RESOURCE_BUNDLE;
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
public class TopComponent implements FXComponent, Initializable {

    private static final Logger LOGGER = LoggerFactory.getLogger(TopComponent.class);

    @FXML
    private HBox rootBox;

    @FXML
    private ImageView imageLogo;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            Image image = new Image(new File("src/main/resources/images/elca-logo.png").toURI().toURL().toExternalForm());
            imageLogo.setImage(image);
        } catch (MalformedURLException e) {
            LOGGER.warn("Cannot load Elca logo");
        }
    }

    @Override
    public Node postHandle(Node node, Message<Event, Object> message) throws Exception {
        return null;
    }

    @Override
    public Node handle(Message<Event, Object> message) throws Exception {
        return null;
    }
}
