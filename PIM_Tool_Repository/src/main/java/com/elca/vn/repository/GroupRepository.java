package com.elca.vn.repository;

import com.elca.vn.entity.Group;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.stream.Stream;

/**
 * Repository class for {@link com.elca.vn.entity.Group}
 */
@Repository
public interface GroupRepository extends CrudRepository<Group, Long> {

    @Query("SELECT g.groupID, g.groupName FROM Group g")
    Stream<Object[]> getAllGroups();
}
