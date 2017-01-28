package com.manywho.services.jira.issues;

import com.manywho.sdk.api.run.elements.type.ListFilter;
import com.manywho.sdk.services.database.Database;
import com.manywho.services.jira.ApplicationConfiguration;

import javax.inject.Inject;
import java.util.List;

public class IssueDatabase implements Database<ApplicationConfiguration, Issue> {
    private final IssueManager issueManager;

    @Inject
    public IssueDatabase(IssueManager issueManager) {
        this.issueManager = issueManager;
    }

    public Issue create(ApplicationConfiguration configuration, Issue issue) {
        return null;
    }

    public List<Issue> create(ApplicationConfiguration configuration, List<Issue> list) {
        return null;
    }

    public void delete(ApplicationConfiguration configuration, Issue issue) {

    }

    public void delete(ApplicationConfiguration configuration, List<Issue> list) {

    }

    public Issue update(ApplicationConfiguration configuration, Issue issue) {
        return null;
    }

    public List<Issue> update(ApplicationConfiguration configuration, List<Issue> list) {
        return null;
    }

    public Issue find(ApplicationConfiguration configuration, String s) {
        return null;
    }

    public List<Issue> findAll(ApplicationConfiguration configuration, ListFilter listFilter) {
        return issueManager.findAll(configuration, listFilter);
    }
}
