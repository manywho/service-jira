package com.manywho.services.jira.authorization;

import com.github.scribejava.core.model.OAuth1RequestToken;
import com.github.scribejava.core.oauth.OAuth10aService;
import com.google.inject.Inject;
import com.manywho.sdk.api.AuthorizationType;
import com.manywho.sdk.api.run.elements.type.ObjectDataRequest;
import com.manywho.sdk.api.run.elements.type.ObjectDataResponse;
import com.manywho.sdk.api.security.AuthenticatedWho;
import com.manywho.sdk.services.configuration.ConfigurationParser;
import com.manywho.sdk.services.types.TypeBuilder;
import com.manywho.sdk.services.types.system.$User;
import com.manywho.services.jira.ApplicationConfiguration;
import com.manywho.services.jira.jira.JiraApi10aFactory;

import java.io.IOException;
import java.util.concurrent.ExecutionException;

public class AuthorizationManager {
    private final AuthorizationRepository repository;
    private final ConfigurationParser configurationParser;
    private final TypeBuilder typeBuilder;

    @Inject
    public AuthorizationManager(AuthorizationRepository repository, ConfigurationParser configurationParser, TypeBuilder typeBuilder) {
        this.repository = repository;
        this.configurationParser = configurationParser;
        this.typeBuilder = typeBuilder;
    }

    public ObjectDataResponse authorization(AuthenticatedWho authenticatedWho, ObjectDataRequest request) {
        ApplicationConfiguration configuration = configurationParser.from(request);

        String status;

        switch (request.getAuthorization().getGlobalAuthenticationType()) {
            case AllUsers:
                // If it's a public user (i.e. not logged in) then return a 401
                if (authenticatedWho.getUserId().equals("PUBLIC_USER")) {
                    status = "401";
                } else {
                    status = "200";
                }

                break;
            case Public:
                status = "200";
                break;
            case Specified:
            default:
                status = "401";
                break;
        }

        OAuth10aService service = JiraApi10aFactory.create(configuration);

        OAuth1RequestToken requestToken;
        try {
            requestToken = service.getRequestToken();
        } catch (IOException | InterruptedException | ExecutionException e) {
            throw new RuntimeException("Unable to get the OAuth1.0a request token from JIRA", e);
        }

        // We need to store this secret temporarily so we can use it to generate an access token in a minute
        repository.putTokenSecret(requestToken.getToken(), requestToken.getTokenSecret());

        $User user = new $User();
        user.setDirectoryId("JIRA");
        user.setDirectoryName("JIRA");
        user.setAuthenticationType(AuthorizationType.Oauth);
        user.setLoginUrl(service.getAuthorizationUrl(requestToken));
        user.setStatus(status);
        user.setUserId("");

        return new ObjectDataResponse(typeBuilder.from(user));
    }

    public ObjectDataResponse groupAttributes() {
        throw new RuntimeException("Specifying group restrictions isn't yet supported in the JIRA Service");
    }

    public ObjectDataResponse groups(ObjectDataRequest request) {
        throw new RuntimeException("Specifying group restrictions isn't yet supported in the JIRA Service");
    }

    public ObjectDataResponse userAttributes() {
        throw new RuntimeException("Specifying user restrictions isn't yet supported in the JIRA Service");
    }

    public ObjectDataResponse users(ObjectDataRequest request) {
        throw new RuntimeException("Specifying user restrictions isn't yet supported in the JIRA Service");
    }
}
