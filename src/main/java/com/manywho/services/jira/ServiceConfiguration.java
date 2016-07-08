package com.manywho.services.jira;

import com.manywho.sdk.api.ContentType;
import com.manywho.sdk.services.configuration.Configuration;

public class ServiceConfiguration implements Configuration {

    @Configuration.Value(name = "URL", contentType = ContentType.String)
    private String url;

    @Configuration.Value(name = "Username", contentType = ContentType.String)
    private String username;

    @Configuration.Value(name = "Password", contentType = ContentType.Password)
    private String password;

    public String getUrl() {
        return url;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }
}
