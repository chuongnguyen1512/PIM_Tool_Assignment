package com.elca.vn.grpc;

import com.elca.vn.constant.BundleConstant;
import com.elca.vn.exception.PIMToolException;
import com.elca.vn.proto.model.PimProjectPersistResponse;
import com.elca.vn.util.ExceptionUtils;
import io.grpc.StatusRuntimeException;
import io.grpc.stub.StreamObserver;
import org.slf4j.LoggerFactory;

/**
 * Base GRPC Service which help us to build skeleton for RPC request handling with support exception handling also
 */
public interface GRPCBaseService {

    /**
     * Receive and handling RPC request
     *
     * @param transactionID uuid
     * @param responseObserver response observer
     * @param handlingRequest interface for request handling
     */
    default void receiveAndHandlingRPCRequest(String transactionID, StreamObserver responseObserver, HandlingRequest handlingRequest) {
        try {
            handlingRequest.execute();
        } catch (PIMToolException pe) {
            responseObserver.onNext(PimProjectPersistResponse.newBuilder()
                    .setTransactionID(transactionID)
                    .setBundleID(pe.getMessage())
                    .setIsSuccess(false)
                    .build());
            responseObserver.onCompleted();
        } catch (Exception e) {
            handlingInternalError(responseObserver, e);
        }
    }

    /**
     * Exception handling
     *
     * @param responseObserver response observer
     */
    default void handlingInternalError(StreamObserver responseObserver, Exception e) {
        LoggerFactory.getLogger(GRPCBaseService.class).error("There is an exception when handling RPC request", e.getCause());
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
