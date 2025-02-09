package com.elca.vn;

import com.elca.vn.configuration.JacpFXConfiguration;
import com.elca.vn.configuration.PIMAppConfiguration;
import com.elca.vn.workbench.PIMToolWorkbench;
import javafx.application.Application;
import javafx.stage.Stage;
import org.jacpfx.rcp.workbench.FXWorkbench;
import org.jacpfx.spring.launcher.AFXSpringJavaConfigLauncher;

public class PIMToolFEApplication extends AFXSpringJavaConfigLauncher {

    public static void main(String[] args) {
        Application.launch(PIMToolFEApplication.class, args);
    }

    @Override
    protected Class<?>[] getConfigClasses() {
        return new Class[]{
                JacpFXConfiguration.class,
                PIMAppConfiguration.class
        };
    }

    @Override
    protected Class<? extends FXWorkbench> getWorkbenchClass() {
        return PIMToolWorkbench.class;
    }

    @Override
    protected String[] getBasePackages() {
        return new String[]{"com.elca.vn"};
    }

    @Override
    protected void postInit(Stage stage) {
        stage.getScene().getStylesheets()
                .addAll(this.getClass().getResource("/styles/pim_common.css").toExternalForm(),
                        this.getClass().getResource("/styles/top_component.css").toExternalForm(),
                        this.getClass().getResource("/styles/left_component.css").toExternalForm(),
                        this.getClass().getResource("/styles/project_form.css").toExternalForm(),
                        this.getClass().getResource("/styles/project_list.css").toExternalForm());
    }
}
