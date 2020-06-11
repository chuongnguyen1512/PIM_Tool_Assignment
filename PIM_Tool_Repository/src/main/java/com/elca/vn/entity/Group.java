package com.elca.vn.entity;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "PIM_GROUP")
public class Group extends BaseEntity {

    @Column(name = "GROUP_NAME", nullable = false, length = 50, unique = true)
    private String groupName;

    @OneToMany(mappedBy = "group", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private Set<Project> projects = new HashSet<>();

    @OneToOne(mappedBy = "emp_group", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private Employee employee;


    public Set<Project> getProjects() {
        return projects;
    }

    public void setProjects(Set<Project> projects) {
        this.projects = projects;
    }

    public Employee getEmployee() {
        return employee;
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }
}
