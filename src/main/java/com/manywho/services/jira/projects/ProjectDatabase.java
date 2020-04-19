package com.manywho.services.jira.projects;

import com.atlassian.jira.rest.client.api.domain.BasicProject;
import com.manywho.sdk.api.draw.content.Command;
import com.manywho.sdk.api.run.elements.type.ListFilter;
import com.manywho.sdk.api.run.elements.type.MObject;
import com.manywho.sdk.api.run.elements.type.ObjectDataType;
import com.manywho.sdk.services.database.ReadOnlyDatabase;
import com.manywho.sdk.services.utils.Streams;
import com.manywho.services.jira.ApplicationConfiguration;
import com.manywho.services.jira.jira.JiraClientFactory;
import com.manywho.services.jira.types.Project;

import javax.inject.Inject;
import java.util.List;
import java.util.stream.Collectors;

public class ProjectDatabase implements ReadOnlyDatabase<ApplicationConfiguration, Project> {
    private final JiraClientFactory clientFactory;

    @Inject
    public ProjectDatabase(JiraClientFactory clientFactory) {
        this.clientFactory = clientFactory;
    }

    @Override
    public Project find(ApplicationConfiguration configuration, ObjectDataType objectDataType, Command command, String id) {
        return null;
    }

    @Override
    public List<Project> findAll(ApplicationConfiguration configuration, ObjectDataType objectDataType, Command command, ListFilter filter, List<MObject> objects) {
        Iterable<BasicProject> projects = clientFactory.create(configuration)
                .getProjectClient()
                .getAllProjects()
                .claim();

        return Streams.asStream(projects)
                .map(project -> new Project(project.getId(), project.getKey(), project.getName()))
                .collect(Collectors.toList());
    }
}
