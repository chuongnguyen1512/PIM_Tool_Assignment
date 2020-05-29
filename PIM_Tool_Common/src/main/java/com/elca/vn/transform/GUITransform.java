package com.elca.vn.transform;

import com.elca.vn.model.GUIProjectTableModel;
import com.elca.vn.model.GUIStatusModel;
import com.elca.vn.proto.model.PimProject;
import com.elca.vn.util.GuiUtils;
import com.elca.vn.util.PIMToolUtils;

import java.util.Objects;

public class GUITransform {

    private GUITransform() {
    }

    public static GUIProjectTableModel transformToGUIModel(PimProject pimProject) {
        if(Objects.isNull(pimProject)) {
            return null;
        }
        GUIProjectTableModel guiModel = new GUIProjectTableModel();
        guiModel.setCustomer(pimProject.getCustomer());
        guiModel.setProjectName(pimProject.getProjectName());
        guiModel.setProjectNumber(pimProject.getProjectNumber());
        guiModel.setStatus(GUIStatusModel.valueOfStatus(pimProject.getStatus()).statusValue);
        guiModel.setStartDate(PIMToolUtils.convertToText(pimProject.getStartDate()));
        return guiModel;
    }
}
