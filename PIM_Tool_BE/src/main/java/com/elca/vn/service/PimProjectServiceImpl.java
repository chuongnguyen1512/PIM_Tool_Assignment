package com.elca.vn.service;

import com.elca.vn.entity.Employee;
import com.elca.vn.entity.Project;
import com.elca.vn.exception.ProjectImportException;
import com.elca.vn.repository.EmployeeRepository;
import com.elca.vn.repository.ProjectRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import static com.elca.vn.constant.BundleConstant.EMPLOYEES_NOT_EXIST_SYSTEM_MSG_BUNDLE_ID;
import static com.elca.vn.constant.BundleConstant.PROJECT_EXISTING_SYSTEM_MSG_BUNDLE_ID;

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

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public Project importData(Project project) {
        if (Objects.isNull(project)) {
            String errorMsg = "Project is null";
            LOGGER.warn(errorMsg);
            throw new IllegalArgumentException(errorMsg);
        }

        // Finding project
        Project dbProject = projectRepository.findByProjectNumber(project.getProjectNumber());

        if (Objects.nonNull(dbProject)) {
            LOGGER.warn("Project has already existed in system");
            throw new ProjectImportException(PROJECT_EXISTING_SYSTEM_MSG_BUNDLE_ID);
        }

        // Verify member visas are existing
        List<String> visaIDs = project.getEmployees().stream().map(Employee::getVisa).collect(Collectors.toList());
        if (!CollectionUtils.isEmpty(visaIDs)) {
            Set<Employee> dbEmployees = employeeRepository.findEmployeeByVisas(visaIDs);

            if (CollectionUtils.isEmpty(dbEmployees)) {
                LOGGER.warn("Member VISAS are not existing in system");
                throw new ProjectImportException(EMPLOYEES_NOT_EXIST_SYSTEM_MSG_BUNDLE_ID);
            }
            project.setEmployees(dbEmployees);
        }

        return projectRepository.save(project);
    }

    @Override
    public List<Project> queryData(String id) {
        return null;
    }

    @Override
    public Iterator<Project> getData() {
        return null;
    }

}

