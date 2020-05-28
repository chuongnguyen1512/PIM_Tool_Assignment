package com.elca.vn.service;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.StatusRuntimeException;
import io.grpc.stub.AbstractStub;

import java.util.Iterator;
import java.util.Objects;

public abstract class BaseGRPCService<I, O> {

    protected String serverName;
    protected int port;
    protected ManagedChannel channel;

    protected BaseGRPCService(String serverName, int port) {
        this.serverName = serverName;
        this.port = port;
    }

    public O sendingRPCRequest(I object) throws StatusRuntimeException {
        try {
            channel = ManagedChannelBuilder.forAddress(serverName, port).usePlaintext().build();
            AbstractStub stub = initStub(channel);
            return callRemoteMethod(object, stub);
        } finally {
            if (Objects.nonNull(channel) && !channel.isShutdown()) {
                channel.shutdown();
            }
        }
    }

    public Iterator<O> sendingRPCRequestWithStreamingResponse(I object) throws StatusRuntimeException {
        channel = ManagedChannelBuilder.forAddress(serverName, port).usePlaintext().build();
        AbstractStub stub = initStub(channel);
        return callRemoteMethodWithStreamResponse(object, stub);
        // Method caller must handle shutdown channel after streaming all of data
    }

    protected abstract AbstractStub initStub(ManagedChannel channel);

    protected abstract O callRemoteMethod(I object, AbstractStub stub);

    protected abstract Iterator<O> callRemoteMethodWithStreamResponse(I object, AbstractStub stub);

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
