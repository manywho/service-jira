package com.manywho.services.jira.types;

import com.manywho.sdk.api.ContentType;
import com.manywho.sdk.services.types.Type;

@Type.Element(name = "Project")
public class Project implements Type {

    @Type.Identifier
    private String id;

    @Type.Property(name = "Key", contentType = ContentType.String)
    private String key;

    @Type.Property(name = "Name", contentType = ContentType.String)
    private String name;

    public Project() {
    }

    public Project(Long id, String key, String name) {
        this.id = String.valueOf(id);
        this.key = key;
        this.name = name;
    }

    public String getKey() {
        return key;
    }

    public String getName() {
        return name;
    }
}
