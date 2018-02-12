package com.manywho.services.jira.jira;

import com.github.scribejava.core.builder.ServiceBuilder;
import com.github.scribejava.core.oauth.OAuth10aService;
import com.manywho.services.jira.ApplicationConfiguration;

public class JiraApi10aFactory {
    public static OAuth10aService create(ApplicationConfiguration configuration) {
        String privateKey = configuration.getConsumerSecret()
                .replace("-----BEGIN PRIVATE KEY-----", "")
                .replace("\n", "")
                .replace("-----END PRIVATE KEY-----", "");

        return new ServiceBuilder(configuration.getConsumerKey())
                .apiSecret(privateKey)
                .callback("http://localhost:22936/api/run/1/oauth")
                .build(new JiraApi10a(configuration.getUrl(), privateKey));
    }
}
