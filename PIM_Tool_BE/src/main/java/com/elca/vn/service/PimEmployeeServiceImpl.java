package com.elca.vn.service;

import com.elca.vn.entity.Employee;
import com.elca.vn.repository.EmployeeRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

@Service
public class PimEmployeeServiceImpl implements BasePimDataService<Employee> {

    private static final Logger LOGGER = LoggerFactory.getLogger(PimEmployeeServiceImpl.class);

    private final EmployeeRepository employeeRepository;

    @Autowired
    public PimEmployeeServiceImpl(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    @Override
    public Employee importData(Employee project) {
        return null;
    }

    @Override
    public Employee updateData(Employee project) {
        return null;
    }

    @Override
    public Employee queryData(String id) {
        return null;
    }

    @Override
    public List<Employee> getData() {
        return new ArrayList();
    }

    @Override
    public Set<Employee> getData(List<String> ids) {
        // Not paging, no need to validate indexPage
        if (CollectionUtils.isEmpty(ids)) {
            String errorMsg = "Inputs searching for employee are not valid";
            LOGGER.warn(errorMsg);
            throw new IllegalArgumentException(errorMsg);
        }

        Set<Employee> employees = employeeRepository.findEmployeeByVisas(ids);
        if (CollectionUtils.isEmpty(employees)) {
            LOGGER.warn("No searching employees have been found with visas: {}", ids);
            return new HashSet<>();
        }
        return employees;
    }

    @Override
    public long getTotalDataSize() {
        return 0;
    }

    @Override
    public long getTotalDataSize(String... contentSearch) {
        return 0;
    }

    @Override
    public Set<Employee> findDataWithPaging(int indexPage, String... contentSearch) {
        // Not paging, no need to validate indexPage
        if (Objects.isNull(contentSearch) || contentSearch.length == 0) {
            String errorMsg = "Inputs searching for employee are not valid";
            LOGGER.warn(errorMsg);
            throw new IllegalArgumentException(errorMsg);
        }
        Set<Employee> employees = new HashSet<>();

        if (contentSearch.length == 1) {
            employees = employeeRepository.findEmployeesByVisa(contentSearch[0]);
        }

        if (CollectionUtils.isEmpty(employees)) {
            LOGGER.warn("No searching employees have been found with search content: {}", contentSearch);
            return new HashSet<>();
        }
        return employees;
    }

    @Override
    public List<Employee> findAllDataWithPaging(int indexPage) {
        return new ArrayList<>();
    }

    @Override
    public int deleteData(List<Integer> deleteIDs) {
        return 0;
    }
}
