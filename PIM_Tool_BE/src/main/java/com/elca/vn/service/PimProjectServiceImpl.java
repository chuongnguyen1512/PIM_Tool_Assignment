package com.elca.vn.service;

import com.elca.vn.entity.Employee;
import com.elca.vn.entity.Project;
import com.elca.vn.exception.PIMToolException;
import com.elca.vn.repository.EmployeeRepository;
import com.elca.vn.repository.ProjectRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import static com.elca.vn.constant.BundleConstant.EMPLOYEES_NOT_EXIST_SYSTEM_MSG_BUNDLE_ID;
import static com.elca.vn.constant.BundleConstant.PROJECT_EXISTING_SYSTEM_MSG_BUNDLE_ID;
import static com.elca.vn.constant.StylesheetConstant.PAGING_SIZE;

/**
 * Service class for processing {@link Project} data
 */
@Service
public class PimProjectServiceImpl implements BasePimDataService<Project> {

    private static final Logger LOGGER = LoggerFactory.getLogger(PimProjectServiceImpl.class);

    private ProjectRepository projectRepository;
    private EmployeeRepository employeeRepository;

    @Autowired
    public PimProjectServiceImpl(ProjectRepository projectRepository, EmployeeRepository employeeRepository) {
        this.projectRepository = projectRepository;
        this.employeeRepository = employeeRepository;
    }

    /**
     * Verify project is existing or not and persist to DB
     *
     * @param project project data
     * @return imported project data
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public Project importData(Project project) throws PIMToolException {
        if (Objects.isNull(project)) {
            String errorMsg = "Project is null";
            LOGGER.warn(errorMsg);
            throw new IllegalArgumentException(errorMsg);
        }

        // Finding project
        Project dbProject = projectRepository.findByProjectNumber(project.getProjectNumber());

        if (Objects.nonNull(dbProject)) {
            LOGGER.warn("Project has already existed in system");
            throw new PIMToolException(PROJECT_EXISTING_SYSTEM_MSG_BUNDLE_ID);
        }

        // Verify member visas are existing
        List<String> visaIDs = project.getEmployees().stream().map(Employee::getVisa).collect(Collectors.toList());
        if (!CollectionUtils.isEmpty(visaIDs)) {
            Set<Employee> dbEmployees = employeeRepository.findEmployeeByVisas(visaIDs);

            if (CollectionUtils.isEmpty(dbEmployees)) {
                LOGGER.warn("Member VISAS are not existing in system");
                throw new PIMToolException(EMPLOYEES_NOT_EXIST_SYSTEM_MSG_BUNDLE_ID);
            }
            project.setEmployees(dbEmployees);
        }

        return projectRepository.save(project);
    }

    /**
     * Finding projects with given index page and content searching per paging
     *
     * @param indexPage     current index page
     * @param contentSearch searching content
     * @return collection of projects in current page
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public List<Project> findDataWithPaging(int indexPage, String... contentSearch) {
        verifySearchingData(indexPage, contentSearch);

        // Index 0 for content searching, index 1 for status info
        String content = contentSearch[0];
        String status = contentSearch[1];

        int projectNum = toNumber(content);

        Page<Project> projectsBatch = null;
        PageRequest page = PageRequest.of(indexPage, PAGING_SIZE);

        if (projectNum > 0) {
            // If content is num, try find with project num first
            projectsBatch = projectRepository.findByProjectWithProjectNumAndStatus(projectNum, status, page);

        }

        if (Objects.isNull(projectsBatch) || projectsBatch.isEmpty()) {
            projectsBatch = projectRepository.findByProjectWithSearchContentAndStatus(content, status, page);
        }

        if (Objects.isNull(projectsBatch) || projectsBatch.isEmpty()) {
            LOGGER.warn("No searching projects have been found in current page", indexPage);
            return new ArrayList();
        }

        return projectsBatch.getContent();
    }

    /**
     * Getting all of projects with index paging
     *
     * @param indexPage index paging
     * @return all of projects belong to index page with page size
     */
    @Override
    public List<Project> findAllDataWithPaging(int indexPage) {
        if (indexPage < 0) {
            String errorMsg = String.format("Index page %s is not correct", indexPage);
            LOGGER.warn(errorMsg);
            throw new IllegalArgumentException(errorMsg);
        }

        PageRequest page = PageRequest.of(indexPage, PAGING_SIZE);
        Page<Project> projectsBatch = projectRepository.findAllWithPaging(page);

        if (Objects.isNull(projectsBatch) || projectsBatch.isEmpty()) {
            LOGGER.warn("No searching projects have been found in current page", indexPage);
            return new ArrayList();
        }
        return projectsBatch.getContent();
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public int deleteData(List<Integer> deleteIDs) {
        if (CollectionUtils.isEmpty(deleteIDs)) {
            String errorMsg = String.format("Collection of delete ids %s is null or emtpy", deleteIDs);
            LOGGER.warn(errorMsg);
            throw new IllegalArgumentException(errorMsg);
        }
        int deleteRows = projectRepository.deleteByProjectNumbers(deleteIDs);
        if (deleteRows <= 0) {
            LOGGER.warn("Not found any projects match with given ids to delete");
        }
        return deleteRows;
    }

    @Override
    public Project queryData(String id) {
        return null;
    }

    @Override
    public Iterator<Project> getData() {
        return null;
    }

    /**
     * Get number of total records in DB
     *
     * @return number of records in DB
     */
    @Override
    public long getTotalDataSize() {
        return projectRepository.countAllProjects();
    }

    /**
     * Find how many finding records with given searching content
     *
     * @param contentSearch searching content
     * @return number of projects corresponding with searching content
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public long getTotalDataSize(String... contentSearch) {
        // Set 1 to skip validate for indexPage
        verifySearchingData(1, contentSearch);

        // Index 0 for content searching, index 1 for status info
        String content = contentSearch[0];
        String status = contentSearch[1];

        int projectNum = toNumber(content);
        if (projectNum > 0) {
            return projectRepository.countProjectsWithProjectNumAndStatus(projectNum, status);
        }
        return projectRepository.countProjectsWithSearchContentAndStatus(content, status);
    }

    private void verifySearchingData(int indexPage, String... contentSearch) {
        if (Objects.isNull(contentSearch) || contentSearch.length == 0 || indexPage < 0) {
            String errorMsg = "Inputs searching for project are not valid";
            LOGGER.warn(errorMsg);
            throw new IllegalArgumentException(errorMsg);
        }
    }

    private int toNumber(String value) {
        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException e) {
            LOGGER.warn("Content searching {} is not a number", value);
        }
        return -1;
    }
}

