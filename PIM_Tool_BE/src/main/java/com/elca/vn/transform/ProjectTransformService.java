package com.elca.vn.transform;

import com.elca.vn.entity.Project;
import com.elca.vn.proto.model.PimProject;
import com.elca.vn.repository.ProjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class ProjectTransformService implements BaseTransformService<PimProject, Project> {

    private ProjectRepository projectRepository;

    @Autowired
    public ProjectTransformService(ProjectRepository projectRepository) {
        this.projectRepository = projectRepository;
    }

    public Project transformFromSourceToDes(PimProject sourceObject) {
        Project project = new Project();
        project.setProjectNumber(sourceObject.getProjectNumber());
        project.setCustomer(sourceObject.getCustomer());
        project.setStartDate(new Date(sourceObject.getStartDate().getSeconds() * 1000));
        project.setEndDate(new Date(sourceObject.getEndDate().getSeconds() * 1000));
        project.setStatus(sourceObject.getStatus().toString());
        project.setName(sourceObject.getProjectName());
        return null;
    }

    public PimProject transformFromDesToSource(Project destinationObject) {
        return null;
    }
}
