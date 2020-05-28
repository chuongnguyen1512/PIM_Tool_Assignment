package com.elca.vn.config;

import com.elca.vn.entity.Group;
import com.elca.vn.entity.Project;
import com.elca.vn.grpc.GroupGRPCService;
import com.elca.vn.grpc.ProjectGRPCService;
import com.elca.vn.proto.model.PimGroup;
import com.elca.vn.proto.model.PimProject;
import com.elca.vn.repository.EmployeeRepository;
import com.elca.vn.repository.GroupRepository;
import com.elca.vn.repository.ProjectRepository;
import com.elca.vn.service.BasePimDataService;
import com.elca.vn.service.PimGroupServiceImpl;
import com.elca.vn.service.PimProjectServiceImpl;
import com.elca.vn.transform.BaseTransformService;
import com.elca.vn.transform.GroupTransformServiceImpl;
import com.elca.vn.transform.ProjectTransformServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
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

    @Bean(BEAN_PROJECT_TRANSFORM_SERVICE)
    public BaseTransformService<PimProject, Project> projectTransformService() {
        return new ProjectTransformServiceImpl();
    }

    @Bean(BEAN_PROJECT_SERVICE)
    public BasePimDataService projectServiceImpl(@Autowired ProjectRepository projectRepository,
                                                 @Autowired EmployeeRepository employeeRepository) {
        return new PimProjectServiceImpl(projectRepository, employeeRepository);
    }

    @Bean(BEAN_PROJECT_GRPC_SERVICE)
    public ProjectGRPCService projectGRPCService(@Qualifier(BEAN_PROJECT_SERVICE) BasePimDataService projectServiceImpl,
                                                 @Qualifier(BEAN_PROJECT_TRANSFORM_SERVICE) BaseTransformService projectTransformService) {
        return new ProjectGRPCService(projectServiceImpl, projectTransformService);
    }

    @Bean(BEAN_GROUP_TRANSFORM_SERVICE)
    public BaseTransformService<PimGroup, Group> groupTransformService() {
        return new GroupTransformServiceImpl();
    }

    @Bean(BEAN_GROUP_SERVICE)
    public BasePimDataService groupServiceImpl(@Autowired GroupRepository groupRepository) {
        return new PimGroupServiceImpl(groupRepository);
    }

    @Bean(BEAN_GROUP_GRPC_SERVICE)
    public GroupGRPCService groupGRPCService(@Qualifier(BEAN_GROUP_SERVICE) BasePimDataService groupServiceImpl,
                                             @Qualifier(BEAN_GROUP_TRANSFORM_SERVICE) BaseTransformService groupTransformService) {
        return new GroupGRPCService(groupServiceImpl, groupTransformService);
    }
}
