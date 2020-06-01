package com.elca.vn.configuration;

import com.elca.vn.service.GroupGRPCService;
import com.elca.vn.service.ProjectGRPCService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@ComponentScan
@Configuration
@PropertySource("classpath:application.properties")
public class PIMAppConfiguration {

    public static final String BEAN_PROJECT_GRPC_SERVICE = "BEAN_PROJECT_GRPC_SERVICE";
    public static final String BEAN_GROUP_GRPC_SERVICE = "BEAN_GROUP_GRPC_SERVICE";

    @Value("${pim.beapp.host}")
    private String beAppHost;

    @Value("${pim.beapp.port}")
    private Integer beAppPort;

    @Bean(BEAN_PROJECT_GRPC_SERVICE)
    public ProjectGRPCService projectGRPCService() {
        return new ProjectGRPCService(beAppHost, beAppPort);
    }

    @Bean(BEAN_GROUP_GRPC_SERVICE)
    public GroupGRPCService groupGRPCService() {
        return new GroupGRPCService(beAppHost, beAppPort);
    }
}
