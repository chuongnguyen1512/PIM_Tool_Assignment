package com.elca.vn.config;

import com.elca.vn.entity.Employee;
import com.elca.vn.entity.Group;
import com.elca.vn.entity.Project;
import com.elca.vn.grpc.EmployeeGRPCService;
import com.elca.vn.grpc.GroupGRPCService;
import com.elca.vn.grpc.ProjectGRPCService;
import com.elca.vn.proto.model.PimEmployee;
import com.elca.vn.proto.model.PimGroup;
import com.elca.vn.proto.model.PimProject;
import com.elca.vn.repository.EmployeeRepository;
import com.elca.vn.repository.GroupRepository;
import com.elca.vn.repository.ProjectRepository;
import com.elca.vn.service.BasePimDataService;
import com.elca.vn.service.PimEmployeeServiceImpl;
import com.elca.vn.service.PimGroupServiceImpl;
import com.elca.vn.service.PimProjectServiceImpl;
import com.elca.vn.socket.MulticastPublisher;
import com.elca.vn.transform.BaseTransformService;
import com.elca.vn.transform.EmployeeTransformServiceImpl;
import com.elca.vn.transform.GroupTransformServiceImpl;
import com.elca.vn.transform.ProjectTransformServiceImpl;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class PIMToolBEConfiguration {

    public static final String BEAN_PROJECT_GRPC_SERVICE = "BEAN_PROJECT_GRPC_SERVICE";
    public static final String BEAN_PROJECT_SERVICE = "BEAN_PROJECT_SERVICE";
    public static final String BEAN_PROJECT_TRANSFORM_SERVICE = "BEAN_PROJECT_TRANSFORM_SERVICE";

    public static final String BEAN_GROUP_GRPC_SERVICE = "BEAN_GROUP_GRPC_SERVICE";
    public static final String BEAN_GROUP_SERVICE = "BEAN_GROUP_SERVICE";
    public static final String BEAN_GROUP_TRANSFORM_SERVICE = "BEAN_GROUP_TRANSFORM_SERVICE";

    public static final String BEAN_EMPLOYEE_GRPC_SERVICE = "BEAN_EMPLOYEE_GRPC_SERVICE";
    public static final String BEAN_EMPLOYEE_SERVICE = "BEAN_EMPLOYEE_SERVICE";
    public static final String BEAN_EMPLOYEE_TRANSFORM_SERVICE = "BEAN_EMPLOYEE_TRANSFORM_SERVICE";

    public static final String BEAN_MULTICAST_PUBLISHER = "BEAN_MULTICAST_PUBLISHER";

    @Bean(BEAN_PROJECT_TRANSFORM_SERVICE)
    public BaseTransformService<PimProject, Project> projectTransformService() {
        return new ProjectTransformServiceImpl();
    }

    @Bean(BEAN_PROJECT_SERVICE)
    public BasePimDataService<Project> projectServiceImpl(@Autowired ProjectRepository projectRepository,
                                                          @Autowired EmployeeRepository employeeRepository) {
        return new PimProjectServiceImpl(projectRepository, employeeRepository);
    }

    @Bean(BEAN_PROJECT_GRPC_SERVICE)
    public ProjectGRPCService projectGRPCService(@Qualifier(BEAN_PROJECT_SERVICE) BasePimDataService<Project> projectServiceImpl,
                                                 @Qualifier(BEAN_PROJECT_TRANSFORM_SERVICE) BaseTransformService<PimProject, Project> projectTransformService,
                                                 @Value("${executor.service.poolsize}") int executorServicePoolSize,
                                                 @Autowired MulticastPublisher multicastPublisher,
                                                 @Autowired Gson gson) {
        return new ProjectGRPCService(projectServiceImpl, projectTransformService, executorServicePoolSize, multicastPublisher, gson);
    }

    @Bean(BEAN_GROUP_TRANSFORM_SERVICE)
    public BaseTransformService<PimGroup, Group> groupTransformService() {
        return new GroupTransformServiceImpl();
    }

    @Bean(BEAN_GROUP_SERVICE)
    public BasePimDataService<Group> groupServiceImpl(@Autowired GroupRepository groupRepository) {
        return new PimGroupServiceImpl(groupRepository);
    }

    @Bean(BEAN_GROUP_GRPC_SERVICE)
    public GroupGRPCService groupGRPCService(@Qualifier(BEAN_GROUP_SERVICE) BasePimDataService<Group> groupServiceImpl,
                                             @Qualifier(BEAN_GROUP_TRANSFORM_SERVICE) BaseTransformService<PimGroup, Group> groupTransformService) {
        return new GroupGRPCService(groupServiceImpl, groupTransformService);
    }

    @Bean(BEAN_EMPLOYEE_TRANSFORM_SERVICE)
    public BaseTransformService<PimEmployee, Employee> employeeTransformService() {
        return new EmployeeTransformServiceImpl();
    }

    @Bean(BEAN_EMPLOYEE_SERVICE)
    public BasePimDataService<Employee> employeeServiceImpl(@Autowired EmployeeRepository employeeRepository) {
        return new PimEmployeeServiceImpl(employeeRepository);
    }

    @Bean(BEAN_EMPLOYEE_GRPC_SERVICE)
    public EmployeeGRPCService employeeGRPCService(@Qualifier(BEAN_EMPLOYEE_SERVICE) BasePimDataService<Employee> employeeServiceImpl,
                                                   @Qualifier(BEAN_EMPLOYEE_TRANSFORM_SERVICE) BaseTransformService<PimEmployee, Employee> employeeTransformService) {
        return new EmployeeGRPCService(employeeServiceImpl, employeeTransformService);
    }

    @Bean
    public Gson gson() {
        return new GsonBuilder().disableHtmlEscaping().create();
    }

    @Bean(BEAN_MULTICAST_PUBLISHER)
    public MulticastPublisher multicastPublisher(@Value("${multicast.inet.host}") String multicastInetHost,
                                                 @Value("${multicast.inet.port}") int multicastInetPort) {
        return new MulticastPublisher(multicastInetHost, multicastInetPort);
    }
}
