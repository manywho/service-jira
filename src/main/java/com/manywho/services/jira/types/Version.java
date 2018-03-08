package com.manywho.services.jira.types;

import com.manywho.sdk.api.ContentType;
import com.manywho.sdk.services.types.Type;

@Type.Element(name = "Version")
public class Version implements Type {

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

    @Type.Property(name = "Is Released?", contentType = ContentType.Boolean)
    private Boolean released;

    @Type.Property(name = "Is Archived?", contentType = ContentType.Boolean)
    private Boolean archived;

    public Version() {
    }

    public Version(Long id, String name, String description, String projectName, String projectKey, Boolean released, Boolean archived) {
        this.id = String.valueOf(id);
        this.name = name;
        this.description = description;
        this.projectName = projectName;
        this.projectKey = projectKey;
        this.released = released;
        this.archived = archived;
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

    public Boolean isReleased(){
        return released;
    }

    public Boolean isArchived(){
        return archived;
    }
}
