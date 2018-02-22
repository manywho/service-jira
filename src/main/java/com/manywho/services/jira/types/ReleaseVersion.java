package com.manywho.services.jira.types;

import com.manywho.sdk.api.ContentType;
import com.manywho.sdk.services.types.Type;
import org.joda.time.DateTime;

@Type.Element(name = "Release Version")
public class ReleaseVersion implements Type {

    @Type.Identifier
    private String id;

    @Type.Property(name = "Name", contentType = ContentType.String)
    private String name;

    @Type.Property(name = "Description", contentType = ContentType.String)
    private String description;

    @Type.Property(name = "Project Name", contentType = ContentType.String)
    private String projectName;

    @Type.Property(name = "Project Key", contentType = ContentType.String)
    private String projectKey;

    @Type.Property(name = "Released", contentType = ContentType.Boolean)
    private Boolean isReleased;

    @Type.Property(name = "Archived", contentType = ContentType.Boolean)
    private Boolean isArchived;

    public ReleaseVersion() {
    }

    public ReleaseVersion(Long id, String name, String description, String projectName, String projectKey, Boolean isReleased, Boolean isArchived) {
        this.id = String.valueOf(id);
        this.name = name;
        this.description = description;
        this.projectName = projectName;
        this.projectKey = projectKey;
        this.isReleased = isReleased;
        this.isArchived = isArchived;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getProjectName() {
        return projectName;
    }

    public String getProjectKey() {
        return projectKey;
    }

    public Boolean getIsReleased(){
        return isReleased;
    }

    public Boolean getIsArchived(){
        return isArchived;
    }
}
