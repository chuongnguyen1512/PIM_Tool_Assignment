package com.elca.vn.workbench;

import javafx.event.Event;
import javafx.scene.Node;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.jacpfx.api.annotations.workbench.Workbench;
import org.jacpfx.api.componentLayout.WorkbenchLayout;
import org.jacpfx.api.message.Message;
import org.jacpfx.api.util.ToolbarPosition;
import org.jacpfx.rcp.componentLayout.FXComponentLayout;
import org.jacpfx.rcp.workbench.FXWorkbench;

import static com.elca.vn.configuration.UIConfiguration.MAIN_PERSPECTIVE_ID;
import static com.elca.vn.configuration.UIConfiguration.PIM_TOOL_WORKBENCH_ID;
import static com.elca.vn.configuration.UIConfiguration.PIM_TOOL_WORKBENCH_NAME;

/**
 * Workbench of PIM Tool UI which also is present for main window application
 */
@Workbench(id = PIM_TOOL_WORKBENCH_ID, name = PIM_TOOL_WORKBENCH_NAME,
        perspectives = {
                MAIN_PERSPECTIVE_ID
        })
public class PIMToolWorkbench implements FXWorkbench {

    /**
     * Handle after start up application
     *
     * @param layout app layout
     */
    @Override
    public void postHandle(FXComponentLayout layout) {
        // do nothing
    }

    /**
     * Handle when application is start up
     *
     * @param action message event
     * @param layout app layout
     * @param stage  primary stage
     */
    @Override
    public void handleInitialLayout(Message<Event, Object> action, WorkbenchLayout<Node> layout, Stage stage) {
        layout.setWorkbenchXYSize(1024, 768);
        layout.registerToolBar(ToolbarPosition.NORTH);
        layout.setStyle(StageStyle.DECORATED);
        stage.setTitle("PIM Tool");
    }
}
