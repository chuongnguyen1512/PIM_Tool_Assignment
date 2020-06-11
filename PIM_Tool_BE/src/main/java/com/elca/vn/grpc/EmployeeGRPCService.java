package com.elca.vn.grpc;

import com.elca.vn.entity.Employee;
import com.elca.vn.proto.model.PimEmployee;
import com.elca.vn.proto.model.PimEmployeeQueryRequest;
import com.elca.vn.proto.model.PimEmployeeQueryResponse;
import com.elca.vn.proto.service.BaseEmployeeServiceGrpc;
import com.elca.vn.service.BasePimDataService;
import com.elca.vn.transform.BaseTransformService;
import com.elca.vn.util.ExceptionUtils;
import io.grpc.stub.StreamObserver;
import org.apache.commons.lang3.StringUtils;
import org.lognet.springboot.grpc.GRpcService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.util.CollectionUtils;

import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import static com.elca.vn.config.PIMToolBEConfiguration.BEAN_EMPLOYEE_SERVICE;
import static com.elca.vn.config.PIMToolBEConfiguration.BEAN_EMPLOYEE_TRANSFORM_SERVICE;
import static com.elca.vn.constant.BundleConstant.INTERNAL_ERROR_MSG_BUNDLE_ID;

/**
 * GRPC Service for handling RPC request for Employee data
 */
@GRpcService
public class EmployeeGRPCService extends BaseEmployeeServiceGrpc.BaseEmployeeServiceImplBase implements GRPCBaseService {

    private static final Logger LOGGER = LoggerFactory.getLogger(EmployeeGRPCService.class);

    private BasePimDataService<Employee> basePimDataService;
    private BaseTransformService<PimEmployee, Employee> baseTransformService;

    @Autowired
    public EmployeeGRPCService(@Qualifier(BEAN_EMPLOYEE_SERVICE) BasePimDataService<Employee> basePimDataService,
                               @Qualifier(BEAN_EMPLOYEE_TRANSFORM_SERVICE) BaseTransformService<PimEmployee, Employee> baseTransformService) {
        this.basePimDataService = basePimDataService;
        this.baseTransformService = baseTransformService;
    }

    /**
     * Get employee data
     *
     * @param request          rpc request
     * @param responseObserver response observer
     */
    @Override
    public void getEmployeeData(PimEmployeeQueryRequest request, StreamObserver<PimEmployeeQueryResponse> responseObserver) {
        if (Objects.isNull(request)) {
            LOGGER.error("Request is not valid");
            responseObserver.onError(ExceptionUtils.buildInternalErrorStatusException(INTERNAL_ERROR_MSG_BUNDLE_ID));
            return;
        }

        String transactionID = request.getTransactionID();
        Set<Employee> data = new HashSet<>();

        if (!CollectionUtils.isEmpty(request.getVisasList())) {
            data.addAll((Set<Employee>) basePimDataService.getData(request.getVisasList()));
        }

        if (StringUtils.isNotBlank(request.getContentSearch())) {
            data.addAll((Set<Employee>) basePimDataService.findDataWithPaging(1, request.getContentSearch()));
        }

        receiveAndHandlingRPCRequest(transactionID, responseObserver, () -> {
            if (CollectionUtils.isEmpty(data)) {
                responseObserver.onNext(PimEmployeeQueryResponse.newBuilder()
                        .setTransactionID(transactionID)
                        .setIsSuccess(true)
                        .build());
            } else {
                List<PimEmployee> pimEmployees = data.stream().map(x -> baseTransformService.transformFromDesToSource(x)).collect(Collectors.toList());
                responseObserver.onNext(PimEmployeeQueryResponse.newBuilder()
                        .setTransactionID(transactionID)
                        .addAllEmployees(pimEmployees)
                        .setIsSuccess(true)
                        .build());
            }
            responseObserver.onCompleted();
        }, PimEmployeeQueryResponse.class);
    }
}
