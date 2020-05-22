package com.elca.vn;

import com.elca.vn.configuration.UIConfiguration;
import com.elca.vn.workbench.PIMToolWorkbench;
import javafx.application.Application;
import javafx.stage.Stage;
import org.jacpfx.rcp.workbench.FXWorkbench;
import org.jacpfx.spring.launcher.AFXSpringJavaConfigLauncher;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@EnableAutoConfiguration
@SpringBootApplication
public class PIMToolFEApplication extends AFXSpringJavaConfigLauncher {

    public static void main(String[] args) {
        SpringApplication.run(PIMToolFEApplication.class, args);
        Application.launch(args);
    }

    @Override
    protected Class<?>[] getConfigClasses() {
        return new Class[]{UIConfiguration.class};
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

    }
}
