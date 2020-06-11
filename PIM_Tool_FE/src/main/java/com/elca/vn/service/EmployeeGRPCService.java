package com.elca.vn.service;

import com.elca.vn.proto.model.PimEmployeeQueryRequest;
import com.elca.vn.proto.service.BaseEmployeeServiceGrpc;
import io.grpc.ManagedChannel;
import io.grpc.stub.AbstractStub;

import java.util.Iterator;
import java.util.Objects;

public class EmployeeGRPCService extends BaseGRPCService {

    public EmployeeGRPCService(String serverName,
                               Integer port) {
        super(serverName, port);
    }

    @Override
    protected <I, O> O callingRemoteMethod(I request, AbstractStub stub) {
        BaseEmployeeServiceGrpc.BaseEmployeeServiceBlockingStub blockingStub = (BaseEmployeeServiceGrpc.BaseEmployeeServiceBlockingStub) stub;
        if (Objects.isNull(request)) {
            return null;
        }

        if (request instanceof PimEmployeeQueryRequest) {
            return (O) blockingStub.getEmployeeData((PimEmployeeQueryRequest) request);
        }
        return null;
    }

    @Override
    protected <I, O> Iterator<O> callingRemoteMethodWithServerStreaming(I request, AbstractStub stub) {
        return null;
    }

    @Override
    protected AbstractStub initStub(ManagedChannel channel) {
        return BaseEmployeeServiceGrpc.newBlockingStub(channel);
    }
}
