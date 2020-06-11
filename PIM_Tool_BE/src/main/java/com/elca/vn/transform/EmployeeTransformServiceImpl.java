package com.elca.vn.transform;

import com.elca.vn.entity.Employee;
import com.elca.vn.proto.model.PimEmployee;
import org.springframework.stereotype.Service;

import java.util.Objects;

/**
 * Transform service for pair models {@link PimEmployee} and {@link Employee}
 */
@Service
public class EmployeeTransformServiceImpl implements BaseTransformService<PimEmployee, Employee> {

    /**
     * {@inheritDoc}
     *
     * @param sourceObject input object
     * @return
     */
    @Override
    public Employee transformFromSourceToDes(PimEmployee sourceObject) {
        if (Objects.isNull(sourceObject)) {
            return null;
        }
        Employee employee = new Employee();
        employee.setVisa(sourceObject.getVisa());
        employee.setFirstName(sourceObject.getFirstName());
        employee.setLastName(sourceObject.getFirstName());
        return employee;
    }

    /**
     * {@inheritDoc}
     *
     * @param destinationObject input object
     * @return
     */
    @Override
    public PimEmployee transformFromDesToSource(Employee destinationObject) {
        if (Objects.isNull(destinationObject)) {
            return null;
        }
        return PimEmployee.newBuilder()
                .setVisa(destinationObject.getVisa())
                .setFirstName(destinationObject.getFirstName())
                .setLastName(destinationObject.getLastName())
                .build();
    }
}
