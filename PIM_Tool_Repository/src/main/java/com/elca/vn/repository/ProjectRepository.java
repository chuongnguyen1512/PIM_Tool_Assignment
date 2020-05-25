package com.elca.vn.repository;

import com.elca.vn.entity.Project;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository class for {@link Project}
 */
@Repository
public interface ProjectRepository extends JpaRepository<Project, Long> {
}
