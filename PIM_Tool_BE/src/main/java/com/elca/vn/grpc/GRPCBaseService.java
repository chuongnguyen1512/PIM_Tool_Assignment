package com.elca.vn.grpc;

import com.elca.vn.constant.BundleConstant;
import com.elca.vn.exception.PIMToolException;
import com.elca.vn.proto.model.*;
import com.elca.vn.util.ExceptionUtils;
import io.grpc.StatusRuntimeException;
import io.grpc.stub.StreamObserver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Objects;

/**
 * Base GRPC Service which help us to build skeleton for RPC request handling with support exception handling also
 */
public interface GRPCBaseService {

    Logger LOGGER = LoggerFactory.getLogger(GRPCBaseService.class);

    /**
     * Receive and handling RPC request
     *
     * @param transactionID    uuid
     * @param responseObserver response observer
     * @param handlingRequest  interface for request handling
     */
    default void receiveAndHandlingRPCRequest(String transactionID, StreamObserver responseObserver, HandlingRequest handlingRequest, Class responseType) {
        try {
            handlingRequest.execute();
        } catch (PIMToolException pe) {
            if (Objects.isNull(responseType)) {
                handlingInternalError(responseObserver, null);
            }
            buildProjectResponseObserver(responseObserver, responseType, pe, transactionID);
            buildGroupResponseObserver(responseObserver, responseType, pe, transactionID);
            buildEmployeeResponseObserver(responseObserver, responseType, pe, transactionID);
            responseObserver.onCompleted();
        } catch (Exception e) {
            LOGGER.error("There is an exception when handling RPC request", e);
            handlingInternalError(responseObserver, e);
        }
    }

    default void buildGroupResponseObserver(StreamObserver responseObserver, Class responseType, PIMToolException pe, String transactionID) {
        if (responseType.equals(PimGroupResponse.class)) {
            responseObserver.onNext(PimGroupResponse.newBuilder().setTransactionID(transactionID)
                    .setBundleID(pe.getMessage())
                    .setIsSuccess(false)
                    .build());
        }
    }

    default void buildProjectResponseObserver(StreamObserver responseObserver, Class responseType, PIMToolException pe, String transactionID) {
        if (responseType.equals(PimProjectPersistResponse.class)) {
            responseObserver.onNext(PimProjectPersistResponse.newBuilder().setTransactionID(transactionID)
                    .setBundleID(pe.getMessage())
                    .setIsSuccess(false)
                    .build());
        }
        if (responseType.equals(PimProjectQueryResponse.class)) {
            responseObserver.onNext(PimProjectQueryResponse.newBuilder().setTransactionID(transactionID)
                    .setBundleID(pe.getMessage())
                    .setIsSuccess(false)
                    .build());
        }
        if (responseType.equals(PimProjectDeleteResponse.class)) {
            responseObserver.onNext(PimProjectDeleteResponse.newBuilder().setTransactionID(transactionID)
                    .setBundleID(pe.getMessage())
                    .setIsSuccess(false)
                    .build());
        }
        if (responseType.equals(PimProjectCountingResponse.class)) {
            responseObserver.onNext(PimProjectCountingResponse.newBuilder().setTransactionID(transactionID)
                    .setBundleID(pe.getMessage())
                    .setIsSuccess(false)
                    .build());
        }
    }

    default void buildEmployeeResponseObserver(StreamObserver responseObserver, Class responseType, PIMToolException pe, String transactionID) {
        if (responseType.equals(PimEmployeeQueryResponse.class)) {
            responseObserver.onNext(PimEmployeeQueryResponse.newBuilder().setTransactionID(transactionID)
                    .setBundleID(pe.getMessage())
                    .setIsSuccess(false)
                    .build());
        }
    }

    /**
     * Exception handling
     *
     * @param responseObserver response observer
     */
    default void handlingInternalError(StreamObserver responseObserver, Exception e) {
        LOGGER.error("Request is not valid", e);
        StatusRuntimeException statusException = ExceptionUtils.buildInternalErrorStatusException(BundleConstant.INTERNAL_ERROR_MSG_BUNDLE_ID);
        responseObserver.onError(statusException);
    }

    /**
     * Interface for handling request
     */
    interface HandlingRequest {
        void execute();
    }
}
