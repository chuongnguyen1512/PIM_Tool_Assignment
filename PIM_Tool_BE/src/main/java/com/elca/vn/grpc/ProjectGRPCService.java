package com.elca.vn.grpc;

import com.elca.vn.constant.BundleConstant;
import com.elca.vn.entity.Project;
import com.elca.vn.exception.ProjectImportException;
import com.elca.vn.proto.model.PimProject;
import com.elca.vn.proto.model.PimProjectPersistRequest;
import com.elca.vn.proto.model.PimProjectPersistResponse;
import com.elca.vn.proto.model.ProcessingStatus;
import com.elca.vn.proto.service.BaseProjectServiceGrpc;
import com.elca.vn.service.BasePimDataService;
import com.elca.vn.transform.BaseTransformService;
import com.elca.vn.util.ExceptionUtils;
import io.grpc.StatusRuntimeException;
import io.grpc.stub.StreamObserver;
import org.lognet.springboot.grpc.GRpcService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import java.util.Objects;

import static com.elca.vn.config.PIMToolBEConfiguration.BEAN_PROJECT_SERVICE;
import static com.elca.vn.config.PIMToolBEConfiguration.BEAN_PROJECT_TRANSFORM_SERVICE;

/**
 *
 */
@GRpcService
public class ProjectGRPCService extends BaseProjectServiceGrpc.BaseProjectServiceImplBase {

    private static final Logger LOGGER = LoggerFactory.getLogger(ProjectGRPCService.class);

    private BasePimDataService basePimDataService;
    private BaseTransformService projectTransformService;

    @Autowired
    public ProjectGRPCService(@Qualifier(BEAN_PROJECT_SERVICE) BasePimDataService basePimDataService,
                              @Qualifier(BEAN_PROJECT_TRANSFORM_SERVICE) BaseTransformService projectTransformService) {
        this.basePimDataService = basePimDataService;
        this.projectTransformService = projectTransformService;
    }

    /**
     * @param request
     * @param responseObserver
     */
    @Override
    public void insertNewProject(PimProjectPersistRequest request, StreamObserver<PimProjectPersistResponse> responseObserver) {
        if (Objects.isNull(request)
                || Objects.isNull(request.getProject())
                || request.getProject().getProcessingStatus() != ProcessingStatus.INSERT) {
            LOGGER.error("Request is not valid");
            StatusRuntimeException statusException = ExceptionUtils.buildInternalErrorStatusException(BundleConstant.INTERNAL_ERROR_MSG_BUNDLE_ID);
            responseObserver.onError(statusException);
        }

        PimProject pimProject = request.getProject();
        String transactionID = request.getTransactionID();
        try {
            Project project = (Project) projectTransformService.transformFromSourceToDes(pimProject);
            Project newInsertProject = (Project) basePimDataService.importData(project);
            PimProject responseProject = (PimProject) projectTransformService.transformFromDesToSource(newInsertProject);

            responseObserver.onNext(PimProjectPersistResponse.newBuilder()
                    .setTransactionID(transactionID)
                    .setProject(responseProject)
                    .setIsSuccess(true).build());
            responseObserver.onCompleted();

        } catch (ProjectImportException pe) {
            responseObserver.onNext(PimProjectPersistResponse.newBuilder()
                    .setTransactionID(transactionID)
                    .setBundleID(pe.getMessage())
                    .setIsSuccess(false).build());
            responseObserver.onCompleted();

        } catch (Exception e) {
            LOGGER.error("There's an exception while persisting project data", e);
            StatusRuntimeException statusException = ExceptionUtils.buildInternalErrorStatusException(BundleConstant.INTERNAL_ERROR_MSG_BUNDLE_ID);
            responseObserver.onError(statusException);
        }
    }
}
