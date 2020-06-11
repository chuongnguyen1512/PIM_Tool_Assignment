package com.elca.vn.service;

import com.elca.vn.MockDataUtils;
import com.elca.vn.entity.Employee;
import com.elca.vn.entity.Project;
import com.elca.vn.exception.PIMToolException;
import com.elca.vn.proto.model.Status;
import com.elca.vn.repository.EmployeeRepository;
import com.elca.vn.repository.ProjectRepository;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Set;

@RunWith(MockitoJUnitRunner.class)
public class PimProjectServiceImplTest {

    private BasePimDataService<Project> projectService;

    @Mock
    private ProjectRepository projectRepository;

    @Mock
    private EmployeeRepository employeeRepository;

    @Before
    public void init() {
        projectService = new PimProjectServiceImpl(projectRepository, employeeRepository);
    }

    @Test
    public void shouldImportProjectDataSuccessfully() {
        Project project = MockDataUtils.prepareProjectsData(1).get(0);
        Set<Employee> employees = MockDataUtils.prepareEmployeesData(1);

        Mockito.when(projectRepository.findByProjectNumber(Mockito.anyInt())).thenReturn(null);
        Mockito.when(projectRepository.save(Mockito.any(Project.class))).thenReturn(project);
        Mockito.when(employeeRepository.findEmployeeByVisas(Mockito.anyList())).thenReturn(employees);

        Project resultProject = projectService.importData(project);

        Assert.assertNotNull(resultProject);
        Assert.assertFalse(CollectionUtils.isEmpty(resultProject.getEmployees()));
        Mockito.verify(projectRepository, Mockito.times(1)).findByProjectNumber(Mockito.anyInt());
        Mockito.verify(projectRepository, Mockito.times(1)).save(Mockito.any(Project.class));
        Mockito.verify(employeeRepository, Mockito.times(1)).findEmployeeByVisas(Mockito.anyList());
    }

    @Test
    public void shouldImportProjectDataSuccessfullyWithoutMembers() {
        Project project = MockDataUtils.prepareProjectsData(1).get(0);
        project.setEmployees(null);

        Mockito.when(projectRepository.findByProjectNumber(Mockito.anyInt())).thenReturn(null);
        Mockito.when(projectRepository.save(Mockito.any(Project.class))).thenReturn(project);

        Project resultProject = projectService.importData(project);

        Assert.assertNotNull(resultProject);
        Assert.assertTrue(CollectionUtils.isEmpty(resultProject.getEmployees()));
        Mockito.verify(projectRepository, Mockito.times(1)).findByProjectNumber(Mockito.anyInt());
        Mockito.verify(projectRepository, Mockito.times(1)).save(Mockito.any(Project.class));
        Mockito.verify(employeeRepository, Mockito.times(0)).findEmployeeByVisas(Mockito.anyList());
    }

    @Test(expected = PIMToolException.class)
    public void shouldThrowExceptionWhenProjectIsExistingInDB() {
        Project project = MockDataUtils.prepareProjectsData(1).get(0);
        Mockito.when(projectRepository.findByProjectNumber(Mockito.anyInt())).thenReturn(project);
        projectService.importData(project);
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowExceptionWhenParamsIsNull() {
        projectService.importData(null);
    }

    @Test(expected = PIMToolException.class)
    public void shouldThrowExceptionWhenMemberVISAsAreNotExistingInDB() {
        Project project = MockDataUtils.prepareProjectsData(1).get(0);
        Mockito.when(projectRepository.findByProjectNumber(Mockito.anyInt())).thenReturn(null);
        Mockito.when(employeeRepository.findEmployeeByVisas(Mockito.anyList())).thenReturn(null);
        projectService.importData(project);
    }

    @Test
    public void shouldUpdateProjectDataSuccessfully() {
        Project project = MockDataUtils.prepareProjectsData(1).get(0);
        Set<Employee> employees = MockDataUtils.prepareEmployeesData(1);

        Mockito.when(projectRepository.findByProjectNumber(Mockito.anyInt())).thenReturn(project);
        Mockito.when(projectRepository.save(Mockito.any(Project.class))).thenReturn(project);
        Mockito.when(employeeRepository.findEmployeeByVisas(Mockito.anyList())).thenReturn(employees);

        Project resultProject = projectService.updateData(project);

        Assert.assertNotNull(resultProject);
        Assert.assertFalse(CollectionUtils.isEmpty(resultProject.getEmployees()));
        Mockito.verify(projectRepository, Mockito.times(1)).findByProjectNumber(Mockito.anyInt());
        Mockito.verify(projectRepository, Mockito.times(1)).save(Mockito.any(Project.class));
        Mockito.verify(employeeRepository, Mockito.times(1)).findEmployeeByVisas(Mockito.anyList());
    }

    @Test
    public void shouldUpdateProjectDataSuccessfullyWithoutMembers() {
        Project project = MockDataUtils.prepareProjectsData(1).get(0);
        project.setEmployees(null);

        Mockito.when(projectRepository.findByProjectNumber(Mockito.anyInt())).thenReturn(project);
        Mockito.when(projectRepository.save(Mockito.any(Project.class))).thenReturn(project);

        Project resultProject = projectService.updateData(project);

        Assert.assertNotNull(resultProject);
        Assert.assertTrue(CollectionUtils.isEmpty(resultProject.getEmployees()));
        Mockito.verify(projectRepository, Mockito.times(1)).findByProjectNumber(Mockito.anyInt());
        Mockito.verify(projectRepository, Mockito.times(1)).save(Mockito.any(Project.class));
        Mockito.verify(employeeRepository, Mockito.times(0)).findEmployeeByVisas(Mockito.anyList());
    }

    @Test(expected = PIMToolException.class)
    public void shouldThrowExceptionWhenUpdatingProjectIsNotExistingInDB() {
        Project project = MockDataUtils.prepareProjectsData(1).get(0);
        Mockito.when(projectRepository.findByProjectNumber(Mockito.anyInt())).thenReturn(null);
        projectService.updateData(project);
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowExceptionWhenUpdatingAndParamsIsNull() {
        projectService.updateData(null);
    }

    @Test(expected = PIMToolException.class)
    public void shouldThrowExceptionWhenUpdatingAndMemberVISAsAreNotExistingInDB() {
        Project project = MockDataUtils.prepareProjectsData(1).get(0);
        Mockito.when(projectRepository.findByProjectNumber(Mockito.anyInt())).thenReturn(project);
        projectService.updateData(project);
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowExceptionWhenFindDataWithPagingAndNullParams() {
        projectService.findDataWithPaging(0, null);
    }

    @Test
    public void shouldFindProjectDataWithPagingAndContentIsNumberSuccessfully() {
        List<Project> projects = MockDataUtils.prepareProjectsData(1);
        Page<Project> projectsBatch = new PageImpl(projects);

        Mockito.when(projectRepository.findByProjectWithProjectNumAndStatus(Mockito.anyInt(),
                Mockito.anyString(), Mockito.any(Pageable.class)))
                .thenReturn(projectsBatch);

        List<Project> result = (List<Project>) projectService.findDataWithPaging(1, String.valueOf(projects.get(0).getProjectNumber()), Status.NEW.name());

        Assert.assertFalse(CollectionUtils.isEmpty(result));
        Mockito.verify(projectRepository, Mockito.times(1)).findByProjectWithProjectNumAndStatus(Mockito.anyInt(), Mockito.anyString(), Mockito.any(Pageable.class));
    }

    @Test
    public void shouldFindProjectDataWithPagingAndContentIsNotNumberSuccessfully() {
        List<Project> projects = MockDataUtils.prepareProjectsData(1);
        Page<Project> projectsBatch = new PageImpl(projects);

        Mockito.when(projectRepository.findByProjectWithSearchContentAndStatus(Mockito.anyString(),
                Mockito.anyString(), Mockito.any(Pageable.class))).thenReturn(projectsBatch);

        List<Project> result = (List<Project>) projectService.findDataWithPaging(1, String.valueOf(projects.get(0).getProjectNumber()), Status.NEW.name());

        Assert.assertFalse(CollectionUtils.isEmpty(result));
        Mockito.verify(projectRepository, Mockito.times(1)).findByProjectWithProjectNumAndStatus(Mockito.anyInt(), Mockito.anyString(), Mockito.any(Pageable.class));
        Mockito.verify(projectRepository, Mockito.times(1)).findByProjectWithSearchContentAndStatus(Mockito.anyString(), Mockito.anyString(), Mockito.any(Pageable.class));
    }

    @Test
    public void shouldFindProjectDataWithPagingAndProjectIsNotExistingSuccessfully() {
        List<Project> projects = MockDataUtils.prepareProjectsData(1);
        List<Project> result = (List<Project>) projectService.findDataWithPaging(1, String.valueOf(projects.get(0).getProjectNumber()), Status.NEW.name());

        Assert.assertTrue(CollectionUtils.isEmpty(result));
        Mockito.verify(projectRepository, Mockito.times(1)).findByProjectWithProjectNumAndStatus(Mockito.anyInt(), Mockito.anyString(), Mockito.any(Pageable.class));
        Mockito.verify(projectRepository, Mockito.times(1)).findByProjectWithSearchContentAndStatus(Mockito.anyString(), Mockito.anyString(), Mockito.any(Pageable.class));
    }

    @Test
    public void shouldFindAllProjectDataWithPagingSuccessfully() {
        List<Project> projects = MockDataUtils.prepareProjectsData(1);
        Page<Project> projectsBatch = new PageImpl(projects);

        Mockito.when(projectRepository.findAllWithPaging(Mockito.any(Pageable.class))).thenReturn(projectsBatch);

        List<Project> result = (List<Project>) projectService.findAllDataWithPaging(1);

        Assert.assertFalse(CollectionUtils.isEmpty(result));
        Mockito.verify(projectRepository, Mockito.times(1)).findAllWithPaging(Mockito.any(Pageable.class));
    }

    @Test
    public void shouldFindAllProjectDataWithPagingWithEmptyDBDataSuccessfully() {
        List<Project> result = (List<Project>) projectService.findAllDataWithPaging(1);

        Assert.assertTrue(CollectionUtils.isEmpty(result));
        Mockito.verify(projectRepository, Mockito.times(1)).findAllWithPaging(Mockito.any(Pageable.class));
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowExceptionWhenFindAllDataWithIndexPageIsNegativeNumber() {
        projectService.findAllDataWithPaging(-1);
    }

    @Test
    public void shouldDeleteDataSuccessfully() {
        List<Integer> projectIDs = MockDataUtils.prepareProjectIDsData(1);

        Mockito.when(projectRepository.deleteByProjectNumbers(Mockito.any(List.class))).thenReturn(projectIDs.size());

        int result = projectService.deleteData(projectIDs);

        Assert.assertTrue(result > 0);
        Mockito.verify(projectRepository, Mockito.times(1)).deleteByProjectNumbers(Mockito.any(List.class));
    }

    @Test
    public void shouldNotDeleteDataWithProjectNumbersNotExisting() {
        List<Integer> projectNumbers = MockDataUtils.prepareProjectIDsData(1);

        Mockito.when(projectRepository.deleteByProjectNumbers(Mockito.any(List.class))).thenReturn(0);

        int result = projectService.deleteData(projectNumbers);

        Assert.assertTrue(result == 0);
        Mockito.verify(projectRepository, Mockito.times(1)).deleteByProjectNumbers(Mockito.any(List.class));
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowExceptionWhenDeleteDataWithNullParams() {
        projectService.deleteData(null);
    }

    @Test
    public void shouldGetAllDataSizeSuccessfully() {
        Mockito.when(projectRepository.countAllProjects()).thenReturn(1L);
        long result = projectService.getTotalDataSize();

        Assert.assertEquals(1L, result);
        Mockito.verify(projectRepository, Mockito.times(1)).countAllProjects();
    }

    @Test
    public void shouldGetAllDataSizeWithSearchContentSuccessfully() {
        Mockito.when(projectRepository.countProjectsWithProjectNumAndStatus(Mockito.anyInt(), Mockito.anyString())).thenReturn(1L);
        long result = projectService.getTotalDataSize("0", Status.NEW.name());

        Assert.assertEquals(1L, result);
        Mockito.verify(projectRepository, Mockito.times(1)).countProjectsWithProjectNumAndStatus(Mockito.anyInt(), Mockito.anyString());
    }

    @Test
    public void shouldGetAllDataSizeWithSearchContentIsANumberSuccessfully() {
        Mockito.when(projectRepository.countProjectsWithSearchContentAndStatus(Mockito.anyString(), Mockito.anyString())).thenReturn(1L);
        long result = projectService.getTotalDataSize("Project 0", Status.NEW.name());

        Assert.assertEquals(1L, result);
        Mockito.verify(projectRepository, Mockito.times(1)).countProjectsWithSearchContentAndStatus(Mockito.anyString(), Mockito.anyString());
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowExceptionIfGetDataSizeWithNullParams() {
        projectService.getTotalDataSize(null);
    }
}
