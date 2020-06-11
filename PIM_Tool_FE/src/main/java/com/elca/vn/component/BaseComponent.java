package com.elca.vn.component;

import com.elca.vn.configuration.JacpFXConfiguration;
import com.elca.vn.converter.ChoiceBoxStatusStringConverter;
import com.elca.vn.model.GUIEventMessage;
import com.elca.vn.model.GUIStatusModel;
import com.elca.vn.util.GuiUtils;
import javafx.collections.FXCollections;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Control;
import org.jacpfx.rcp.context.Context;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.CollectionUtils;

import java.util.Arrays;
import java.util.List;

import static com.elca.vn.configuration.JacpFXConfiguration.CENTER_COMPONENT_ID;
import static com.elca.vn.configuration.JacpFXConfiguration.LEFT_COMPONENT_ID;
import static com.elca.vn.configuration.JacpFXConfiguration.OPEN_INTERNAL_ERROR_MESSAGE;
import static com.elca.vn.configuration.JacpFXConfiguration.TOP_COMPONENT_ID;

/**
 * Fragment event for loading status data
 */
public abstract class BaseComponent {

    private static final Logger LOGGER = LoggerFactory.getLogger(BaseComponent.class);

    public void loadStatusData(ChoiceBox choiceBox) {
        choiceBox.setConverter(new ChoiceBoxStatusStringConverter());
        choiceBox.setItems(FXCollections.observableList(Arrays.asList(GUIStatusModel.values())));
        choiceBox.getSelectionModel().selectFirst();
    }

    public synchronized void reloadComponents(Context context) {
        context.send(TOP_COMPONENT_ID, new GUIEventMessage()
                .setMessageID(TopComponent.getCurrentEventID())
                .setParams(TopComponent.getCurrentEventParams()));

        context.send(LEFT_COMPONENT_ID, new GUIEventMessage()
                .setMessageID(LeftComponent.getCurrentEventID())
                .setParams(LeftComponent.getCurrentEventParams()));

        context.send(CENTER_COMPONENT_ID, new GUIEventMessage()
                .setMessageID(CenterComponent.getCurrentEventID())
                .setParams(CenterComponent.getCurrentEventParams()));
    }

    public void cleanUpInputForm(Control... controls) {
        GuiUtils.cleanUpDataForm(controls);
    }

    public boolean isInvalidForm(String styleForFailedRecords, Control... controls) {
        List<Control> inputFormComponents = Arrays.asList(controls);
        if (CollectionUtils.isEmpty(inputFormComponents)) {
            return false;
        }
        return GuiUtils.isEmptyGUIData(inputFormComponents, styleForFailedRecords);
    }

    public void grpcHandling(Context context, GRPCHandling grpcHandling) {
        try {
            grpcHandling.execute();
        } catch (Exception e) {
            LOGGER.error("There is an exception when receiving RPC request", e);
            // If fail, redirect to internal error page
            context.send(JacpFXConfiguration.CENTER_COMPONENT_ID, new GUIEventMessage().setMessageID(OPEN_INTERNAL_ERROR_MESSAGE));
        }
    }

    public interface GRPCHandling {
        void execute();
    }
}
