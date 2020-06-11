package com.elca.vn.fragment;

import com.elca.vn.component.BaseComponent;
import com.elca.vn.component.CenterComponent;
import com.elca.vn.component.LeftComponent;
import com.elca.vn.component.TopComponent;
import com.elca.vn.model.GUIEventMessage;
import com.elca.vn.util.ApplicationBundleUtils;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;
import org.jacpfx.api.annotations.Resource;
import org.jacpfx.api.annotations.fragment.Fragment;
import org.jacpfx.api.fragment.Scope;
import org.jacpfx.rcp.context.Context;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

import static com.elca.vn.configuration.JacpFXConfiguration.BUNDLE_LANGUAGES;
import static com.elca.vn.configuration.JacpFXConfiguration.CENTER_COMPONENT_ID;
import static com.elca.vn.configuration.JacpFXConfiguration.DEFAULT_RESOURCE_BUNDLE;
import static com.elca.vn.configuration.JacpFXConfiguration.LEFT_COMPONENT_ID;
import static com.elca.vn.configuration.JacpFXConfiguration.MAIN_PERSPECTIVE_ID;
import static com.elca.vn.configuration.JacpFXConfiguration.TOP_COMPONENT_ID;
import static com.elca.vn.configuration.JacpFXConfiguration.TOP_MENU_FRAGMENT_FXML_URL;
import static com.elca.vn.configuration.JacpFXConfiguration.TOP_MENU_FRAGMENT_ID;

@Fragment(id = TOP_MENU_FRAGMENT_ID,
        viewLocation = TOP_MENU_FRAGMENT_FXML_URL,
        scope = Scope.PROTOTYPE,
        resourceBundleLocation = DEFAULT_RESOURCE_BUNDLE)
public class TopMenuFragment extends BaseComponent implements Initializable {

    private static final Logger LOGGER = LoggerFactory.getLogger(TopMenuFragment.class);

    @Resource
    private Context context;

    @FXML
    @Getter
    @Setter
    private HBox rootBox;

    @FXML
    @Getter
    @Setter
    private HBox hboxLanguage;

    @FXML
    @Getter
    @Setter
    private ImageView imageLogo;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        ApplicationBundleUtils.reloadBundle(context, resources);
        try {
            Image image = new Image(new File("src/main/resources/images/elca-logo.png").toURI().toURL().toExternalForm());
            imageLogo.setImage(image);
        } catch (MalformedURLException e) {
            LOGGER.warn("Cannot load Elca logo");
        }
        ObservableList<Node> childs = hboxLanguage.getChildren();
        childs.addAll(createLanguageLabels());
    }

    private List<Label> createLanguageLabels() {
        List<Label> labelLanguages = new ArrayList();
        for (int i = 0; i < BUNDLE_LANGUAGES.size(); i++) {
            String language = BUNDLE_LANGUAGES.get(i);
            labelLanguages.add(registerNewLabelLanguage(language, true));
            if (i < BUNDLE_LANGUAGES.size() - 1) {
                labelLanguages.add(registerNewLabelLanguage("|", false));
            }
        }
        return labelLanguages;
    }

    private Label registerNewLabelLanguage(String language, boolean registerEvent) {
        Label label = new Label(language);
        label.paddingProperty().set(new Insets(0, 2, 0, 2));
        if (registerEvent) {
            label.setId("lb" + language);
            label.getStyleClass().add("lbLanguageCommon");
            label.addEventHandler(MouseEvent.MOUSE_CLICKED, x -> {
                changeLanguageAndReloadGui(language);
            });
        }
        return label;
    }

    private void changeLanguageAndReloadGui(String language) {
        if (StringUtils.isBlank(language)) {
            return;
        }
        Locale.setDefault(new Locale(language));
        reloadComponents(context);
    }

}
