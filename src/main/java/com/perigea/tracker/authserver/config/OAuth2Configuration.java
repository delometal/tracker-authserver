package com.perigea.tracker.authserver.config;

import java.security.KeyPair;
import java.security.interfaces.RSAPublicKey;
import java.util.HashMap;
import java.util.Map;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.OAuth2RequestFactory;
import org.springframework.security.oauth2.provider.endpoint.TokenEndpointAuthenticationFilter;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;
import org.springframework.security.oauth2.provider.token.store.KeyStoreKeyFactory;

import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.KeyUse;
import com.nimbusds.jose.jwk.RSAKey;
import com.perigea.tracker.authserver.service.CustomUserDetailsService;


@Configuration
@EnableAuthorizationServer
@SuppressWarnings("deprecation")
public class OAuth2Configuration extends AuthorizationServerConfigurerAdapter {
	
    @Autowired
    private AuthPropertiesConfiguration authPropertiesConfiguration;
	
	@Autowired
	private AuthenticationManager authenticationManager;

	@Autowired
	private DataSource dataSource;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Autowired
	private CustomUserDetailsService userDetailsService;

	@Autowired
	private ClientDetailsService clientDetailsService;
	
	@Bean
	public OAuth2RequestFactory requestFactory() {
		Oauth2CustomRequestFactory requestFactory = new Oauth2CustomRequestFactory(clientDetailsService);
		requestFactory.setCheckUserScopes(true);
		return requestFactory;
	}
	
	@Bean
	public TokenStore tokenStore() {
		return new JwtTokenStore(accessTokenConverter());
	}

    @Bean
    public JwtAccessTokenConverter accessTokenConverter() {
        Map<String, String> customHeaders = new HashMap<>();
        customHeaders.put("kid", authPropertiesConfiguration.getJwtKid());
        JwtAccessTokenConverter jwt = new JwtCustomHeadersAccessTokenConverter(customHeaders, keyPair());
        ((JwtCustomHeadersAccessTokenConverter) jwt).setIssuer(authPropertiesConfiguration.getIssuer());
        jwt.setSigningKey(authPropertiesConfiguration.getJwtSigningKey());
        return jwt;
    }
	
    @Bean("keyPairPerigea")
    public KeyPair keyPair() {
        ClassPathResource ksFile = new ClassPathResource(authPropertiesConfiguration.getJwtJksFile());
        KeyStoreKeyFactory ksFactory = new KeyStoreKeyFactory(ksFile, authPropertiesConfiguration.getKeyPairKeyPass().toCharArray());
        return ksFactory.getKeyPair(authPropertiesConfiguration.getKeyPairAlias());
    }
    
    @Bean
	public TokenEndpointAuthenticationFilter tokenEndpointAuthenticationFilter() {
		return new TokenEndpointAuthenticationFilter(authenticationManager, requestFactory());
	}

    @Bean
    public JWKSet jwkSet() {
        RSAKey.Builder builder = new RSAKey.Builder((RSAPublicKey) keyPair().getPublic()).keyUse(KeyUse.SIGNATURE)
            .algorithm(JWSAlgorithm.RS256)
            .keyID(authPropertiesConfiguration.getJwtKid());
        return new JWKSet(builder.build());
    }

	@Override
	public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
		clients.jdbc(dataSource).passwordEncoder(passwordEncoder);
	}
	
	@Override
	public void configure(AuthorizationServerSecurityConfigurer oauthServer) throws Exception {
		oauthServer.tokenKeyAccess("permitAll()").checkTokenAccess("isAuthenticated()");
	}
	
	@Override
	public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
		endpoints.tokenStore(tokenStore()).tokenEnhancer(accessTokenConverter())
				.authenticationManager(authenticationManager).userDetailsService(userDetailsService);
		if (authPropertiesConfiguration.getCheckUserScopes())
			endpoints.requestFactory(requestFactory());
	}

}