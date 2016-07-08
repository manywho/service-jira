package com.manywho.services.jira.types;

import com.manywho.sdk.api.ContentType;
import com.manywho.sdk.services.types.Type;

@Type.Element(name = "Label")
public class Label implements Type {

    @Type.Identifier
    @Type.Property(name = "Name", contentType = ContentType.String)
    private String name;

    public Label() {
    }

    public Label(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
