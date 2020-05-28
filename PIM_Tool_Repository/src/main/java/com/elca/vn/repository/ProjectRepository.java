package com.elca.vn.repository;

import com.elca.vn.entity.Project;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository class for {@link Project}
 */
@Repository
public interface ProjectRepository extends CrudRepository<Project, Long> {

    Project findByProjectNumber(int projectNumber);
}
