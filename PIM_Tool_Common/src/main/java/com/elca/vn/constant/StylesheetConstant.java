package com.elca.vn.constant;

import com.elca.vn.model.GUIStatusModel;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Constant class for stylesheet css
 */
public class StylesheetConstant {
    private StylesheetConstant() {
    }

    public static final String STYLE_BORDER_FAILED = "-fx-border-color: #ee6c6c; -fx-border-radius: 5 5 5 5;";
    public static final String STYLE_FONT_COLOR_WHEN_CURSOR_POINTING = "#49a4ff";
    public static final String STYLE_FONT_COLOR = "Black";
    public static final String STYLE_DELETE_ICON = "-fx-graphic: url('%s'); -fx-background-color: transparent;";

    public static final int PAGING_SIZE = 5;
    public static final int PAGING_MAX_INDICATOR = 4;

    public static final Set<GUIStatusModel> ALLOW_DELETE_STATUS = new HashSet<>();

    static {
        ALLOW_DELETE_STATUS.add(GUIStatusModel.NEW);
    }

}
