package com.manywho.services.jira.releaseVersions;

import com.atlassian.jira.rest.client.api.domain.BasicProject;
import com.atlassian.jira.rest.client.api.domain.Version;
import com.manywho.sdk.api.ComparisonType;
import com.manywho.sdk.api.CriteriaType;
import com.manywho.sdk.api.run.elements.type.ListFilter;
import com.manywho.sdk.api.run.elements.type.ListFilterWhere;
import com.manywho.sdk.services.database.Database;
import com.manywho.sdk.services.utils.Streams;
import com.manywho.services.jira.ApplicationConfiguration;
import com.manywho.services.jira.jira.JiraClientFactory;
import com.manywho.services.jira.types.ReleaseVersion;

import javax.inject.Inject;
import java.util.List;
import java.util.stream.Collectors;

public class ReleaseVersionDatabase implements Database<ApplicationConfiguration, ReleaseVersion> {
    private final JiraClientFactory clientFactory;

    @Inject
    public ReleaseVersionDatabase(JiraClientFactory clientFactory) {
        this.clientFactory = clientFactory;
    }

    @Override
    public ReleaseVersion create(ApplicationConfiguration configuration, ReleaseVersion releaseVersion) {
        return null;
    }

    @Override
    public List<ReleaseVersion> create(ApplicationConfiguration configuration, List<ReleaseVersion> list) {
        return null;
    }

    @Override
    public void delete(ApplicationConfiguration configuration, ReleaseVersion releaseVersion) {

    }

    @Override
    public void delete(ApplicationConfiguration configuration, List<ReleaseVersion> list) {

    }

    @Override
    public ReleaseVersion update(ApplicationConfiguration configuration, ReleaseVersion releaseVersion) {
        return null;
    }

    @Override
    public List<ReleaseVersion> update(ApplicationConfiguration configuration, List<ReleaseVersion> list) {
        return null;
    }

    @Override
    public ReleaseVersion find(ApplicationConfiguration configuration, String s) {
        return null;
    }

    @Override
    public List<ReleaseVersion> findAll(ApplicationConfiguration configuration, ListFilter listFilter) {
        // Get all projects
        Iterable<BasicProject> allProjects =
                clientFactory.create(configuration)
                        .getProjectClient()
                        .getAllProjects()
                        .claim();

        // For each project get all the release versions
        Iterable<Iterable<ReleaseVersion>> allProjectVersions = Streams.asStream(allProjects).map(project ->
                Streams.asStream((clientFactory.create(configuration)
                        .getProjectClient()
                        .getProject(project.getSelf())
                        .claim())
                        .getVersions()
                )
                .filter(releaseVersion -> filterVersion(releaseVersion, project, listFilter))
                .map(releaseVersion -> new ReleaseVersion(
                        releaseVersion.getId(),
                        releaseVersion.getName(),
                        releaseVersion.getDescription(),
                        project.getName(),
                        project.getKey(),
                        releaseVersion.isReleased(),
                        releaseVersion.isArchived()
                ))
                .collect(Collectors.toList())
        )
        .collect(Collectors.toList());

        // Return all release versions of all projects
        return Streams.asStream(allProjectVersions)
                .flatMap(projectVersions -> Streams.asStream(projectVersions))
                .collect(Collectors.toList());
    }

    private boolean filterVersion(Version releaseVersion, BasicProject project, ListFilter listFilter) {
        if (!listFilter.hasWhere())
            return true;
        if (listFilter.getComparisonType().equals(ComparisonType.Or)) {
            return listFilter.getWhere()
                    .stream()
                    .anyMatch(where -> doesReleaseMatchCriteria(where, releaseVersion, project));
        }
        if (listFilter.getComparisonType().equals(ComparisonType.And)) {
            return listFilter.getWhere()
                    .stream()
                    .allMatch(where -> doesReleaseMatchCriteria(where, releaseVersion, project));
        }
        throw new UnsupportedOperationException("Filtering with the comparison type " + listFilter.getComparisonType() + " is not yet supported");
    }

    private boolean doesReleaseMatchCriteria(ListFilterWhere where, Version releaseVersion, BasicProject project){
        if (!(where.getCriteriaType().equals(CriteriaType.Equal) || where.getCriteriaType().equals(CriteriaType.NotEqual)))
            throw new UnsupportedOperationException("Filtering on the criteria " + where.getCriteriaType() + " is not yet supported");
        if (where.getColumnName().equals("Released")) {
            // If we're using the Equal operator, and we find a match, return true
            // If we're using the NotEqual operator, and we find a fail, return true
            return where.getCriteriaType().equals(CriteriaType.Equal) == (Boolean.parseBoolean(where.getContentValue().toLowerCase()) == releaseVersion.isReleased());
        }
        if (where.getColumnName().equals("Project Key")){
            // If we're using the Equal operator, and we find a match, return true
            // If we're using the NotEqual operator, and we find a fail, return true
            return where.getCriteriaType().equals(CriteriaType.Equal) == (where.getContentValue().equals(project.getKey()));
        }
        //An unsupported column was given
        throw new UnsupportedOperationException("Filtering on the property " + where.getColumnName() + " is not yet supported");
    }
}
