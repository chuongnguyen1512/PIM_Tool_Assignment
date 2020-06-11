package com.elca.vn.transform;

import com.elca.vn.MockDataUtils;
import com.elca.vn.entity.Group;
import com.elca.vn.proto.model.PimGroup;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class GroupTransformServiceImplTest {

    private GroupTransformServiceImpl groupTransformService;

    @Before
    public void init() {
        groupTransformService = new GroupTransformServiceImpl();
    }

    @Test
    public void shouldTransformFromSourceToDesSuccessfully() {
        PimGroup pimGroup = MockDataUtils.preparePimGroup(1).get(0);
        Group group = groupTransformService.transformFromSourceToDes(pimGroup);
        Assert.assertNotNull(group);
        Assert.assertEquals(pimGroup.getGroupID(), group.getId());
        Assert.assertEquals(pimGroup.getGroupName(), group.getGroupName());
    }

    @Test
    public void shouldTransformFromDesToSourceSuccessfully() {
        Group group = MockDataUtils.prepareGroups(1).get(0);
        PimGroup pimGroup = groupTransformService.transformFromDesToSource(group);
        Assert.assertNotNull(pimGroup);
        Assert.assertEquals(group.getId(), pimGroup.getGroupID());
        Assert.assertEquals(group.getGroupName(), pimGroup.getGroupName());
    }
}
