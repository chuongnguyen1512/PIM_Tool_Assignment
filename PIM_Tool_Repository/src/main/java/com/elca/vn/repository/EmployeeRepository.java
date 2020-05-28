package com.elca.vn.repository;

import com.elca.vn.entity.Employee;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

/**
 * Repository class for {@link com.elca.vn.entity.Employee}
 */
@Repository
public interface EmployeeRepository extends CrudRepository<Employee, Long> {

    @Query("SELECT e FROM Employee e WHERE e.visa in :visas")
    public Set<Employee> findEmployeeByVisas(@Param("visas") List<String> visas);
}
