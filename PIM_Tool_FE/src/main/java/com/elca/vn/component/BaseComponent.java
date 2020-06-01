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
import org.springframework.util.CollectionUtils;

import java.util.Arrays;
import java.util.List;

import static com.elca.vn.configuration.JacpFXConfiguration.OPEN_INTERNAL_ERROR_MESSAGE;
import static com.elca.vn.constant.StylesheetConstant.STYLE_BORDER_FAILED;

/**
 * Fragment event for loading status data
 */
public interface BaseComponent {

    default void loadStatusData(ChoiceBox choiceBox) {
        choiceBox.setConverter(new ChoiceBoxStatusStringConverter());
        choiceBox.setItems(FXCollections.observableList(Arrays.asList(GUIStatusModel.values())));
        choiceBox.getSelectionModel().selectFirst();
    }

    default void cleanUpInputForm(Control... controls) {
        GuiUtils.cleanUpDataForm(controls);
    }

    default boolean isInvalidForm(String styleForFailedRecords, Control... controls) {
        List<Control> inputFormComponents = Arrays.asList(controls);
        if (CollectionUtils.isEmpty(inputFormComponents)) {
            return false;
        }
        return GuiUtils.isEmptyGUIData(inputFormComponents, STYLE_BORDER_FAILED);
    }

    default void grpcHandling(Context context, GRPCHandling grpcHandling) {
        try {
            grpcHandling.execute();
        } catch (Exception e) {
            // If fail, redirect to internal error page
            context.send(JacpFXConfiguration.CENTER_COMPONENT_ID, new GUIEventMessage().setMessageID(OPEN_INTERNAL_ERROR_MESSAGE));
        }
    }

    interface GRPCHandling {
        void execute();
    }
}
