package com.manywho.services.jira.issues;

import com.atlassian.jira.rest.client.api.domain.SearchResult;
import com.manywho.sdk.api.run.elements.type.ListFilter;
import com.manywho.sdk.services.database.Database;
import com.manywho.sdk.services.utils.Streams;
import com.manywho.services.jira.ServiceConfiguration;
import com.manywho.services.jira.factories.JiraClientFactory;
import com.manywho.services.jira.types.IssueType;
import com.manywho.services.jira.types.Status;
import com.softwareleaf.jira.jql.JQL;

import javax.inject.Inject;
import java.util.List;
import java.util.stream.Collectors;

public class IssueDatabase implements Database<ServiceConfiguration, Issue> {
    private final JiraClientFactory clientFactory;

    @Inject
    public IssueDatabase(JiraClientFactory clientFactory) {
        this.clientFactory = clientFactory;
    }

    public Issue create(ServiceConfiguration configuration, Issue issue) {
        return null;
    }

    public List<Issue> create(ServiceConfiguration configuration, List<Issue> list) {
        return null;
    }

    public void delete(ServiceConfiguration configuration, Issue issue) {

    }

    public void delete(ServiceConfiguration configuration, List<Issue> list) {

    }

    public Issue update(ServiceConfiguration configuration, Issue issue) {
        return null;
    }

    public List<Issue> update(ServiceConfiguration configuration, List<Issue> list) {
        return null;
    }

    public Issue find(ServiceConfiguration configuration, String s) {
        return null;
    }

    public List<Issue> findAll(ServiceConfiguration configuration, ListFilter listFilter) {
        JQL jql = IssueSearch.createJqlFromListFilter(listFilter);

        SearchResult result = clientFactory.create(configuration)
                .getSearchClient()
                .searchJql(jql.build(), listFilter.getLimit(), listFilter.getOffset(), null)
                .claim();

        return Streams.asStream(result.getIssues())
                .map(issue -> new Issue(
                        issue.getId(),
                        issue.getKey(),
                        issue.getSummary(),
                        new IssueType(issue.getIssueType().getId(), issue.getIssueType().getName()),
                        new Status(issue.getStatus().getId(), issue.getStatus().getName())
                ))
                .collect(Collectors.toList());
    }
}
