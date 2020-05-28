package com.elca.vn.service;

import com.elca.vn.proto.model.PimGroupRequest;
import com.elca.vn.proto.model.PimGroupResponse;
import com.elca.vn.proto.service.BaseGroupServiceGrpc;
import io.grpc.ManagedChannel;
import io.grpc.stub.AbstractStub;

import java.util.Iterator;
import java.util.Objects;

public class GroupGRPCService extends BaseGRPCService<PimGroupRequest, PimGroupResponse> {

    private static GroupGRPCService groupGRPCService;

    private GroupGRPCService(String serverName, int port) {
        super(serverName, port);
    }

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

    @Override
    protected PimGroupResponse callRemoteMethod(PimGroupRequest request, AbstractStub stub) {
        return null;
    }

    @Override
    protected Iterator<PimGroupResponse> callRemoteMethodWithStreamResponse(PimGroupRequest object, AbstractStub stub) {
        BaseGroupServiceGrpc.BaseGroupServiceBlockingStub blockingStub = (BaseGroupServiceGrpc.BaseGroupServiceBlockingStub) stub;
        return blockingStub.streamGroupData(object);
    }
}
