package com.elca.vn.service;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.StatusRuntimeException;
import io.grpc.stub.AbstractStub;

import java.util.Iterator;
import java.util.Objects;

/**
 * Default GRPC client service building skeleton for sending request
 */
public abstract class BaseGRPCService {

    protected String serverName;
    protected int port;
    protected ManagedChannel channel;

    protected BaseGRPCService(String serverName, int port) {
        this.serverName = serverName;
        this.port = port;
    }

    /**
     * Sending RPC Request
     *
     * @param request RPC Request
     * @return RPC Response
     * @throws StatusRuntimeException
     */
    public <I, O> O sendingRPCRequest(I request) throws StatusRuntimeException {
        try {
            channel = ManagedChannelBuilder.forAddress(serverName, port).usePlaintext().build();
            AbstractStub stub = initStub(channel);
            return callingRemoteMethod(request, stub);
        } finally {
            if (Objects.nonNull(channel) && !channel.isShutdown()) {
                channel.shutdown();
            }
        }
    }

    /**
     * Sending RPC request and server will stream response
     *
     * @param request RPC request
     * @return iterator of response
     * @throws StatusRuntimeException
     */
    public <I, O> Iterator<O> sendingRPCRequestWithStreamingResponse(I request) throws StatusRuntimeException {
        channel = ManagedChannelBuilder.forAddress(serverName, port).usePlaintext().build();
        AbstractStub stub = initStub(channel);
        // Method caller must handle shutdown channel after streaming all of data
        return callingRemoteMethodWithServerStreaming(request, stub);
    }

    protected abstract <I, O> O callingRemoteMethod(I request, AbstractStub stub);

    protected abstract <I, O> Iterator<O> callingRemoteMethodWithServerStreaming(I request, AbstractStub stub);

    protected abstract AbstractStub initStub(ManagedChannel channel);

    public String getServerName() {
        return serverName;
    }

    public void setServerName(String serverName) {
        this.serverName = serverName;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public ManagedChannel getChannel() {
        return channel;
    }

    public void setChannel(ManagedChannel channel) {
        this.channel = channel;
    }
}
