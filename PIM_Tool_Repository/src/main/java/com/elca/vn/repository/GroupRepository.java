package com.elca.vn.repository;

import com.elca.vn.entity.Group;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repository class for {@link com.elca.vn.entity.Group}
 */
@Repository
public interface GroupRepository extends CrudRepository<Group, Long> {

    @Query("SELECT g.id, g.groupName FROM Group g")
    List<Object[]> getAllGroups();
}
