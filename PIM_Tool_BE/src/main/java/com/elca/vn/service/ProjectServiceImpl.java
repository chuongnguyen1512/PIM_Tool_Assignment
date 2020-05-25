package com.elca.vn.service;

import com.elca.vn.entity.Project;
import com.elca.vn.repository.ProjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProjectServiceImpl implements ProjectService {

    private ProjectRepository projectRepository;

    @Autowired
    public ProjectServiceImpl(ProjectRepository projectRepository) {
        this.projectRepository = projectRepository;

    }

    /**
     * @param project
     * @return
     */
    public Project persitProject(Project project) {
        return projectRepository.saveAndFlush(project);
    }
}

