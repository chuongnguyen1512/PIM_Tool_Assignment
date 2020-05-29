package com.elca.vn.service;

import com.elca.vn.entity.Group;
import com.elca.vn.entity.Project;
import com.elca.vn.repository.GroupRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Iterator;
import java.util.List;
import java.util.Objects;

/**
 * Service class for processing {@link Group} data
 */
@Service
public class PimGroupServiceImpl implements BasePimDataService<Group> {

    private GroupRepository groupRepository;

    @Autowired
    public PimGroupServiceImpl(GroupRepository groupRepository) {
        this.groupRepository = groupRepository;
    }

    @Override
    public Group importData(Group project) {
        return null;
    }

    @Override
    public Group queryData(String id) {
        return null;
    }

    @Override
    @Transactional(readOnly = true)
    public Iterator<Group> getData() {
        Iterable<Group> data = groupRepository.findAll();
        if (Objects.nonNull(data)) {
            return data.iterator();
        }
        return null;
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
    public List<Group> findDataWithPaging(int indexPage, String... contentSearch) {
        return null;
    }

    @Override
    public List<Group> findAllDataWithPaging(int indexPage) {
        return null;
    }
}
