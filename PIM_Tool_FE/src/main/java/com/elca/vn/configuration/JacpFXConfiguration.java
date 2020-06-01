package com.elca.vn.configuration;

import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

/**
 * Configuration class for UI components
 */
@Configuration
@Component
public class JacpFXConfiguration {

    // Workbench
    public static final String PIM_TOOL_WORKBENCH_ID = "Pim_Workbench_ID";
    public static final String PIM_TOOL_WORKBENCH_NAME = "Pim_Workbench_Name";

    // Perspectives
    public static final String MAIN_PERSPECTIVE_ID = "Main_Perspective_ID";
    public static final String MAIN_PERSPECTIVE_NAME = "Main_Perspective_Name";
    public static final String MAIN_PERSPECTIVE_FXML_URL = "/fxml/perspective/Main_Perspective.fxml";

    //Component
    public static final String TOP_COMPONENT_ID = "Top_Component_ID";
    public static final String TOP_COMPONENT_NAME = "Top_Component_Name";
    public static final String TOP_COMPONENT_TARGET_LAYOUT_ID = "Top_Component_Target_Layout_ID";
    public static final String TOP_COMPONENT_FXML_URL = "/fxml/component/Top_Component.fxml";

    public static final String LEFT_COMPONENT_ID = "Left_Component_ID";
    public static final String LEFT_COMPONENT_NAME = "Left_Component_Name";
    public static final String LEFT_COMPONENT_TARGET_LAYOUT_ID = "Left_Component_Target_Layout_ID";
    public static final String LEFT_COMPONENT_FXML_URL = "/fxml/component/Left_Component.fxml";

    public static final String CENTER_COMPONENT_ID = "Center_Component_ID";
    public static final String CENTER_COMPONENT_NAME = "Center_Component_Name";
    public static final String CENTER_COMPONENT_TARGET_LAYOUT_ID = "Center_Component_Target_Layout_ID";
    public static final String CENTER_COMPONENT_FXML_URL = "/fxml/component/Center_Component.fxml";

    // Fragment
    public static final String PROJECT_FORM_FRAGMENT_ID = "Project_Form_Fragment_ID";
    public static final String PROJECT_FORM_FRAGMENT_FXML_URL = "/fxml/fragment/Project_Form_Fragment.fxml";

    public static final String PROJECT_LIST_FRAGMENT_ID = "Project_List_Fragment_ID";
    public static final String PROJECT_LIST_FRAGMENT_FXML_URL = "/fxml/fragment/Project_List_Fragment.fxml";

    public static final String INTERNAL_ERROR_FRAGMENT_ID = "Internal_Error_Fragment_ID";
    public static final String INTERNAL_ERROR_FRAGMENT_FXML_URL = "/fxml/fragment/Internal_Error_Fragment.fxml";

    // Command message event
    public static final String OPEN_PROJECT_UPDATE_FORM_MESSAGE = "OPEN_PROJECT_UPDATE_FORM_MESSAGE";
    public static final String UPDATE_PROJECT_NUMBER = "UPDATE_PROJECT_NUMBER";
    public static final String OPEN_PROJECT_INSERT_FORM_MESSAGE = "OPEN_PROJECT_INSERT_FORM_MESSAGE";
    public static final String OPEN_PROJECT_LIST_MESSAGE = "OPEN_PROJECT_LIST_MESSAGE";
    public static final String OPEN_INTERNAL_ERROR_MESSAGE = "OPEN_INTERNAL_ERROR_MESSAGE";
    public static final String CLOSE_CHILD_COMPONENTS_MESSAGE = "CLOSE_CHILD_COMPONENTS_MESSAGE";

    // Resource
    public static final String DEFAULT_RESOURCE_BUNDLE = "bundles.language";
    public static final String SEPARATOR_CHARACTER = ",";

}
