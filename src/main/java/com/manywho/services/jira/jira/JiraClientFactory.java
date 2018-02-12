package com.manywho.services.jira.jira;

import com.atlassian.httpclient.api.Request;
import com.atlassian.jira.rest.client.api.JiraRestClient;
import com.atlassian.jira.rest.client.internal.async.AsynchronousJiraRestClientFactory;
import com.github.scribejava.core.model.OAuth1AccessToken;
import com.github.scribejava.core.model.OAuthRequest;
import com.github.scribejava.core.model.Verb;
import com.manywho.services.jira.ApplicationConfiguration;

import java.net.URI;
import java.util.Map;

public class JiraClientFactory {
    public JiraRestClient create(ApplicationConfiguration configuration) {
        return new AsynchronousJiraRestClientFactory()
                .createWithBasicHttpAuthentication(URI.create(configuration.getUrl()), configuration.getUsername(), configuration.getPassword());
    }

    public JiraRestClient create(ApplicationConfiguration configuration, String token) {
        String[] splitToken = token.split(":::");

        return new AsynchronousJiraRestClientFactory()
                .createWithAuthenticationHandler(URI.create(configuration.getUrl()), builder -> {
                    Request request = builder.build();

                    // We need to create a "fake" request here, as an easy way of getting a generated OAuth signature
                    OAuthRequest oAuthRequest = new OAuthRequest(Verb.valueOf(request.getMethod().toString()), request.getUri().toString());
                    oAuthRequest.setPayload(request.getEntity());

                    JiraApi10aFactory.create(configuration)
                            .signRequest(new OAuth1AccessToken(splitToken[0], splitToken[1]), oAuthRequest);

                    // Copy the generated signature to the real request object
                    Map<String, String> headers = oAuthRequest.getHeaders();
                    if (headers.containsKey("Authorization") == false) {
                        throw new RuntimeException("No Authorization header was created for the OAuth 1.0a request");
                    }

                    builder.setHeader("Authorization", headers.get("Authorization"));
                });
    }
}
