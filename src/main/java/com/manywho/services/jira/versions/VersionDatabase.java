package com.manywho.services.jira.versions;

import com.atlassian.jira.rest.client.api.ProjectRestClient;
import com.atlassian.jira.rest.client.api.domain.BasicProject;
import com.manywho.sdk.api.ComparisonType;
import com.manywho.sdk.api.CriteriaType;
import com.manywho.sdk.api.run.elements.type.ListFilter;
import com.manywho.sdk.api.run.elements.type.ListFilterWhere;
import com.manywho.sdk.services.database.ReadOnlyDatabase;
import com.manywho.sdk.services.utils.Streams;
import com.manywho.services.jira.ApplicationConfiguration;
import com.manywho.services.jira.jira.JiraClientFactory;
import com.manywho.services.jira.types.Version;

import javax.inject.Inject;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class VersionDatabase implements ReadOnlyDatabase<ApplicationConfiguration, Version> {
    private final JiraClientFactory clientFactory;

    @Inject
    public VersionDatabase(JiraClientFactory clientFactory) {
        this.clientFactory = clientFactory;
    }

    @Override
    public Version find(ApplicationConfiguration configuration, String s) {
        return null;
    }

    @Override
    public List<Version> findAll(ApplicationConfiguration configuration, ListFilter listFilter) {
        // Get the project client
        ProjectRestClient projectClient =
                clientFactory.create(configuration)
                        .getProjectClient();

        // Get all projects
        Iterable<BasicProject> allProjects =
                projectClient
                        .getAllProjects()
                        .claim();

        // For each project, filter them by the list filter, then get all the versions of those projects, then filter the versions by the list filter
        Iterable<Iterable<Version>> allAcceptableVersions = Streams.asStream(allProjects).filter(project -> filterProject(project, listFilter))
                .map(project ->
                        Streams.asStream((projectClient
                                .getProject(project.getSelf())
                                .claim())
                                .getVersions()
                        )
                        .filter(version -> filterVersion(version, listFilter))
                        .map(version -> new Version(
                                version.getId(),
                                version.getName(),
                                version.getDescription(),
                                project.getName(),
                                project.getKey(),
                                version.isReleased(),
                                version.isArchived()
                        ))
                        .collect(Collectors.toList())
                )
                .collect(Collectors.toList());

        // Return all acceptable versions of all acceptable projects
        return Streams.asStream(allAcceptableVersions)
                .flatMap(projectVersions -> Streams.asStream(projectVersions))
                .collect(Collectors.toList());
    }

    private boolean filterProject(BasicProject project, ListFilter listFilter){
        if (listFilter.hasWhere() == false) {
            return true;
        }
        Stream<ListFilterWhere> projectCriteria = listFilter.getWhere()
                .stream()
                .filter(where -> where.getColumnName().equals("Project Key"));
        if (listFilter.getComparisonType().equals(ComparisonType.Or)) {
            return projectCriteria.anyMatch(where -> doesProjectMatchCriterion(where, project));
        }
        if (listFilter.getComparisonType().equals(ComparisonType.And)) {
            return projectCriteria.allMatch(where -> doesProjectMatchCriterion(where, project));
        }
        throw new UnsupportedOperationException("Filtering with the comparison type " + listFilter.getComparisonType() + " is not yet supported in the JIRA service");
    }

    private boolean doesProjectMatchCriterion(ListFilterWhere where, BasicProject project){
        if (!(where.getCriteriaType().equals(CriteriaType.Equal) || where.getCriteriaType().equals(CriteriaType.NotEqual))) {
            throw new UnsupportedOperationException("Filtering on the criteria " + where.getCriteriaType() + " is not yet supported in the JIRA service");
        }
        if (where.getCriteriaType().equals(CriteriaType.Equal)){
            return where.getCriteriaType().equals(CriteriaType.Equal) == (where.getContentValue().equals(project.getKey()));
        }
        else
        {
            return where.getCriteriaType().equals(CriteriaType.Equal) != (where.getContentValue().equals(project.getKey()));
        }
    }

    private boolean filterVersion(com.atlassian.jira.rest.client.api.domain.Version version, ListFilter listFilter) {
        if (listFilter.hasWhere() == false) {
            return true;
        }
        Stream<ListFilterWhere> versionCriteria = listFilter.getWhere()
            .stream()
            .filter(where -> where.getColumnName().equals("Project Key") == false);
        if (listFilter.getComparisonType().equals(ComparisonType.Or)) {
            return versionCriteria.anyMatch(where -> doesVersionMatchCriterion(where, version));
        }
        if (listFilter.getComparisonType().equals(ComparisonType.And)) {
            return versionCriteria.allMatch(where -> doesVersionMatchCriterion(where, version));
        }
        throw new UnsupportedOperationException("Filtering with the comparison type " + listFilter.getComparisonType() + " is not yet supported in the JIRA service");
    }

    private boolean doesVersionMatchCriterion(ListFilterWhere where, com.atlassian.jira.rest.client.api.domain.Version version){
        if (!(where.getCriteriaType().equals(CriteriaType.Equal) || where.getCriteriaType().equals(CriteriaType.NotEqual))) {
            throw new UnsupportedOperationException("Filtering on the criteria " + where.getCriteriaType() + " is not yet supported in the JIRA service");
        }
        if (where.getColumnName().equals("Is Released?")) {
            if (where.getCriteriaType().equals(CriteriaType.Equal)){
                return (Boolean.valueOf(where.getContentValue()) == version.isReleased());
            }
            else
            {
                return (Boolean.valueOf(where.getContentValue()) != version.isReleased());
            }
        }
        throw new UnsupportedOperationException("Filtering on the property " + where.getColumnName() + " is not yet supported in the JIRA service");
    }
}
