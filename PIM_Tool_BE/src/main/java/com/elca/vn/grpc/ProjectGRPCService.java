package com.elca.vn.grpc;

import com.elca.vn.entity.Project;
import com.elca.vn.proto.model.PimProject;
import com.elca.vn.proto.model.PimProjectCountingRequest;
import com.elca.vn.proto.model.PimProjectCountingResponse;
import com.elca.vn.proto.model.PimProjectDeleteRequest;
import com.elca.vn.proto.model.PimProjectDeleteResponse;
import com.elca.vn.proto.model.PimProjectPersistRequest;
import com.elca.vn.proto.model.PimProjectPersistResponse;
import com.elca.vn.proto.model.PimProjectQueryRequest;
import com.elca.vn.proto.model.PimProjectQueryResponse;
import com.elca.vn.proto.model.ProcessingStatus;
import com.elca.vn.proto.service.BaseProjectServiceGrpc;
import com.elca.vn.service.BasePimDataService;
import com.elca.vn.transform.BaseTransformService;
import io.grpc.stub.StreamObserver;
import org.apache.commons.lang3.StringUtils;
import org.lognet.springboot.grpc.GRpcService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static com.elca.vn.config.PIMToolBEConfiguration.BEAN_PROJECT_SERVICE;
import static com.elca.vn.config.PIMToolBEConfiguration.BEAN_PROJECT_TRANSFORM_SERVICE;

/**
 * GRPC Service class for handling RPC request for project data
 */
@GRpcService
public class ProjectGRPCService extends BaseProjectServiceGrpc.BaseProjectServiceImplBase implements GRPCBaseService {

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
     * Verify and processing RPC request for saving project data to DB
     *
     * @param request          RPC request
     * @param responseObserver response observer
     */
    @Override
    public void savingNewProject(PimProjectPersistRequest request, StreamObserver<PimProjectPersistResponse> responseObserver) {
        if (Objects.isNull(request)
                || Objects.isNull(request.getProject())
                || request.getProject().getProcessingStatus() != ProcessingStatus.INSERT) {
            LOGGER.error("Request is not valid");
            handlingInternalError(responseObserver, null);
            return;
        }

        PimProject pimProject = request.getProject();
        String transactionID = request.getTransactionID();

        receiveAndHandlingRPCRequest(transactionID, responseObserver, () -> {
            Project project = (Project) projectTransformService.transformFromSourceToDes(pimProject);
            Project newInsertProject = (Project) basePimDataService.importData(project);
            PimProject responseProject = (PimProject) projectTransformService.transformFromDesToSource(newInsertProject);

            responseObserver.onNext(PimProjectPersistResponse.newBuilder()
                    .setTransactionID(transactionID)
                    .setProject(responseProject)
                    .setIsSuccess(true)
                    .build());
            responseObserver.onCompleted();
        });
    }

    /**
     * Getting projects data with paging mechanism.
     *
     * @param request          rpc request
     * @param responseObserver response observer
     */
    @Override
    public void getProjects(PimProjectQueryRequest request, StreamObserver<PimProjectQueryResponse> responseObserver) {
        if (Objects.isNull(request) || request.getPage() < 0) {
            LOGGER.error("Request is not valid");
            handlingInternalError(responseObserver, null);
            return;
        }

        int indexPage = request.getPage();
        String searchContent = request.getSearchContent();
        String transactionID = request.getTransactionID();
        String status = request.getStatus();

        receiveAndHandlingRPCRequest(transactionID, responseObserver, () -> {

            List<Project> projects = new ArrayList();
            if (StringUtils.isBlank(searchContent) && StringUtils.isBlank(status)) {
                projects = basePimDataService.findAllDataWithPaging(indexPage);
            } else {
                projects = basePimDataService.findDataWithPaging(indexPage, searchContent, status);
            }

            List<PimProject> pimProjects = projects.stream()
                    .map(x -> (PimProject) projectTransformService.transformFromDesToSource(x))
                    .collect(Collectors.toList());

            responseObserver.onNext(PimProjectQueryResponse.newBuilder()
                    .setTransactionID(transactionID)
                    .addAllProject(pimProjects)
                    .setIsSuccess(true)
                    .build());
            responseObserver.onCompleted();
        });
    }

    /**
     * Getting number of total records either with searching content or without searching content
     *
     * @param request          rpc request
     * @param responseObserver response observer
     */
    @Override
    public void getTotalNumOfProjects(PimProjectCountingRequest request, StreamObserver<PimProjectCountingResponse> responseObserver) {
        if (Objects.isNull(request)) {
            LOGGER.error("Request is not valid");
            handlingInternalError(responseObserver, null);
            return;
        }

        String transactionID = request.getTransactionID();
        String searchContent = request.getSearchContent();
        String status = request.getStatus();

        receiveAndHandlingRPCRequest(transactionID, responseObserver, () -> {
            long totalRecordsNum;

            if (StringUtils.isBlank(searchContent) && StringUtils.isBlank(status)) {
                totalRecordsNum = basePimDataService.getTotalDataSize();
            } else {
                totalRecordsNum = basePimDataService.getTotalDataSize(searchContent, status);
            }

            responseObserver.onNext(PimProjectCountingResponse.newBuilder()
                    .setTransactionID(transactionID)
                    .setTotalRecordsNum(totalRecordsNum)
                    .setIsSuccess(true)
                    .build());
            responseObserver.onCompleted();
        });
    }

    @Override
    public void deleteProjects(PimProjectDeleteRequest request, StreamObserver<PimProjectDeleteResponse> responseObserver) {
        if (Objects.isNull(request) || request.getProjectNumbersCount() == 0) {
            LOGGER.error("Request is not valid");
            handlingInternalError(responseObserver, null);
            return;
        }
        String transactionID = request.getTransactionID();
        List<Integer> deleteProjectNums = request.getProjectNumbersList();

        receiveAndHandlingRPCRequest(transactionID, responseObserver, () -> {
            int deletedProjects = basePimDataService.deleteData(deleteProjectNums);
            responseObserver.onNext(PimProjectDeleteResponse.newBuilder()
                    .setTransactionID(transactionID)
                    .setTotalDeletedRecordsNum(deletedProjects)
                    .setIsSuccess(true)
                    .build());
            responseObserver.onCompleted();
        });
    }
}
