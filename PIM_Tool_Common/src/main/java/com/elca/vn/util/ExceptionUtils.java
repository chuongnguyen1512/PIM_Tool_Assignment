package com.elca.vn.util;

import com.google.rpc.Code;
import com.google.rpc.Status;
import io.grpc.StatusRuntimeException;
import io.grpc.protobuf.StatusProto;

/**
 * Util class for handling exception
 */
public class ExceptionUtils {

    private ExceptionUtils() {
    }

    public static StatusRuntimeException buildCustomizeStatusException(int code, String bundleID) {
        Status status = Status.newBuilder()
                .setCode(code)
                .setMessage(bundleID)
                .build();
        return StatusProto.toStatusRuntimeException(status);
    }

    public static StatusRuntimeException buildInternalErrorStatusException(String bundleID) {
        Status status = Status.newBuilder()
                .setCode(Code.INTERNAL.getNumber())
                .setMessage(bundleID)
                .build();
        return StatusProto.toStatusRuntimeException(status);
    }
}
