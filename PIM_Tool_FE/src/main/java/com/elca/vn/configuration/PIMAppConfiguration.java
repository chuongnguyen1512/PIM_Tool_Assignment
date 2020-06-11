package com.elca.vn.configuration;

import com.elca.vn.service.EmployeeGRPCService;
import com.elca.vn.service.GroupGRPCService;
import com.elca.vn.service.ProjectGRPCService;
import com.elca.vn.socket.MulticastReceiver;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@PropertySource("classpath:application.properties")
public class PIMAppConfiguration {

    public static final String BEAN_PROJECT_GRPC_SERVICE = "BEAN_PROJECT_GRPC_SERVICE";
    public static final String BEAN_GROUP_GRPC_SERVICE = "BEAN_GROUP_GRPC_SERVICE";
    public static final String BEAN_EMPLOYEE_GRPC_SERVICE = "BEAN_EMPLOYEE_GRPC_SERVICE";
    public static final String BEAN_MULTICAST_RECEIVER = "BEAN_MULTICAST_RECEIVER";

    @Value("${pim.beapp.host}")
    private String beAppHost;

    @Value("${pim.beapp.port}")
    private Integer beAppPort;

    @Value("${multicast.inet.host}")
    private String multicastHost;

    @Value("${multicast.inet.port}")
    private Integer multicastPort;

    @Bean(BEAN_PROJECT_GRPC_SERVICE)
    public ProjectGRPCService projectGRPCService() {
        return new ProjectGRPCService(beAppHost, beAppPort);
    }

    @Bean(BEAN_GROUP_GRPC_SERVICE)
    public GroupGRPCService groupGRPCService() {
        return new GroupGRPCService(beAppHost, beAppPort);
    }

    @Bean(BEAN_EMPLOYEE_GRPC_SERVICE)
    public EmployeeGRPCService employeeGRPCService() {
        return new EmployeeGRPCService(beAppHost, beAppPort);
    }

    @Bean(BEAN_MULTICAST_RECEIVER)
    public MulticastReceiver multicastReceiver() {
        return new MulticastReceiver(multicastHost, multicastPort);
    }

    @Bean
    public Gson gson() {
        return new GsonBuilder().disableHtmlEscaping().create();
    }
}
