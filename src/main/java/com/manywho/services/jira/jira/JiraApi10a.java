package com.manywho.services.jira.jira;

import com.github.scribejava.core.builder.api.DefaultApi10a;
import com.github.scribejava.core.model.OAuth1RequestToken;
import com.github.scribejava.core.services.RSASha1SignatureService;
import com.github.scribejava.core.services.SignatureService;

import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.Base64;

public class JiraApi10a extends DefaultApi10a {
    private final String url;
    private final String privateKey;

    public JiraApi10a(String url, String privateKey) {
        this.url = url;
        this.privateKey = privateKey;
    }

    @Override
    public String getRequestTokenEndpoint() {
        return String.format("%s/plugins/servlet/oauth/request-token", url);
    }

    @Override
    public String getAccessTokenEndpoint() {
        return String.format("%s/plugins/servlet/oauth/access-token", url);
    }

    @Override
    public String getAuthorizationUrl(OAuth1RequestToken oAuth1RequestToken) {
        return String.format("%s/plugins/servlet/oauth/authorize?oauth_token=%s", url, oAuth1RequestToken.getToken());
    }

    @Override
    public SignatureService getSignatureService() {
        KeyFactory keyFactory;
        try {
            keyFactory = KeyFactory.getInstance("RSA");
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("The given algorithm could not be found", e);
        }

        PKCS8EncodedKeySpec pkcs8EncodedKeySpec = new PKCS8EncodedKeySpec(Base64.getDecoder().decode(privateKey));

        try {
            return new RSASha1SignatureService(keyFactory.generatePrivate(pkcs8EncodedKeySpec));
        } catch (InvalidKeySpecException e) {
            throw new RuntimeException("An invalid key specification was given", e);
        }
    }
}
