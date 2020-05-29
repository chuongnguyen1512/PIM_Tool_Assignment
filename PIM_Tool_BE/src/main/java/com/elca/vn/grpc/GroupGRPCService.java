package com.elca.vn.grpc;

import com.elca.vn.entity.Group;
import com.elca.vn.proto.model.PimGroup;
import com.elca.vn.proto.model.PimGroupRequest;
import com.elca.vn.proto.model.PimGroupResponse;
import com.elca.vn.proto.service.BaseGroupServiceGrpc;
import com.elca.vn.service.BasePimDataService;
import com.elca.vn.transform.BaseTransformService;
import com.elca.vn.util.ExceptionUtils;
import io.grpc.stub.StreamObserver;
import org.lognet.springboot.grpc.GRpcService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import java.util.Iterator;
import java.util.Objects;

import static com.elca.vn.config.PIMToolBEConfiguration.BEAN_GROUP_SERVICE;
import static com.elca.vn.config.PIMToolBEConfiguration.BEAN_GROUP_TRANSFORM_SERVICE;
import static com.elca.vn.constant.BundleConstant.INTERNAL_ERROR_MSG_BUNDLE_ID;

/**
 * GRPC Service for handling RPC request for Group data
 */
@GRpcService
public class GroupGRPCService extends BaseGroupServiceGrpc.BaseGroupServiceImplBase implements GRPCBaseService {
    private static final Logger LOGGER = LoggerFactory.getLogger(ProjectGRPCService.class);

    private BasePimDataService basePimDataService;
    private BaseTransformService baseTransformService;

    @Autowired
    public GroupGRPCService(@Qualifier(BEAN_GROUP_SERVICE) BasePimDataService basePimDataService,
                            @Qualifier(BEAN_GROUP_TRANSFORM_SERVICE) BaseTransformService baseTransformService) {
        this.basePimDataService = basePimDataService;
        this.baseTransformService = baseTransformService;
    }

    /**
     * Streaming group data
     *
     * @param request rpc request
     * @param responseObserver response observer
     */
    @Override
    public void streamGroupData(PimGroupRequest request, StreamObserver<PimGroupResponse> responseObserver) {
        if (Objects.isNull(request)) {
            LOGGER.error("Request is not valid");
            responseObserver.onError(ExceptionUtils.buildInternalErrorStatusException(INTERNAL_ERROR_MSG_BUNDLE_ID));
        }
        String transactionID = request.getTransactionID();

        receiveAndHandlingRPCRequest(transactionID, responseObserver, () -> {
            Iterator<Group> data = basePimDataService.getData();

            // if stream is null, return response immediately
            if (Objects.isNull(data)) {
                responseObserver.onNext(PimGroupResponse.newBuilder()
                        .setTransactionID(transactionID)
                        .setIsSuccess(true)
                        .build());
                responseObserver.onCompleted();
            }
            iteratorGroupData(data, responseObserver, transactionID);
        });

    }

    private void iteratorGroupData(Iterator<Group> data, StreamObserver responseObserver, String transactionID) {
        try {
            while (data.hasNext()) {
                PimGroup pimGroup = (PimGroup) baseTransformService.transformFromDesToSource(data.next());
                if (Objects.isNull(pimGroup)) {
                    return;
                }
                responseObserver.onNext(PimGroupResponse.newBuilder()
                        .setTransactionID(transactionID)
                        .setGroup(pimGroup)
                        .setIsSuccess(true)
                        .build());
            }
        } finally {
            responseObserver.onCompleted();
        }
    }
}
