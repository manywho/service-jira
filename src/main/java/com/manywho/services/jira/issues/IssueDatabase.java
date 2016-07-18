package com.manywho.services.jira.issues;

import com.manywho.sdk.api.run.elements.type.ListFilter;
import com.manywho.sdk.services.database.Database;
import com.manywho.services.jira.ServiceConfiguration;

import javax.inject.Inject;
import java.util.List;

public class IssueDatabase implements Database<ServiceConfiguration, Issue> {
    private final IssueManager issueManager;

    @Inject
    public IssueDatabase(IssueManager issueManager) {
        this.issueManager = issueManager;
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
        return issueManager.findAll(configuration, listFilter);
    }
}
