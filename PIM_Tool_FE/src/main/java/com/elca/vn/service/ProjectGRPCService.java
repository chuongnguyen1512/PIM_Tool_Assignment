package com.elca.vn.service;

import com.elca.vn.proto.model.PimProjectPersistRequest;
import com.elca.vn.proto.model.PimProjectPersistResponse;
import com.elca.vn.proto.service.BaseProjectServiceGrpc;
import io.grpc.ManagedChannel;
import io.grpc.stub.AbstractStub;

import java.util.Iterator;
import java.util.Objects;

/**
 * RCP Client service communicate using protobuf
 */
public class ProjectGRPCService extends BaseGRPCService<PimProjectPersistRequest, PimProjectPersistResponse> {

    private static ProjectGRPCService projectGRPCService;

    private ProjectGRPCService(String serverName, int port) {
        super(serverName, port);
    }

    public static ProjectGRPCService getInstance(String serverName, int port) {
        if (Objects.isNull(projectGRPCService)) {
            return new ProjectGRPCService(serverName, port);
        }
        return projectGRPCService;
    }

    @Override
    protected AbstractStub initStub(ManagedChannel channel) {
        return BaseProjectServiceGrpc.newBlockingStub(channel);
    }

    @Override
    protected PimProjectPersistResponse callRemoteMethod(PimProjectPersistRequest object, AbstractStub stub) {
        BaseProjectServiceGrpc.BaseProjectServiceBlockingStub blockingStub = (BaseProjectServiceGrpc.BaseProjectServiceBlockingStub) stub;
        return blockingStub.insertNewProject(object);
    }

    @Override
    protected Iterator<PimProjectPersistResponse> callRemoteMethodWithStreamResponse(PimProjectPersistRequest object, AbstractStub stub) {
        return null;
    }
}
