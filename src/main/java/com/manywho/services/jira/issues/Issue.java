package com.manywho.services.jira.issues;

import com.manywho.sdk.api.ContentType;
import com.manywho.sdk.services.types.Type;
import com.manywho.services.jira.types.IssueType;
import com.manywho.services.jira.types.Label;
import com.manywho.services.jira.types.Project;
import com.manywho.services.jira.types.Status;

import java.util.List;

@Type.Element(name = "Issue")
public class Issue implements Type {

    @Type.Identifier
    private String id;

    @Type.Property(name = "Key", contentType = ContentType.String)
    private String key;

    @Type.Property(name = "Summary", contentType = ContentType.String)
    private String summary;

    @Type.Property(name = "Project", contentType = ContentType.Object)
    private Project project;

    @Type.Property(name = "Issue Type", contentType = ContentType.Object)
    private IssueType issueType;

    @Type.Property(name = "Status", contentType = ContentType.Object)
    private Status status;

    @Type.Property(name = "Labels", contentType = ContentType.List)
    private List<Label> labels;

    public Issue() {
    }

    public Issue(Long id, String key, String summary, IssueType issueType, Status status) {
        this.id = String.valueOf(id);
        this.key = key;
        this.summary = summary;
        this.issueType = issueType;
        this.status = status;
    }

    public String getId() {
        return id;
    }

    public String getKey() {
        return key;
    }

    public String getSummary() {
        return summary;
    }

    public Project getProject() {
        return project;
    }

    public IssueType getIssueType() {
        return issueType;
    }

    public Status getStatus() {
        return status;
    }

    public List<Label> getLabels() {
        return labels;
    }
}
