package com.manywho.services.jira.projects;

import com.atlassian.jira.rest.client.api.domain.BasicProject;
import com.manywho.sdk.api.run.elements.type.ListFilter;
import com.manywho.sdk.services.database.Database;
import com.manywho.sdk.services.utils.Streams;
import com.manywho.services.jira.ServiceConfiguration;
import com.manywho.services.jira.factories.JiraClientFactory;
import com.manywho.services.jira.types.Project;

import javax.inject.Inject;
import java.util.List;
import java.util.stream.Collectors;

public class ProjectDatabase implements Database<ServiceConfiguration, Project> {
    private final JiraClientFactory clientFactory;

    @Inject
    public ProjectDatabase(JiraClientFactory clientFactory) {
        this.clientFactory = clientFactory;
    }

    @Override
    public Project create(ServiceConfiguration configuration, Project project) {
        return null;
    }

    @Override
    public List<Project> create(ServiceConfiguration configuration, List<Project> list) {
        return null;
    }

    @Override
    public void delete(ServiceConfiguration configuration, Project project) {

    }

    @Override
    public void delete(ServiceConfiguration configuration, List<Project> list) {

    }

    @Override
    public Project update(ServiceConfiguration configuration, Project project) {
        return null;
    }

    @Override
    public List<Project> update(ServiceConfiguration configuration, List<Project> list) {
        return null;
    }

    @Override
    public Project find(ServiceConfiguration configuration, String s) {
        return null;
    }

    @Override
    public List<Project> findAll(ServiceConfiguration configuration, ListFilter listFilter) {
        Iterable<BasicProject> projects = clientFactory.create(configuration)
                .getProjectClient()
                .getAllProjects()
                .claim();

        return Streams.asStream(projects)
                .map(project -> new Project(project.getId(), project.getKey(), project.getName()))
                .collect(Collectors.toList());
    }
}
