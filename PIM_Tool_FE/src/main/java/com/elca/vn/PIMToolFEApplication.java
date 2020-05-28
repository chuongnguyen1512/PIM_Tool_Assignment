package com.elca.vn;

import com.elca.vn.configuration.JacpFXConfiguration;
import com.elca.vn.workbench.PIMToolWorkbench;
import javafx.application.Application;
import javafx.stage.Stage;
import org.jacpfx.rcp.workbench.FXWorkbench;
import org.jacpfx.spring.launcher.AFXSpringJavaConfigLauncher;

public class PIMToolFEApplication extends AFXSpringJavaConfigLauncher {

    public static void main(String[] args) {
        Application.launch(args);
    }

    @Override
    protected Class<?>[] getConfigClasses() {
        return new Class[]{JacpFXConfiguration.class};
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
                .addAll(PIMToolFEApplication.class.getResource("/styles/pim_common.css").toExternalForm());
    }
}
