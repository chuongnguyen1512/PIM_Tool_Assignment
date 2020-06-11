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
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static com.elca.vn.config.PIMToolBEConfiguration.BEAN_GROUP_SERVICE;
import static com.elca.vn.config.PIMToolBEConfiguration.BEAN_GROUP_TRANSFORM_SERVICE;
import static com.elca.vn.constant.BundleConstant.INTERNAL_ERROR_MSG_BUNDLE_ID;

/**
 * GRPC Service for handling RPC request for Group data
 */
@GRpcService
public class GroupGRPCService extends BaseGroupServiceGrpc.BaseGroupServiceImplBase implements GRPCBaseService {

    private static final Logger LOGGER = LoggerFactory.getLogger(GroupGRPCService.class);

    private BasePimDataService<Group> basePimDataService;
    private BaseTransformService<PimGroup, Group> baseTransformService;

    @Autowired
    public GroupGRPCService(@Qualifier(BEAN_GROUP_SERVICE) BasePimDataService<Group> basePimDataService,
                            @Qualifier(BEAN_GROUP_TRANSFORM_SERVICE) BaseTransformService<PimGroup, Group> baseTransformService) {
        this.basePimDataService = basePimDataService;
        this.baseTransformService = baseTransformService;
    }

    /**
     * Streaming group data
     *
     * @param request          rpc request
     * @param responseObserver response observer
     */
    @Override
    public void getGroupData(PimGroupRequest request, StreamObserver<PimGroupResponse> responseObserver) {
        if (Objects.isNull(request)) {
            LOGGER.error("Request is not valid");
            responseObserver.onError(ExceptionUtils.buildInternalErrorStatusException(INTERNAL_ERROR_MSG_BUNDLE_ID));
            return;
        }

        String transactionID = request.getTransactionID();
        receiveAndHandlingRPCRequest(transactionID, responseObserver, () -> {
            List<Group> data = (List<Group>) basePimDataService.getData();
            if (CollectionUtils.isEmpty(data)) {
                responseObserver.onNext(PimGroupResponse.newBuilder()
                        .setTransactionID(transactionID)
                        .setIsSuccess(true)
                        .build());
            } else {
                List<PimGroup> pimGroups = data.stream().map(x -> baseTransformService.transformFromDesToSource(x)).collect(Collectors.toList());
                responseObserver.onNext(PimGroupResponse.newBuilder()
                        .setTransactionID(transactionID)
                        .addAllGroups(pimGroups)
                        .setIsSuccess(true)
                        .build());
            }
            responseObserver.onCompleted();
        }, PimGroupResponse.class);
    }
}
