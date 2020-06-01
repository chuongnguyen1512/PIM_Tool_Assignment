package com.elca.vn.service;

import com.elca.vn.proto.model.PimGroupRequest;
import com.elca.vn.proto.service.BaseGroupServiceGrpc;
import io.grpc.ManagedChannel;
import io.grpc.stub.AbstractStub;
import org.springframework.stereotype.Service;

import java.util.Iterator;
import java.util.Objects;

@Service
public class GroupGRPCService extends BaseGRPCService {

    public GroupGRPCService(String serverName, int port) {
        super(serverName, port);
    }

    @Override
    protected <I, O> O callingRemoteMethod(I request, AbstractStub stub) {
        return null;
    }

    @Override
    protected <I, O> Iterator<O> callingRemoteMethodWithServerStreaming(I request, AbstractStub stub) {
        BaseGroupServiceGrpc.BaseGroupServiceBlockingStub blockingStub = (BaseGroupServiceGrpc.BaseGroupServiceBlockingStub) stub;
        if (Objects.isNull(request)) {
            return null;
        }

        if (request instanceof PimGroupRequest) {
            return (Iterator<O>) blockingStub.streamGroupData((PimGroupRequest) request);
        }
        return null;
    }

    @Override
    protected AbstractStub initStub(ManagedChannel channel) {
        return BaseGroupServiceGrpc.newBlockingStub(channel);
    }
}
