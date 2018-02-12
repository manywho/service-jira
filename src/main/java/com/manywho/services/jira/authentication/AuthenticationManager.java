package com.manywho.services.jira.authentication;

import com.atlassian.jira.rest.client.api.JiraRestClient;
import com.atlassian.jira.rest.client.api.domain.Session;
import com.atlassian.jira.rest.client.api.domain.User;
import com.github.scribejava.core.model.OAuth1AccessToken;
import com.github.scribejava.core.model.OAuth1RequestToken;
import com.google.common.base.Strings;
import com.manywho.sdk.api.security.AuthenticatedWhoResult;
import com.manywho.sdk.api.security.AuthenticationCredentials;
import com.manywho.sdk.services.configuration.ConfigurationParser;
import com.manywho.services.jira.ApplicationConfiguration;
import com.manywho.services.jira.jira.JiraApi10aFactory;
import com.manywho.services.jira.jira.JiraClientFactory;

import javax.inject.Inject;
import java.io.IOException;
import java.util.concurrent.ExecutionException;

public class AuthenticationManager {
    private final AuthenticationRepository repository;
    private final ConfigurationParser configurationParser;
    private final JiraClientFactory jiraClientFactory;

    @Inject
    public AuthenticationManager(AuthenticationRepository repository, ConfigurationParser configurationParser, JiraClientFactory jiraClientFactory) {
        this.repository = repository;
        this.configurationParser = configurationParser;
        this.jiraClientFactory = jiraClientFactory;
    }

    public AuthenticatedWhoResult authentication(AuthenticationCredentials credentials) {
        ApplicationConfiguration configuration = configurationParser.from(credentials);

        // Get the temporarily-stored token secret so we can generate an access token
        String tokenSecret = repository.getTokenSecret(credentials.getToken());
        if (Strings.isNullOrEmpty(tokenSecret)) {
            throw new RuntimeException("No token secret could be found - the token times out after 5 minutes");
        }

        OAuth1AccessToken accessToken;

        try {
            // Request an access token from JIRA using the given token, verifier and stored token secret
            accessToken = JiraApi10aFactory.create(configuration)
                    .getAccessToken(new OAuth1RequestToken(credentials.getToken(), tokenSecret), credentials.getVerifier());
        } catch (IOException | InterruptedException | ExecutionException e) {
            throw new RuntimeException("Unable to get the access token from JIRA: " + e.getMessage(), e);
        }

        if (accessToken == null) {
            throw new RuntimeException("No access token was given back from JIRA");
        }

        // Now we need to create this concatenated token + secret "token" so we can send it back and forth in one field
        String token = String.format("%s:::%s", accessToken.getToken(), accessToken.getTokenSecret());

        // Now we need to get the currently authorized user, to generate the AuthenticatedWhoResult
        JiraRestClient jiraRestClient = jiraClientFactory.create(configuration, token);

        Session session = jiraRestClient.getSessionClient()
                .getCurrentSession()
                .claim();

        return jiraRestClient.getUserClient().getUser(session.getUserUri())
                .map(user -> createAuthenticatedWhoResult(user, token))
                .claim();
    }

    private AuthenticatedWhoResult createAuthenticatedWhoResult(User user, String token) {
        AuthenticatedWhoResult result = new AuthenticatedWhoResult();
        result.setDirectoryId("JIRA");
        result.setDirectoryName("JIRA");
        result.setEmail(user.getEmailAddress());
        result.setFirstName(user.getName());
        result.setIdentityProvider("?");
        result.setLastName(user.getName());
        result.setStatus(AuthenticatedWhoResult.AuthenticationStatus.Authenticated);
        result.setTenantName("?");
        result.setToken(token);
        result.setUserId(user.getEmailAddress());
        result.setUsername(user.getEmailAddress());

        return result;
    }
}
