package com.elca.vn;

import com.elca.vn.entity.Employee;
import com.elca.vn.entity.Group;
import com.elca.vn.entity.Project;
import com.elca.vn.proto.model.PimGroup;
import com.elca.vn.proto.model.PimGroupRequest;
import com.elca.vn.proto.model.PimGroupResponse;
import com.elca.vn.proto.model.PimProject;
import com.elca.vn.proto.model.PimProjectCountingRequest;
import com.elca.vn.proto.model.PimProjectDeleteRequest;
import com.elca.vn.proto.model.PimProjectPersistRequest;
import com.elca.vn.proto.model.PimProjectQueryRequest;
import com.elca.vn.proto.model.ProcessingStatus;
import com.elca.vn.proto.model.Status;
import com.google.protobuf.Timestamp;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

public class MockDataUtils {

    private MockDataUtils() {
    }

    public static List<Group> prepareGroups(int size) {
        if (size <= 0) {
            return new ArrayList<>();
        }
        List<Group> groups = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            Group group = new Group();
            group.setId(Long.valueOf(i));
            group.setGroupName("Group " + i);
            group.setVersion(Long.valueOf(i));
            groups.add(group);
        }
        return groups;
    }

    public static List<Object[]> prepareDBGroups(int size) {
        if (size <= 0) {
            return new ArrayList<>();
        }
        List<Object[]> groups = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            Object[] groupData = {Long.valueOf(i), "Group " + i};
            groups.add(groupData);
        }
        return groups;
    }

    public static List<Project> prepareProjectsData(int size) {
        if (size <= 0) {
            return new ArrayList<>();
        }
        List<Project> projects = new ArrayList<>();
        List<Group> groups = prepareGroups(size);
        Set<Employee> employees = prepareEmployeesData(size);
        for (int i = 0; i < size; i++) {
            Project project = new Project();
            project.setId(Long.valueOf(i));
            project.setName("Project " + i);
            project.setCustomer("Customer " + i);
            project.setGroup(groups.get(i));
            project.setStatus(Status.NEW.name());
            project.setVersion(Long.valueOf(i));
            project.setStartDate(new Date());
            project.setEndDate(new Date());
            project.setEmployees(employees);
            projects.add(project);
        }
        return projects;
    }

    public static List<Integer> prepareProjectIDsData(int size) {
        if (size <= 0) {
            return new ArrayList<>();
        }
        return prepareProjectsData(size).stream().map(Project::getProjectNumber).collect(Collectors.toList());
    }

    public static Set<Employee> prepareEmployeesData(int size) {
        if (size <= 0) {
            return new HashSet<>();
        }
        Set<Employee> employees = new HashSet<>();
        for (int i = 0; i < size; i++) {
            Employee employee = new Employee();
            employee.setId(Long.valueOf(i));
            employee.setVisa("Visa " + i);
            employee.setVersion(Long.valueOf(i));
            employee.setBirthDate(new Date());
            employee.setFirstName("First name " + i);
            employee.setLastName("Last name " + i);
            employees.add(employee);
        }
        return employees;
    }

    public static List<PimGroup> preparePimGroup(int size) {
        if (size <= 0) {
            return new ArrayList<>();
        }
        List<PimGroup> groups = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            PimGroup group = PimGroup.newBuilder()
                    .setGroupID(Long.valueOf(i))
                    .setGroupName("Group " + i)
                    .build();
            groups.add(group);
        }
        return groups;
    }

    public static PimGroupRequest preparePimGroupRequest() {
        return PimGroupRequest.newBuilder()
                .setGroupID(1L)
                .setTransactionID(UUID.randomUUID().toString())
                .build();
    }

    public static PimGroupResponse preparePimGroupResponse(int size, boolean isSuccess, String bundleID) {
        return PimGroupResponse.newBuilder()
                .setIsSuccess(isSuccess)
                .addAllGroups(preparePimGroup(size))
                .setBundleID(bundleID)
                .setTransactionID(UUID.randomUUID().toString())
                .build();
    }

    public static PimProjectPersistRequest preparePimProjectPersistRequest(ProcessingStatus processingStatus, List<String> memberVISAs) {
        return PimProjectPersistRequest.newBuilder()
                .setTransactionID(UUID.randomUUID().toString())
                .setProject(preparePimProjectsData(1, processingStatus, memberVISAs).get(0))
                .build();
    }

    public static List<PimProject> preparePimProjectsData(int size, ProcessingStatus processingStatus, List<String> memberVISAs) {
        if (size <= 0) {
            return new ArrayList<>();
        }
        List<PimProject> projects = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            PimProject project = PimProject.newBuilder()
                    .setProjectNumber(i)
                    .setProjectName("Project " + i)
                    .setCustomer("Customer" + i)
                    .setStatus(Status.NEW)
                    .setGroupID(preparePimGroup(1).get(0).getGroupID())
                    .setProcessingStatus(processingStatus)
                    .addAllMemberVISAs(memberVISAs)
                    .setStartDate(Timestamp.newBuilder().build())
                    .setEndDate(Timestamp.newBuilder().build())
                    .build();
            projects.add(project);
        }
        return projects;
    }

    public static List<String> prepareMemberVISAs(int size) {
        if (size <= 0) {
            return new ArrayList<>();
        }
        return prepareEmployeesData(size).stream().map(Employee::getVisa).collect(Collectors.toList());
    }

    public static PimProjectQueryRequest preparePimProjectQueryRequest(int indexPage, String searchContent, String status) {
        return PimProjectQueryRequest.newBuilder()
                .setTransactionID(UUID.randomUUID().toString())
                .setStatus(status)
                .setSearchContent(searchContent)
                .setPage(indexPage)
                .build();
    }

    public static PimProjectCountingRequest preparePimProjectCountingRequest(String searchContent, String status) {
        return PimProjectCountingRequest.newBuilder()
                .setTransactionID(UUID.randomUUID().toString())
                .setStatus(status)
                .setSearchContent(searchContent)
                .build();
    }

    public static PimProjectDeleteRequest preparePimProjectDeleteRequest(List<Integer> projectNums) {
        return PimProjectDeleteRequest.newBuilder()
                .setTransactionID(UUID.randomUUID().toString())
                .addAllProjectNumbers(projectNums)
                .build();
    }
}
