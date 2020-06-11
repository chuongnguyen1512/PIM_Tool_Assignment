package com.elca.vn.grpc;

import com.elca.vn.MockDataUtils;
import com.elca.vn.entity.Group;
import com.elca.vn.exception.PIMToolException;
import com.elca.vn.proto.model.PimGroupRequest;
import com.elca.vn.proto.model.PimGroupResponse;
import com.elca.vn.service.PimGroupServiceImpl;
import com.elca.vn.transform.GroupTransformServiceImpl;
import io.grpc.StatusRuntimeException;
import io.grpc.stub.StreamObserver;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class GroupGRPCServiceTest {

    private GroupGRPCService groupGRPCService;

    @Mock
    private PimGroupServiceImpl groupService;

    @Mock
    private GroupTransformServiceImpl groupTransformService;

    @Before
    public void init() {
        groupGRPCService = new GroupGRPCService(groupService, groupTransformService);
    }

    @Test
    public void shouldGetGroupDataSuccessfully() {
        PimGroupRequest groupRequest = MockDataUtils.preparePimGroupRequest();

        StreamObserver<PimGroupResponse> streamObserver = Mockito.mock(StreamObserver.class);
        Mockito.when(groupService.getData()).thenReturn(MockDataUtils.prepareGroups(1));
        Mockito.when(groupTransformService.transformFromDesToSource(Mockito.any(Group.class)))
                .thenReturn(MockDataUtils.preparePimGroup(1).get(0));

        groupGRPCService.getGroupData(groupRequest, streamObserver);

        Mockito.verify(groupService, Mockito.times(1)).getData();
        Mockito.verify(groupTransformService, Mockito.times(1)).transformFromDesToSource(Mockito.any(Group.class));
        Mockito.verify(streamObserver, Mockito.times(1)).onNext(Mockito.any(PimGroupResponse.class));
        Mockito.verify(streamObserver, Mockito.times(1)).onCompleted();
    }

    @Test
    public void shouldGetGroupDataWithEmptyDBData() {
        PimGroupRequest groupRequest = MockDataUtils.preparePimGroupRequest();

        StreamObserver<PimGroupResponse> streamObserver = Mockito.mock(StreamObserver.class);
        groupGRPCService.getGroupData(groupRequest, streamObserver);

        Mockito.verify(groupService, Mockito.times(1)).getData();
        Mockito.verify(streamObserver, Mockito.times(1)).onNext(Mockito.any(PimGroupResponse.class));
        Mockito.verify(streamObserver, Mockito.times(1)).onCompleted();
    }

    @Test
    public void shouldMarkStreamObserverResponseErrorWithNullParams() {
        StreamObserver<PimGroupResponse> streamObserver = Mockito.mock(StreamObserver.class);
        groupGRPCService.getGroupData(null, streamObserver);

        Mockito.verify(streamObserver, Mockito.times(1)).onError(Mockito.any(StatusRuntimeException.class));
    }

    @Test
    public void shouldReturnResponseIfFacingPIMToolException() {
        PimGroupRequest groupRequest = MockDataUtils.preparePimGroupRequest();

        StreamObserver<PimGroupResponse> streamObserver = Mockito.mock(StreamObserver.class);
        Mockito.when(groupService.getData()).thenThrow(new PIMToolException("Test PimToolException when getting group data"));

        groupGRPCService.getGroupData(groupRequest, streamObserver);

        Mockito.verify(groupService, Mockito.times(1)).getData();
        Mockito.verify(streamObserver, Mockito.times(1)).onNext(Mockito.any(PimGroupResponse.class));
        Mockito.verify(streamObserver, Mockito.times(1)).onCompleted();
    }

    @Test
    public void shouldMarkStreamObserverResponseErrorIfFacingException() {
        PimGroupRequest groupRequest = MockDataUtils.preparePimGroupRequest();

        StreamObserver<PimGroupResponse> streamObserver = Mockito.mock(StreamObserver.class);
        Mockito.when(groupService.getData()).thenThrow(new RuntimeException("Test Exception when getting group data"));

        groupGRPCService.getGroupData(groupRequest, streamObserver);

        Mockito.verify(groupService, Mockito.times(1)).getData();
        Mockito.verify(streamObserver, Mockito.times(1)).onError(Mockito.any(StatusRuntimeException.class));
    }
}
