package com.elca.vn.service;

import com.elca.vn.proto.model.PimProjectCountingRequest;
import com.elca.vn.proto.model.PimProjectDeleteRequest;
import com.elca.vn.proto.model.PimProjectPersistRequest;
import com.elca.vn.proto.model.PimProjectQueryRequest;
import com.elca.vn.proto.service.BaseProjectServiceGrpc;
import io.grpc.ManagedChannel;
import io.grpc.stub.AbstractStub;
import org.springframework.stereotype.Service;

import java.util.Iterator;
import java.util.Objects;

/**
 * RCP Client service communicate using protobuf
 */
@Service
public class ProjectGRPCService extends BaseGRPCService {

    public ProjectGRPCService(String serverName, int port) {
        super(serverName, port);
    }

    /**
     * Initialize stub to communicate with server side
     *
     * @param channel channel
     * @return stub to communicate with server side
     */
    @Override
    protected AbstractStub initStub(ManagedChannel channel) {
        return BaseProjectServiceGrpc.newBlockingStub(channel);
    }

    @Override
    protected <I, O> O callingRemoteMethod(I request, AbstractStub stub) {
        BaseProjectServiceGrpc.BaseProjectServiceBlockingStub blockingStub = (BaseProjectServiceGrpc.BaseProjectServiceBlockingStub) stub;
        if (Objects.isNull(request)) {
            return null;
        }

        if (request instanceof PimProjectPersistRequest) {
            return (O) blockingStub.savingNewProject((PimProjectPersistRequest) request);
        }

        if (request instanceof PimProjectCountingRequest) {
            return (O) blockingStub.getTotalNumOfProjects((PimProjectCountingRequest) request);
        }

        if (request instanceof PimProjectQueryRequest) {
            return (O) blockingStub.getProjects((PimProjectQueryRequest) request);
        }

        if (request instanceof PimProjectDeleteRequest) {
            return (O) blockingStub.deleteProjects((PimProjectDeleteRequest) request);
        }
        return null;
    }

    @Override
    protected <I, O> Iterator<O> callingRemoteMethodWithServerStreaming(I request, AbstractStub stub) {
        return null;
    }
}
