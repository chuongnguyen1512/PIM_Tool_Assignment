package com.elca.vn.proto;

import com.elca.vn.proto.model.PimProject;

import static io.grpc.MethodDescriptor.generateFullMethodName;
import static io.grpc.stub.ClientCalls.asyncUnaryCall;
import static io.grpc.stub.ClientCalls.blockingServerStreamingCall;
import static io.grpc.stub.ClientCalls.blockingUnaryCall;
import static io.grpc.stub.ClientCalls.futureUnaryCall;
import static io.grpc.stub.ServerCalls.asyncUnaryCall;
import static io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall;

/**
 */
@javax.annotation.Generated(
    value = "by gRPC proto compiler (version 1.29.0)",
    comments = "Source: base_project_service.proto")
public final class BaseProjectServiceGrpc {

  private BaseProjectServiceGrpc() {}

  public static final String SERVICE_NAME = "com.elca.vn.service.BaseProjectService";

  // Static method descriptors that strictly reflect the proto.
  private static volatile io.grpc.MethodDescriptor<PimProject,
          PimProject> getInsertNewProjectMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "insertNewProject",
      requestType = PimProject.class,
      responseType = PimProject.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<PimProject,
          PimProject> getInsertNewProjectMethod() {
    io.grpc.MethodDescriptor<PimProject, PimProject> getInsertNewProjectMethod;
    if ((getInsertNewProjectMethod = BaseProjectServiceGrpc.getInsertNewProjectMethod) == null) {
      synchronized (BaseProjectServiceGrpc.class) {
        if ((getInsertNewProjectMethod = BaseProjectServiceGrpc.getInsertNewProjectMethod) == null) {
          BaseProjectServiceGrpc.getInsertNewProjectMethod = getInsertNewProjectMethod =
              io.grpc.MethodDescriptor.<PimProject, PimProject>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "insertNewProject"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  PimProject.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  PimProject.getDefaultInstance()))
              .setSchemaDescriptor(new BaseProjectServiceMethodDescriptorSupplier("insertNewProject"))
              .build();
        }
      }
    }
    return getInsertNewProjectMethod;
  }

  private static volatile io.grpc.MethodDescriptor<PimProject,
          PimProject> getUpdateProjectMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "updateProject",
      requestType = PimProject.class,
      responseType = PimProject.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<PimProject,
          PimProject> getUpdateProjectMethod() {
    io.grpc.MethodDescriptor<PimProject, PimProject> getUpdateProjectMethod;
    if ((getUpdateProjectMethod = BaseProjectServiceGrpc.getUpdateProjectMethod) == null) {
      synchronized (BaseProjectServiceGrpc.class) {
        if ((getUpdateProjectMethod = BaseProjectServiceGrpc.getUpdateProjectMethod) == null) {
          BaseProjectServiceGrpc.getUpdateProjectMethod = getUpdateProjectMethod =
              io.grpc.MethodDescriptor.<PimProject, PimProject>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "updateProject"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  PimProject.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  PimProject.getDefaultInstance()))
              .setSchemaDescriptor(new BaseProjectServiceMethodDescriptorSupplier("updateProject"))
              .build();
        }
      }
    }
    return getUpdateProjectMethod;
  }

  /**
   * Creates a new async stub that supports all call types for the service
   */
  public static BaseProjectServiceStub newStub(io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<BaseProjectServiceStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<BaseProjectServiceStub>() {
        @java.lang.Override
        public BaseProjectServiceStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new BaseProjectServiceStub(channel, callOptions);
        }
      };
    return BaseProjectServiceStub.newStub(factory, channel);
  }

  /**
   * Creates a new blocking-style stub that supports unary and streaming output calls on the service
   */
  public static BaseProjectServiceBlockingStub newBlockingStub(
      io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<BaseProjectServiceBlockingStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<BaseProjectServiceBlockingStub>() {
        @java.lang.Override
        public BaseProjectServiceBlockingStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new BaseProjectServiceBlockingStub(channel, callOptions);
        }
      };
    return BaseProjectServiceBlockingStub.newStub(factory, channel);
  }

  /**
   * Creates a new ListenableFuture-style stub that supports unary calls on the service
   */
  public static BaseProjectServiceFutureStub newFutureStub(
      io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<BaseProjectServiceFutureStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<BaseProjectServiceFutureStub>() {
        @java.lang.Override
        public BaseProjectServiceFutureStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new BaseProjectServiceFutureStub(channel, callOptions);
        }
      };
    return BaseProjectServiceFutureStub.newStub(factory, channel);
  }

  /**
   */
  public static abstract class BaseProjectServiceImplBase implements io.grpc.BindableService {

    /**
     * <pre>
     *Insert
     * </pre>
     */
    public void insertNewProject(PimProject request,
                                 io.grpc.stub.StreamObserver<PimProject> responseObserver) {
      asyncUnimplementedUnaryCall(getInsertNewProjectMethod(), responseObserver);
    }

    /**
     * <pre>
     *Update
     * </pre>
     */
    public void updateProject(PimProject request,
                              io.grpc.stub.StreamObserver<PimProject> responseObserver) {
      asyncUnimplementedUnaryCall(getUpdateProjectMethod(), responseObserver);
    }

    @java.lang.Override public final io.grpc.ServerServiceDefinition bindService() {
      return io.grpc.ServerServiceDefinition.builder(getServiceDescriptor())
          .addMethod(
            getInsertNewProjectMethod(),
            asyncUnaryCall(
              new MethodHandlers<
                      PimProject,
                      PimProject>(
                  this, METHODID_INSERT_NEW_PROJECT)))
          .addMethod(
            getUpdateProjectMethod(),
            asyncUnaryCall(
              new MethodHandlers<
                      PimProject,
                      PimProject>(
                  this, METHODID_UPDATE_PROJECT)))
          .build();
    }
  }

  /**
   */
  public static final class BaseProjectServiceStub extends io.grpc.stub.AbstractAsyncStub<BaseProjectServiceStub> {
    private BaseProjectServiceStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected BaseProjectServiceStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new BaseProjectServiceStub(channel, callOptions);
    }

    /**
     * <pre>
     *Insert
     * </pre>
     */
    public void insertNewProject(PimProject request,
                                 io.grpc.stub.StreamObserver<PimProject> responseObserver) {
      asyncUnaryCall(
          getChannel().newCall(getInsertNewProjectMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     * <pre>
     *Update
     * </pre>
     */
    public void updateProject(PimProject request,
                              io.grpc.stub.StreamObserver<PimProject> responseObserver) {
      asyncUnaryCall(
          getChannel().newCall(getUpdateProjectMethod(), getCallOptions()), request, responseObserver);
    }
  }

  /**
   */
  public static final class BaseProjectServiceBlockingStub extends io.grpc.stub.AbstractBlockingStub<BaseProjectServiceBlockingStub> {
    private BaseProjectServiceBlockingStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected BaseProjectServiceBlockingStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new BaseProjectServiceBlockingStub(channel, callOptions);
    }

    /**
     * <pre>
     *Insert
     * </pre>
     */
    public PimProject insertNewProject(PimProject request) {
      return blockingUnaryCall(
          getChannel(), getInsertNewProjectMethod(), getCallOptions(), request);
    }

    /**
     * <pre>
     *Update
     * </pre>
     */
    public PimProject updateProject(PimProject request) {
      return blockingUnaryCall(
          getChannel(), getUpdateProjectMethod(), getCallOptions(), request);
    }
  }

  /**
   */
  public static final class BaseProjectServiceFutureStub extends io.grpc.stub.AbstractFutureStub<BaseProjectServiceFutureStub> {
    private BaseProjectServiceFutureStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected BaseProjectServiceFutureStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new BaseProjectServiceFutureStub(channel, callOptions);
    }

    /**
     * <pre>
     *Insert
     * </pre>
     */
    public com.google.common.util.concurrent.ListenableFuture<PimProject> insertNewProject(
        PimProject request) {
      return futureUnaryCall(
          getChannel().newCall(getInsertNewProjectMethod(), getCallOptions()), request);
    }

    /**
     * <pre>
     *Update
     * </pre>
     */
    public com.google.common.util.concurrent.ListenableFuture<PimProject> updateProject(
        PimProject request) {
      return futureUnaryCall(
          getChannel().newCall(getUpdateProjectMethod(), getCallOptions()), request);
    }
  }

  private static final int METHODID_INSERT_NEW_PROJECT = 0;
  private static final int METHODID_UPDATE_PROJECT = 1;

  private static final class MethodHandlers<Req, Resp> implements
      io.grpc.stub.ServerCalls.UnaryMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.ServerStreamingMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.ClientStreamingMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.BidiStreamingMethod<Req, Resp> {
    private final BaseProjectServiceImplBase serviceImpl;
    private final int methodId;

    MethodHandlers(BaseProjectServiceImplBase serviceImpl, int methodId) {
      this.serviceImpl = serviceImpl;
      this.methodId = methodId;
    }

    @java.lang.Override
    @java.lang.SuppressWarnings("unchecked")
    public void invoke(Req request, io.grpc.stub.StreamObserver<Resp> responseObserver) {
      switch (methodId) {
        case METHODID_INSERT_NEW_PROJECT:
          serviceImpl.insertNewProject((PimProject) request,
              (io.grpc.stub.StreamObserver<PimProject>) responseObserver);
          break;
        case METHODID_UPDATE_PROJECT:
          serviceImpl.updateProject((PimProject) request,
              (io.grpc.stub.StreamObserver<PimProject>) responseObserver);
          break;
        default:
          throw new AssertionError();
      }
    }

    @java.lang.Override
    @java.lang.SuppressWarnings("unchecked")
    public io.grpc.stub.StreamObserver<Req> invoke(
        io.grpc.stub.StreamObserver<Resp> responseObserver) {
      switch (methodId) {
        default:
          throw new AssertionError();
      }
    }
  }

  private static abstract class BaseProjectServiceBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoFileDescriptorSupplier, io.grpc.protobuf.ProtoServiceDescriptorSupplier {
    BaseProjectServiceBaseDescriptorSupplier() {}

    @java.lang.Override
    public com.google.protobuf.Descriptors.FileDescriptor getFileDescriptor() {
      return BaseProjectServiceProto.getDescriptor();
    }

    @java.lang.Override
    public com.google.protobuf.Descriptors.ServiceDescriptor getServiceDescriptor() {
      return getFileDescriptor().findServiceByName("BaseProjectService");
    }
  }

  private static final class BaseProjectServiceFileDescriptorSupplier
      extends BaseProjectServiceBaseDescriptorSupplier {
    BaseProjectServiceFileDescriptorSupplier() {}
  }

  private static final class BaseProjectServiceMethodDescriptorSupplier
      extends BaseProjectServiceBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoMethodDescriptorSupplier {
    private final String methodName;

    BaseProjectServiceMethodDescriptorSupplier(String methodName) {
      this.methodName = methodName;
    }

    @java.lang.Override
    public com.google.protobuf.Descriptors.MethodDescriptor getMethodDescriptor() {
      return getServiceDescriptor().findMethodByName(methodName);
    }
  }

  private static volatile io.grpc.ServiceDescriptor serviceDescriptor;

  public static io.grpc.ServiceDescriptor getServiceDescriptor() {
    io.grpc.ServiceDescriptor result = serviceDescriptor;
    if (result == null) {
      synchronized (BaseProjectServiceGrpc.class) {
        result = serviceDescriptor;
        if (result == null) {
          serviceDescriptor = result = io.grpc.ServiceDescriptor.newBuilder(SERVICE_NAME)
              .setSchemaDescriptor(new BaseProjectServiceFileDescriptorSupplier())
              .addMethod(getInsertNewProjectMethod())
              .addMethod(getUpdateProjectMethod())
              .build();
        }
      }
    }
    return result;
  }
}
