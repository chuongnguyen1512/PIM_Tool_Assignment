package com.elca.vn.service;

import com.elca.vn.entity.Group;
import com.elca.vn.repository.GroupRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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
    public Group updateData(Group project) {
        return null;
    }

    @Override
    public Group queryData(String id) {
        return null;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Group> getData() {
        List<Object[]> data = groupRepository.getAllGroups();
        if (CollectionUtils.isEmpty(data)) {
            return new ArrayList<>();
        }
        return data.stream().map(x -> {
            Group group = new Group();
            group.setId((Long) x[0]);
            group.setGroupName((String) x[1]);
            return group;
        }).collect(Collectors.toList());
    }

    @Override
    public Iterable<Group> getData(List<String> ids) {
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
        return new ArrayList<>();
    }

    @Override
    public List<Group> findAllDataWithPaging(int indexPage) {
        return new ArrayList<>();
    }

    @Override
    public int deleteData(List<Integer> deleteIDs) {
        return 0;
    }
}
