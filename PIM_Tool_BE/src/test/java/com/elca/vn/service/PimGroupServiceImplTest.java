package com.elca.vn.service;

import com.elca.vn.MockDataUtils;
import com.elca.vn.entity.Group;
import com.elca.vn.repository.GroupRepository;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

@RunWith(MockitoJUnitRunner.class)
public class PimGroupServiceImplTest {

    private BasePimDataService<Group> groupService;

    @Mock
    private GroupRepository groupRepository;

    @Before
    public void init() {
        groupService = new PimGroupServiceImpl(groupRepository);
    }

    @Test
    public void shouldGetAllGroupDataSuccessfully() {
        Mockito.when(groupRepository.getAllGroups()).thenReturn(MockDataUtils.prepareDBGroups(1));
        List<Group> resultGroup = (List<Group>) groupService.getData();

        Assert.assertFalse(CollectionUtils.isEmpty(resultGroup));
        Assert.assertEquals(1, resultGroup.size());
    }

    @Test
    public void shouldGetAllGroupDataSuccessfullyWithEmptyDBData() {
        Mockito.when(groupRepository.getAllGroups()).thenReturn(new ArrayList<>());
        List<Group> resultGroup = (List<Group>) groupService.getData();

        Assert.assertTrue(CollectionUtils.isEmpty(resultGroup));
    }

    @Test(expected = Exception.class)
    public void shouldThrowExceptionIfHavingErrorWhenGettingAllGroupData() {
        Mockito.when(groupRepository.getAllGroups()).thenThrow(new RuntimeException("Test exception when getting all groups data"));
        groupService.getData();
    }
}
