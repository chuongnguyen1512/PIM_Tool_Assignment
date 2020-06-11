package com.elca.vn.grpc;

import com.elca.vn.MockDataUtils;
import com.elca.vn.entity.Project;
import com.elca.vn.exception.PIMToolException;
import com.elca.vn.proto.model.PimProject;
import com.elca.vn.proto.model.PimProjectCountingRequest;
import com.elca.vn.proto.model.PimProjectCountingResponse;
import com.elca.vn.proto.model.PimProjectDeleteRequest;
import com.elca.vn.proto.model.PimProjectDeleteResponse;
import com.elca.vn.proto.model.PimProjectPersistRequest;
import com.elca.vn.proto.model.PimProjectPersistResponse;
import com.elca.vn.proto.model.PimProjectQueryRequest;
import com.elca.vn.proto.model.PimProjectQueryResponse;
import com.elca.vn.proto.model.ProcessingStatus;
import com.elca.vn.proto.model.Status;
import com.elca.vn.service.PimProjectServiceImpl;
import com.elca.vn.socket.MulticastPublisher;
import com.elca.vn.transform.ProjectTransformServiceImpl;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import io.grpc.StatusRuntimeException;
import io.grpc.stub.StreamObserver;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.lang.reflect.Array;

@RunWith(MockitoJUnitRunner.class)
public class ProjectGRPCServiceTest {

    private ProjectGRPCService projectGRPCService;

    @Mock
    private PimProjectServiceImpl pimProjectService;

    @Mock
    private ProjectTransformServiceImpl projectTransformService;

    @Mock
    private MulticastPublisher multicastPublisher;

    private Gson gson;

    @Before
    public void init() {
        gson = new GsonBuilder().disableHtmlEscaping().create();
        projectGRPCService = new ProjectGRPCService(pimProjectService, projectTransformService, 1, multicastPublisher, gson);
    }

    @Test
    public void shouldSavingNewProjectSuccessfully() {
        PimProjectPersistRequest request = MockDataUtils.preparePimProjectPersistRequest(ProcessingStatus.INSERT, MockDataUtils.prepareMemberVISAs(1));
        StreamObserver<PimProjectPersistResponse> streamObserver = Mockito.mock(StreamObserver.class);

        Mockito.when(projectTransformService.transformFromSourceToDes(Mockito.any(PimProject.class))).thenReturn(MockDataUtils.prepareProjectsData(1).get(0));
        Mockito.when(pimProjectService.importData(Mockito.any(Project.class))).thenReturn(MockDataUtils.prepareProjectsData(1).get(0));
        Mockito.when(projectTransformService.transformFromDesToSource(Mockito.any(Project.class))).thenReturn(MockDataUtils.preparePimProjectsData(1, ProcessingStatus.INSERT, MockDataUtils.prepareMemberVISAs(1)).get(0));

        projectGRPCService.savingProject(request, streamObserver);

        Mockito.verify(pimProjectService, Mockito.times(1)).importData(Mockito.any(Project.class));
        Mockito.verify(projectTransformService, Mockito.times(1)).transformFromSourceToDes(Mockito.any(PimProject.class));
        Mockito.verify(projectTransformService, Mockito.times(1)).transformFromDesToSource(Mockito.any(Project.class));
        Mockito.verify(streamObserver, Mockito.times(1)).onNext(Mockito.any(PimProjectPersistResponse.class));
        Mockito.verify(streamObserver, Mockito.times(1)).onCompleted();
        Mockito.verify(multicastPublisher, Mockito.timeout(2000).times(1)).sendSocketMessage(Mockito.anyString());
    }

    @Test
    public void shouldUpdateProjectSuccessfully() {
        PimProjectPersistRequest request = MockDataUtils.preparePimProjectPersistRequest(ProcessingStatus.UPDATE, MockDataUtils.prepareMemberVISAs(1));
        StreamObserver<PimProjectPersistResponse> streamObserver = Mockito.mock(StreamObserver.class);

        Mockito.when(projectTransformService.transformFromSourceToDes(Mockito.any(PimProject.class))).thenReturn(MockDataUtils.prepareProjectsData(1).get(0));
        Mockito.when(pimProjectService.updateData(Mockito.any(Project.class))).thenReturn(MockDataUtils.prepareProjectsData(1).get(0));
        Mockito.when(projectTransformService.transformFromDesToSource(Mockito.any(Project.class))).thenReturn(MockDataUtils.preparePimProjectsData(1, ProcessingStatus.UPDATE, MockDataUtils.prepareMemberVISAs(1)).get(0));

        projectGRPCService.savingProject(request, streamObserver);

        Mockito.verify(pimProjectService, Mockito.times(1)).updateData(Mockito.any(Project.class));
        Mockito.verify(projectTransformService, Mockito.times(1)).transformFromSourceToDes(Mockito.any(PimProject.class));
        Mockito.verify(projectTransformService, Mockito.times(1)).transformFromDesToSource(Mockito.any(Project.class));
        Mockito.verify(streamObserver, Mockito.times(1)).onNext(Mockito.any(PimProjectPersistResponse.class));
        Mockito.verify(streamObserver, Mockito.timeout(2000).times(1)).onCompleted();
        Mockito.verify(multicastPublisher, Mockito.timeout(2000).times(1)).sendSocketMessage(Mockito.anyString());
    }

    @Test
    public void shouldThrowExceptionWhenSavingProjectWithNullParams() {
        StreamObserver<PimProjectPersistResponse> streamObserver = Mockito.mock(StreamObserver.class);
        projectGRPCService.savingProject(null, streamObserver);

        Mockito.verify(streamObserver, Mockito.timeout(2000).times(1)).onError(Mockito.any(StatusRuntimeException.class));
    }

    @Test
    public void shouldReturnResponseIfFacingPIMToolException() {
        PimProjectPersistRequest request = MockDataUtils.preparePimProjectPersistRequest(ProcessingStatus.INSERT, MockDataUtils.prepareMemberVISAs(1));
        StreamObserver<PimProjectPersistResponse> streamObserver = Mockito.mock(StreamObserver.class);

        Mockito.when(projectTransformService.transformFromSourceToDes(Mockito.any(PimProject.class))).thenReturn(MockDataUtils.prepareProjectsData(1).get(0));
        Mockito.when(pimProjectService.importData(Mockito.any(Project.class))).thenThrow(new PIMToolException("Test PIMToolException when saving project"));

        projectGRPCService.savingProject(request, streamObserver);

        Mockito.verify(pimProjectService, Mockito.times(1)).importData(Mockito.any(Project.class));
        Mockito.verify(projectTransformService, Mockito.times(1)).transformFromSourceToDes(Mockito.any(PimProject.class));
        Mockito.verify(streamObserver, Mockito.times(1)).onNext(Mockito.any(PimProjectPersistResponse.class));
        Mockito.verify(streamObserver, Mockito.timeout(2000).times(1)).onCompleted();
    }

    @Test
    public void shouldMarkErrorResponseIfFacingException() {
        PimProjectPersistRequest request = MockDataUtils.preparePimProjectPersistRequest(ProcessingStatus.INSERT, MockDataUtils.prepareMemberVISAs(1));
        StreamObserver<PimProjectPersistResponse> streamObserver = Mockito.mock(StreamObserver.class);

        Mockito.when(projectTransformService.transformFromSourceToDes(Mockito.any(PimProject.class))).thenReturn(MockDataUtils.prepareProjectsData(1).get(0));
        Mockito.when(pimProjectService.importData(Mockito.any(Project.class))).thenThrow(new RuntimeException("Test PIMToolException when saving project"));

        projectGRPCService.savingProject(request, streamObserver);

        Mockito.verify(pimProjectService, Mockito.times(1)).importData(Mockito.any(Project.class));
        Mockito.verify(projectTransformService, Mockito.times(1)).transformFromSourceToDes(Mockito.any(PimProject.class));
        Mockito.verify(streamObserver, Mockito.timeout(2000).times(1)).onError(Mockito.any(StatusRuntimeException.class));
    }

    @Test
    public void shouldReturnSuccessResponseIfHavingErrorWhenMulticastMessage() {
        PimProjectPersistRequest request = MockDataUtils.preparePimProjectPersistRequest(ProcessingStatus.INSERT, MockDataUtils.prepareMemberVISAs(1));
        StreamObserver<PimProjectPersistResponse> streamObserver = Mockito.mock(StreamObserver.class);

        Mockito.when(projectTransformService.transformFromSourceToDes(Mockito.any(PimProject.class))).thenReturn(MockDataUtils.prepareProjectsData(1).get(0));
        Mockito.when(pimProjectService.importData(Mockito.any(Project.class))).thenReturn(MockDataUtils.prepareProjectsData(1).get(0));
        Mockito.when(projectTransformService.transformFromDesToSource(Mockito.any(Project.class))).thenReturn(MockDataUtils.preparePimProjectsData(1, ProcessingStatus.INSERT, MockDataUtils.prepareMemberVISAs(1)).get(0));
        Mockito.doThrow(new RuntimeException("Test failed when multicast message")).when(multicastPublisher).sendSocketMessage(Mockito.anyString());

        projectGRPCService.savingProject(request, streamObserver);

        Mockito.verify(pimProjectService, Mockito.times(1)).importData(Mockito.any(Project.class));
        Mockito.verify(projectTransformService, Mockito.times(1)).transformFromSourceToDes(Mockito.any(PimProject.class));
        Mockito.verify(projectTransformService, Mockito.times(1)).transformFromDesToSource(Mockito.any(Project.class));
        Mockito.verify(streamObserver, Mockito.times(1)).onNext(Mockito.any(PimProjectPersistResponse.class));
        Mockito.verify(streamObserver, Mockito.times(1)).onCompleted();
        Mockito.verify(multicastPublisher, Mockito.timeout(2000).times(1)).sendSocketMessage(Mockito.anyString());
    }

    @Test
    public void shouldGetAllProjectsSuccessfully() {
        PimProjectQueryRequest request = MockDataUtils.preparePimProjectQueryRequest(1, "", "");
        StreamObserver<PimProjectQueryResponse> streamObserver = Mockito.mock(StreamObserver.class);

        Mockito.when(pimProjectService.findAllDataWithPaging(Mockito.anyInt())).thenReturn(MockDataUtils.prepareProjectsData(1));
        Mockito.when(projectTransformService.transformFromDesToSource(Mockito.any(Project.class))).thenReturn(MockDataUtils.preparePimProjectsData(1, ProcessingStatus.INSERT, MockDataUtils.prepareMemberVISAs(1)).get(0));

        projectGRPCService.getProjects(request, streamObserver);

        Mockito.verify(pimProjectService, Mockito.times(1)).findAllDataWithPaging(Mockito.anyInt());
        Mockito.verify(projectTransformService, Mockito.times(1)).transformFromDesToSource(Mockito.any(Project.class));
        Mockito.verify(streamObserver, Mockito.times(1)).onNext(Mockito.any(PimProjectQueryResponse.class));
        Mockito.verify(streamObserver, Mockito.times(1)).onCompleted();
    }

    @Test
    public void shouldGetProjectSuccessfully() {
        PimProjectQueryRequest request = MockDataUtils.preparePimProjectQueryRequest(1, "0", Status.NEW.name());
        StreamObserver<PimProjectQueryResponse> streamObserver = Mockito.mock(StreamObserver.class);

        Mockito.when(pimProjectService.findDataWithPaging(Mockito.anyInt(), Mockito.any())).thenReturn(MockDataUtils.prepareProjectsData(1));
        Mockito.when(projectTransformService.transformFromDesToSource(Mockito.any(Project.class))).thenReturn(MockDataUtils.preparePimProjectsData(1, ProcessingStatus.INSERT, MockDataUtils.prepareMemberVISAs(1)).get(0));

        projectGRPCService.getProjects(request, streamObserver);

        Mockito.verify(pimProjectService, Mockito.times(1)).findDataWithPaging(Mockito.anyInt(), Mockito.any());
        Mockito.verify(projectTransformService, Mockito.times(1)).transformFromDesToSource(Mockito.any(Project.class));
        Mockito.verify(streamObserver, Mockito.times(1)).onNext(Mockito.any(PimProjectQueryResponse.class));
        Mockito.verify(streamObserver, Mockito.times(1)).onCompleted();
    }

    @Test
    public void shouldGetTotalNumOfAllProjectsSuccessfully() {
        PimProjectCountingRequest request = MockDataUtils.preparePimProjectCountingRequest("", "");
        StreamObserver<PimProjectCountingResponse> streamObserver = Mockito.mock(StreamObserver.class);

        Mockito.when(pimProjectService.getTotalDataSize()).thenReturn(1L);

        projectGRPCService.getTotalNumOfProjects(request, streamObserver);

        Mockito.verify(pimProjectService, Mockito.times(1)).getTotalDataSize();
        Mockito.verify(streamObserver, Mockito.times(1)).onNext(Mockito.any(PimProjectCountingResponse.class));
        Mockito.verify(streamObserver, Mockito.times(1)).onCompleted();
    }

    @Test
    public void shouldGetTotalNumOfProjectSuccessfully() {
        PimProjectCountingRequest request = MockDataUtils.preparePimProjectCountingRequest("0", Status.NEW.name());
        StreamObserver<PimProjectCountingResponse> streamObserver = Mockito.mock(StreamObserver.class);

        Mockito.when(pimProjectService.getTotalDataSize(Mockito.any())).thenReturn(1L);

        projectGRPCService.getTotalNumOfProjects(request, streamObserver);

        Mockito.verify(pimProjectService, Mockito.times(1)).getTotalDataSize(Mockito.any());
        Mockito.verify(streamObserver, Mockito.times(1)).onNext(Mockito.any(PimProjectCountingResponse.class));
        Mockito.verify(streamObserver, Mockito.times(1)).onCompleted();
    }

    @Test
    public void shouldDeleteProjectSuccessfully() {
        PimProjectDeleteRequest request = MockDataUtils.preparePimProjectDeleteRequest(MockDataUtils.prepareProjectIDsData(1));
        StreamObserver<PimProjectDeleteResponse> streamObserver = Mockito.mock(StreamObserver.class);

        Mockito.when(pimProjectService.deleteData(Mockito.anyList())).thenReturn(1);

        projectGRPCService.deleteProjects(request, streamObserver);

        Mockito.verify(pimProjectService, Mockito.times(1)).deleteData(Mockito.anyList());
        Mockito.verify(streamObserver, Mockito.times(1)).onNext(Mockito.any(PimProjectDeleteResponse.class));
        Mockito.verify(streamObserver, Mockito.times(1)).onCompleted();
    }

}
