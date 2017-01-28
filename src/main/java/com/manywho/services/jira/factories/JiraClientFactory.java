package com.manywho.services.jira.factories;

import com.atlassian.jira.rest.client.api.JiraRestClient;
import com.atlassian.jira.rest.client.internal.async.AsynchronousJiraRestClientFactory;
import com.manywho.services.jira.ApplicationConfiguration;

import java.net.URI;

public class JiraClientFactory {
    public JiraRestClient create(ApplicationConfiguration configuration) {
        return new AsynchronousJiraRestClientFactory()
                .createWithBasicHttpAuthentication(URI.create(configuration.getUrl()), configuration.getUsername(), configuration.getPassword());
    }
}
