package com.perigea.tracker.authserver.config;

import java.security.KeyPair;
import java.security.SecureRandom;
import java.security.interfaces.RSAPrivateKey;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.security.jwt.JwtHelper;
import org.springframework.security.jwt.crypto.sign.RsaSigner;
import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.common.util.JsonParser;
import org.springframework.security.oauth2.common.util.JsonParserFactory;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;

import com.perigea.tracker.authserver.authuser.AuthUser;

@SuppressWarnings({"deprecation", "unchecked"})
public class JwtCustomHeadersAccessTokenConverter extends JwtAccessTokenConverter {

    private Map<String, String> customHeaders = new HashMap<>();
    private JsonParser objectMapper = JsonParserFactory.create();
    private final RsaSigner signer;
    private String issuer;

    public JwtCustomHeadersAccessTokenConverter(Map<String, String> customHeaders, KeyPair keyPair) {
        super();
        super.setKeyPair(keyPair);
        this.signer = new RsaSigner((RSAPrivateKey) keyPair.getPrivate());
        this.customHeaders = customHeaders;
    }
    
    @Override
	public OAuth2AccessToken enhance(OAuth2AccessToken accessToken, OAuth2Authentication authentication) {
		AuthUser user = (AuthUser) authentication.getPrincipal();
		Map<String, Object> info = new LinkedHashMap<String, Object>(accessToken.getAdditionalInformation());

		info.put("email", user.getUsername());
		info.put("name", user.getNome());
		info.put("lastname", user.getCognome());
		info.put("type", user.getTipo());

		DefaultOAuth2AccessToken customAccessToken = new DefaultOAuth2AccessToken(accessToken);
		customAccessToken.setAdditionalInformation(info);
		customHeaders.put("nonce", generateNonce());
		return super.enhance(customAccessToken, authentication);
	}
    
    public void setIssuer(String issuer) {
    	this.issuer = issuer;
    }

	@Override
    protected String encode(OAuth2AccessToken accessToken, OAuth2Authentication authentication) {
        String content;
        try {
			Map<String, Object> info = (Map<String, Object>) getAccessTokenConverter().convertAccessToken(accessToken, authentication);
    		DefaultOAuth2AccessToken result = new DefaultOAuth2AccessToken(accessToken);
    		String tokenId = result.getValue();
        	if (!info.containsKey(TOKEN_ID)) {
    			info.put(TOKEN_ID, tokenId);
    		}
        	info.put("sub", authentication.getName());
        	info.put("iss", issuer);
            content = this.objectMapper.formatMap(info);
        } catch (Exception ex) {
            throw new IllegalStateException("Cannot convert access token to JSON", ex);
        }
        return JwtHelper.encode(content, this.signer, this.customHeaders).getEncoded();
    }
    
    private String generateNonce(){
        SecureRandom secureRandom = new SecureRandom();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 10; i++) {
        	sb.append(secureRandom.nextInt(10));
        }
        String randomNumber = sb.toString();
        return randomNumber;
    }

}