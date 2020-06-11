package com.elca.vn.repository;

import com.elca.vn.entity.Project;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Repository class for {@link Project}
 */
@Repository
public interface ProjectRepository extends CrudRepository<Project, Long> {

    Project findByProjectNumber(int projectNumber);

    @Query(value = "SELECT * FROM PIM_PROJECT p " +
            "WHERE ( CAST(p.PROJECT_NUMBER as VARCHAR) LIKE CONCAT('%', ?1, '%') " +
            "OR UPPER(p.NAME) LIKE UPPER(CONCAT('%', ?1, '%')) " +
            "OR UPPER(p.CUSTOMER) LIKE UPPER(CONCAT('%', ?1, '%')) ) " +
            "AND p.STATUS = ?2", nativeQuery = true)
    Page<Project> findByProjectWithSearchContentAndStatus(String projectNamOrCustomer,
                                                          String status,
                                                          Pageable pageable);

    @Query("SELECT p FROM Project p " +
            "WHERE p.projectNumber = :projectNum " +
            "AND p.status = :status")
    Page<Project> findByProjectWithProjectNumAndStatus(@Param("projectNum") int projectNum,
                                                       @Param("status") String status,
                                                       Pageable pageable);

    @Query("SELECT p FROM Project p")
    Page<Project> findAllWithPaging(Pageable pageable);

    @Query("SELECT COUNT(p.id) FROM Project p")
    long countAllProjects();

    @Query(value = "SELECT COUNT(ID) FROM PIM_PROJECT p " +
            "WHERE ( CAST(p.PROJECT_NUMBER as VARCHAR) LIKE CONCAT('%', ?1, '%') " +
            "OR UPPER(p.NAME) LIKE UPPER(CONCAT('%', ?1, '%')) " +
            "OR UPPER(p.CUSTOMER) LIKE UPPER(CONCAT('%', ?1, '%')) ) " +
            "AND p.STATUS = ?2", nativeQuery = true)
    long countProjectsWithSearchContentAndStatus(String projectNamOrCustomer,
                                                 String status);

    @Query("SELECT COUNT(p.id) FROM Project p " +
            "WHERE p.projectNumber = :projectNum " +
            "AND p.status = :status")
    long countProjectsWithProjectNumAndStatus(@Param("projectNum") int projectNum,
                                              @Param("status") String status);

    @Transactional
    @Modifying
    @Query("DELETE FROM Project p WHERE p.projectNumber IN :projectNums")
    int deleteByProjectNumbers(@Param("projectNums") List<Integer> projectNums);
}
