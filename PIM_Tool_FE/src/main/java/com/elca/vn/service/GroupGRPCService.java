package com.elca.vn.service;

import com.elca.vn.proto.model.PimGroupRequest;
import com.elca.vn.proto.service.BaseGroupServiceGrpc;
import io.grpc.ManagedChannel;
import io.grpc.stub.AbstractStub;

import java.util.Iterator;
import java.util.Objects;

public class GroupGRPCService extends BaseGRPCService {

    public GroupGRPCService(String serverName,
                            Integer port) {
        super(serverName, port);
    }

    @Override
    protected <I, O> O callingRemoteMethod(I request, AbstractStub stub) {
        BaseGroupServiceGrpc.BaseGroupServiceBlockingStub blockingStub = (BaseGroupServiceGrpc.BaseGroupServiceBlockingStub) stub;
        if (Objects.isNull(request)) {
            return null;
        }

        if (request instanceof PimGroupRequest) {
            return (O) blockingStub.getGroupData((PimGroupRequest) request);
        }
        return null;
    }

    @Override
    protected <I, O> Iterator<O> callingRemoteMethodWithServerStreaming(I request, AbstractStub stub) {
        return null;
    }

    @Override
    protected AbstractStub initStub(ManagedChannel channel) {
        return BaseGroupServiceGrpc.newBlockingStub(channel);
    }
}
