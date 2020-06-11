package com.elca.vn.entity;

import javax.persistence.*;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "PIM_PROJECT",
        indexes = {
                @Index(name = "INDEX_PROJECT_NUM", columnList = "PROJECT_NUMBER", unique = true),
                @Index(name = "INDEX_COMBINED_PROJECT", columnList = "PROJECT_NUMBER, STATUS", unique = true)
        })
public class Project extends BaseEntity {

    @Column(name = "PROJECT_NUMBER", nullable = false, length = 4, unique = true)
    private int projectNumber;

    @Column(name = "NAME", nullable = false, length = 50)
    private String name;

    @Column(name = "CUSTOMER", nullable = false, length = 50)
    private String customer;

    @Column(name = "STATUS", nullable = false, length = 3)
    private String status;

    @Column(name = "START_DATE", nullable = false)
    private Date startDate;

    @Column(name = "END_DATE")
    private Date endDate;

    @ManyToOne
    @JoinColumn(name = "GROUP_ID")
    private Group group;

    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinTable(name = "PROJECT_EMPLOYEE", joinColumns = {@JoinColumn(name = "PROJECT_ID")}, inverseJoinColumns = {@JoinColumn(name = "EMPLOYEE_ID")})
    private Set<Employee> employees = new HashSet<>();

    public int getProjectNumber() {
        return projectNumber;
    }

    public void setProjectNumber(int projectNumber) {
        this.projectNumber = projectNumber;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCustomer() {
        return customer;
    }

    public void setCustomer(String customer) {
        this.customer = customer;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public Group getGroup() {
        return group;
    }

    public void setGroup(Group group) {
        this.group = group;
    }

    public Set<Employee> getEmployees() {
        return employees;
    }

    public void setEmployees(Set<Employee> employees) {
        this.employees = employees;
    }
}
