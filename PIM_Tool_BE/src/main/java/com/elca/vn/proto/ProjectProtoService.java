package com.elca.vn.proto;

import com.elca.vn.entity.Project;
import com.elca.vn.exception.ProjectExecuteException;
import com.elca.vn.proto.model.PimProject;
import com.elca.vn.proto.model.ProcessingStatus;
import com.elca.vn.service.ProjectService;
import com.elca.vn.transform.BaseTransformService;
import io.grpc.stub.StreamObserver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProjectProtoService extends BaseProjectServiceGrpc.BaseProjectServiceImplBase {

    private static final Logger LOGGER = LoggerFactory.getLogger(ProjectProtoService.class);

    private ProjectService projectService;
    private BaseTransformService projectTransformService;

    @Autowired
    public ProjectProtoService(ProjectService projectService, BaseTransformService projectTransformService) {
        this.projectService = projectService;
        this.projectTransformService = projectTransformService;
    }

    @Override
    public void insertNewProject(PimProject request, StreamObserver<PimProject> responseObserver) {
        if (request.getProcessingStatus() != ProcessingStatus.INSERT) {
            LOGGER.warn("Request is not valid");
            responseObserver.onError(new IllegalArgumentException("Request is not valid"));
        }

        try {
            Project project = (Project) projectTransformService.transformFromDesToSource(request);
            projectService.persitProject(project);
        } catch (Exception e) {
            LOGGER.error("There's an exception while persisting project data", e);
            responseObserver.onError(new ProjectExecuteException("Cannot persist project data"));
        }
        responseObserver.onCompleted();
    }

    @Override
    public void updateProject(PimProject request, StreamObserver<PimProject> responseObserver) {
        super.updateProject(request, responseObserver);
    }
}
