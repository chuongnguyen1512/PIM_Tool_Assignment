package com.elca.vn.transform;

import com.elca.vn.MockDataUtils;
import com.elca.vn.entity.Project;
import com.elca.vn.proto.model.PimProject;
import com.elca.vn.proto.model.ProcessingStatus;
import com.elca.vn.util.PIMToolUtils;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

public class ProjectTransformServiceImplTest {

    private ProjectTransformServiceImpl projectTransformService;

    @Before
    public void init() {
        projectTransformService = new ProjectTransformServiceImpl();
    }

    @Test
    public void shouldTransformFromSourceToDesSuccessfully() {
        PimProject pimProject = MockDataUtils.preparePimProjectsData(1, ProcessingStatus.INSERT, new ArrayList<>()).get(0);
        Project project = projectTransformService.transformFromSourceToDes(pimProject);

        Assert.assertNotNull(project);
        Assert.assertEquals(pimProject.getProjectNumber(), project.getProjectNumber());
        Assert.assertEquals(pimProject.getProjectName(), project.getName());
        Assert.assertEquals(pimProject.getCustomer(), project.getCustomer());
        Assert.assertEquals(pimProject.getStartDate().getSeconds(), project.getStartDate().getTime() / 1000);
        Assert.assertEquals(pimProject.getGroupID(), project.getGroup().getId());
    }


    @Test
    public void shouldTransformFromDesToSourceSuccessfully() {
        Project project = MockDataUtils.prepareProjectsData(1).get(0);
        PimProject pimProject = projectTransformService.transformFromDesToSource(project);

        Assert.assertNotNull(pimProject);
        Assert.assertEquals(project.getProjectNumber(), pimProject.getProjectNumber());
        Assert.assertEquals(project.getName(), pimProject.getProjectName());
        Assert.assertEquals(project.getCustomer(), pimProject.getCustomer());
        Assert.assertEquals(PIMToolUtils.convertToTimestamp(project.getStartDate()).getSeconds(), pimProject.getStartDate().getSeconds());
        Assert.assertEquals(project.getGroup().getId(), pimProject.getGroupID());
    }
}
