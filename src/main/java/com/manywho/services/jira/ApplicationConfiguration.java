package com.manywho.services.jira;

import com.manywho.sdk.api.ContentType;
import com.manywho.sdk.services.configuration.Configuration;

public class ApplicationConfiguration implements Configuration {

    @Configuration.Setting(name = "URL", contentType = ContentType.String)
    private String url;

    @Configuration.Setting(name = "Username", contentType = ContentType.String)
    private String username;

    @Configuration.Setting(name = "Password", contentType = ContentType.Password)
    private String password;

    @Configuration.Setting(name = "Consumer Key", contentType = ContentType.String)
    private String consumerKey;

    @Configuration.Setting(name = "Consumer Secret", contentType = ContentType.Password)
    private String consumerSecret;

    public String getUrl() {
        return url;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getConsumerKey() {
        return consumerKey;
    }

    public String getConsumerSecret() {
        return consumerSecret;
    }
}
