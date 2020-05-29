package com.elca.vn.service;

import com.elca.vn.proto.model.PimGroupRequest;
import com.elca.vn.proto.service.BaseGroupServiceGrpc;
import io.grpc.ManagedChannel;
import io.grpc.stub.AbstractStub;

import java.util.Iterator;
import java.util.Objects;

public class GroupGRPCService extends BaseGRPCService {

    private static GroupGRPCService groupGRPCService;

    private GroupGRPCService(String serverName, int port) {
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

    /**
     * Getting singleton instance for group grpc client service
     *
     * @param serverName server name
     * @param port       port
     * @return singleton instance for group grpc client service
     */
    public static GroupGRPCService getInstance(String serverName, int port) {
        if (Objects.isNull(groupGRPCService)) {
            return new GroupGRPCService(serverName, port);
        }
        return groupGRPCService;
    }

    @Override
    protected AbstractStub initStub(ManagedChannel channel) {
        return BaseGroupServiceGrpc.newBlockingStub(channel);
    }
}
