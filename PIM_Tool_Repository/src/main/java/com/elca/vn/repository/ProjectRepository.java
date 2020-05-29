package com.elca.vn.repository;

import com.elca.vn.entity.Project;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Repository class for {@link Project}
 */
@Repository
public interface ProjectRepository extends CrudRepository<Project, Long> {

    Project findByProjectNumber(int projectNumber);

    @Query("SELECT p FROM Project p " +
            "WHERE p.name LIKE %:projectNamOrCustomer% " +
            "OR p.customer LIKE %:projectNamOrCustomer% " +
            "AND p.status = :status")
    Page<Project> findByProjectWithSearchContentAndStatus(@Param("projectNamOrCustomer") String projectNamOrCustomer,
                                                          @Param("status") String status,
                                                          Pageable pageable);

    @Query("SELECT p FROM Project p " +
            "WHERE p.projectNumber = :projectNum " +
            "AND p.status = :status")
    Page<Project> findByProjectWithProjectNumAndStatus(@Param("projectNum") int projectNum,
                                                       @Param("status") String status,
                                                       Pageable pageable);

    @Query("SELECT p FROM Project p")
    Page<Project> findAllWithPaging(Pageable pageable);

    @Query("SELECT COUNT(p.projectID) FROM Project p")
    long countAllProjects();

    @Query("SELECT COUNT(p.projectID) FROM Project p " +
            "WHERE p.name LIKE %:projectNamOrCustomer% " +
            "OR p.customer LIKE %:projectNamOrCustomer% " +
            "AND p.status = :status")
    long countProjectsWithSearchContentAndStatus(@Param("projectNamOrCustomer") String projectNamOrCustomer,
                                                 @Param("status") String status);

    @Query("SELECT COUNT(p.projectID) FROM Project p " +
            "WHERE p.projectNumber = :projectNum " +
            "AND p.status = :status")
    long countProjectsWithProjectNumAndStatus(@Param("projectNum") int projectNum,
                                              @Param("status") String status);
}
