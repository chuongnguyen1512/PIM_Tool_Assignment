package com.elca.vn.grpc;

import com.elca.vn.entity.Project;
import com.elca.vn.model.GUIEventMessage;
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
import com.elca.vn.socket.MulticastPublisher;
import com.elca.vn.transform.BaseTransformService;
import com.google.gson.Gson;
import io.grpc.stub.StreamObserver;
import org.apache.commons.lang3.StringUtils;
import org.lognet.springboot.grpc.GRpcService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

import static com.elca.vn.config.PIMToolBEConfiguration.BEAN_PROJECT_SERVICE;
import static com.elca.vn.config.PIMToolBEConfiguration.BEAN_PROJECT_TRANSFORM_SERVICE;
import static com.elca.vn.constant.EventConstant.RELOAD_TABLE_MESSAGE;

/**
 * GRPC Service class for handling RPC request for project data
 */
@GRpcService
public class ProjectGRPCService extends BaseProjectServiceGrpc.BaseProjectServiceImplBase implements GRPCBaseService {

    private static final Logger LOGGER = LoggerFactory.getLogger(ProjectGRPCService.class);
    private final Gson gson;
    private final MulticastPublisher multicastPublisher;
    private final int executorServicePoolSize;

    private BasePimDataService<Project> basePimDataService;
    private BaseTransformService<PimProject, Project> projectTransformService;

    @Autowired
    public ProjectGRPCService(@Qualifier(BEAN_PROJECT_SERVICE) BasePimDataService<Project> basePimDataService,
                              @Qualifier(BEAN_PROJECT_TRANSFORM_SERVICE) BaseTransformService<PimProject, Project> projectTransformService,
                              @Value("${executor.service.poolsize}") int executorServicePoolSize,
                              @Autowired MulticastPublisher multicastPublisher,
                              @Autowired Gson gson) {
        this.basePimDataService = basePimDataService;
        this.projectTransformService = projectTransformService;
        this.multicastPublisher = multicastPublisher;
        this.gson = gson;
        this.executorServicePoolSize = executorServicePoolSize;
    }

    /**
     * Verify and processing RPC request for saving project data to DB
     *
     * @param request          RPC request
     * @param responseObserver response observer
     */
    @Override
    public void savingProject(PimProjectPersistRequest request, StreamObserver<PimProjectPersistResponse> responseObserver) {
        if (Objects.isNull(request) || Objects.isNull(request.getProject())) {
            handlingInternalError(responseObserver, null);
            return;
        }

        PimProject pimProject = request.getProject();
        String transactionID = request.getTransactionID();

        receiveAndHandlingRPCRequest(transactionID, responseObserver, () -> {
            Project project = projectTransformService.transformFromSourceToDes(pimProject);
            Project saveProject;

            if (ProcessingStatus.INSERT.equals(request.getProject().getProcessingStatus())) {
                saveProject = basePimDataService.importData(project);
            } else {
                saveProject = basePimDataService.updateData(project);
            }

            PimProject responseProject = projectTransformService.transformFromDesToSource(saveProject);
            responseObserver.onNext(PimProjectPersistResponse.newBuilder()
                    .setTransactionID(transactionID)
                    .setProject(responseProject)
                    .setIsSuccess(true)
                    .build());
            responseObserver.onCompleted();

            // Multicast to client to reload table data
            Executors.newFixedThreadPool(executorServicePoolSize).execute(()-> {
                String eventMessage = gson.toJson(new GUIEventMessage().setMessageID(RELOAD_TABLE_MESSAGE));
                multicastPublisher.sendSocketMessage(eventMessage);
            });
        }, PimProjectPersistResponse.class);
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

            List<Project> projects;
            if (StringUtils.isBlank(searchContent) && StringUtils.isBlank(status)) {
                projects = (List<Project>) basePimDataService.findAllDataWithPaging(indexPage);
            } else {
                projects = (List<Project>) basePimDataService.findDataWithPaging(indexPage, searchContent, status);
            }

            List<PimProject> pimProjects = new ArrayList<>();

            if (!CollectionUtils.isEmpty(projects)) {
                pimProjects = projects.stream().map(x -> projectTransformService.transformFromDesToSource(x)).collect(Collectors.toList());
            }

             responseObserver.onNext(PimProjectQueryResponse.newBuilder()
                    .setTransactionID(transactionID)
                    .addAllProject(pimProjects)
                    .setIsSuccess(true)
                    .build());
            responseObserver.onCompleted();
        }, PimProjectQueryResponse.class);
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
        }, PimProjectCountingResponse.class);
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
        }, PimProjectDeleteResponse.class);
    }
}
