package com.manywho.services.jira.issues;

import com.manywho.sdk.api.draw.content.Command;
import com.manywho.sdk.api.run.elements.type.ListFilter;
import com.manywho.sdk.api.run.elements.type.MObject;
import com.manywho.sdk.api.run.elements.type.ObjectDataType;
import com.manywho.sdk.services.database.ReadOnlyDatabase;
import com.manywho.services.jira.ApplicationConfiguration;

import javax.inject.Inject;
import java.util.List;

public class IssueDatabase implements ReadOnlyDatabase<ApplicationConfiguration, Issue> {
    private final IssueManager issueManager;

    @Inject
    public IssueDatabase(IssueManager issueManager) {
        this.issueManager = issueManager;
    }

    @Override
    public Issue find(ApplicationConfiguration configuration, ObjectDataType objectDataType, Command command, String id) {
        return null;
    }

    @Override
    public List<Issue> findAll(ApplicationConfiguration configuration, ObjectDataType objectDataType, Command command, ListFilter filter, List<MObject> objects) {
        return issueManager.findAll(configuration, filter);
    }
}
