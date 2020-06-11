package com.elca.vn.transform;

import com.elca.vn.entity.Employee;
import com.elca.vn.entity.Group;
import com.elca.vn.entity.Project;
import com.elca.vn.proto.model.PimProject;
import com.elca.vn.proto.model.Status;
import com.elca.vn.util.PIMToolUtils;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Transform service for pair models {@link PimProject} and {@link Project}
 */
@Service
public class ProjectTransformServiceImpl implements BaseTransformService<PimProject, Project> {

    /**
     * {@inheritDoc}
     *
     * @param sourceObject
     * @return
     */
    public Project transformFromSourceToDes(PimProject sourceObject) {
        if (Objects.isNull(sourceObject)) {
            return null;
        }
        Project project = new Project();
        project.setProjectNumber(sourceObject.getProjectNumber());
        project.setCustomer(sourceObject.getCustomer());
        project.setStartDate(new Date(sourceObject.getStartDate().getSeconds() * 1000));
        project.setEndDate(new Date(sourceObject.getEndDate().getSeconds() * 1000));
        project.setStatus(sourceObject.getStatus().toString());
        project.setName(sourceObject.getProjectName());
        project.setEmployees(transformToEmployees(sourceObject.getMemberVISAsList()));
        project.setGroup(transformToGroup(sourceObject.getGroupID()));
        return project;
    }

    /**
     * {@inheritDoc}
     *
     * @param desProject
     * @return
     */
    public PimProject transformFromDesToSource(Project desProject) {
        if (Objects.isNull(desProject)) {
            return null;
        }
        Group group = desProject.getGroup();
        if (Objects.nonNull(group)) {
            return PimProject.newBuilder()
                    .setProjectNumber(desProject.getProjectNumber())
                    .setProjectName(desProject.getName())
                    .setCustomer(desProject.getCustomer())
                    .setStatus(Status.valueOf(desProject.getStatus()))
                    .setStartDate(PIMToolUtils.convertToTimestamp(desProject.getStartDate()))
                    .setEndDate(PIMToolUtils.convertToTimestamp(desProject.getEndDate()))
                    .addAllMemberVISAs(desProject.getEmployees().stream().map(Employee::getVisa).collect(Collectors.toList()))
                    .setGroupID(group.getId())
                    .build();
        }
        return PimProject.newBuilder()
                .setProjectNumber(desProject.getProjectNumber())
                .setProjectName(desProject.getName())
                .setCustomer(desProject.getCustomer())
                .setStatus(Status.valueOf(desProject.getStatus()))
                .setStartDate(PIMToolUtils.convertToTimestamp(desProject.getStartDate()))
                .setEndDate(PIMToolUtils.convertToTimestamp(desProject.getEndDate()))
                .addAllMemberVISAs(desProject.getEmployees().stream().map(Employee::getVisa).collect(Collectors.toList()))
                .build();
    }

    private Set<Employee> transformToEmployees(List<String> visas) {
        return visas.stream().filter(Objects::nonNull).map(x -> {
            Employee employee = new Employee();
            employee.setVisa(x);
            return employee;
        }).collect(Collectors.toSet());
    }

    private Group transformToGroup(long groupID) {
        Group group = new Group();
        group.setId(groupID);
        return group;
    }
}
