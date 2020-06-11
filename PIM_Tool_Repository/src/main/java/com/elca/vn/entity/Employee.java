package com.elca.vn.entity;

import javax.persistence.*;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "PIM_EMPLOYEE")
public class Employee extends BaseEntity {

    @Column(name = "VISA", nullable = false, length = 3)
    private String visa;

    @Column(name = "FIRST_NAME", nullable = false, length = 50)
    private String firstName;

    @Column(name = "LAST_NAME", nullable = false, length = 50)
    private String lastName;

    @Column(name = "BIRTH_DATE", nullable = false)
    private Date birthDate;

    @ManyToMany(mappedBy = "employees", fetch = FetchType.EAGER)
    private Set<Project> projects = new HashSet<>();

    @OneToOne
    @JoinColumn(name = "GROUP_LEADER_ID")
    private Group emp_group;

    public String getVisa() {
        return visa;
    }

    public void setVisa(String visa) {
        this.visa = visa;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public Date getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(Date birthDate) {
        this.birthDate = birthDate;
    }

    public Set<Project> getProjects() {
        return projects;
    }

    public void setProjects(Set<Project> projects) {
        this.projects = projects;
    }

    public Group getEmp_group() {
        return emp_group;
    }

    public void setEmp_group(Group emp_group) {
        this.emp_group = emp_group;
    }
}
