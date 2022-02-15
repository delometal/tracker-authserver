package com.perigea.tracker.authserver.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Configuration
public class AuthPropertiesConfiguration {
	
	@Value("${keypair.keypass}")
	private String keyPairKeyPass;
	
	@Value("${keypair.alias}")
	private String keyPairAlias;
	
	@Value("${keypair.keystore}")
	private String keyPairKeyStore;
	
	@Value("${jwt.signing.key}")
	private String jwtSigningKey;
	
	@Value("${jwt.kid}")
	private String jwtKid;
	
	@Value("${jwt.issuer}")
	private String issuer;

	@Value("${jwt.jks.file}")
	private String jwtJksFile;

	@Value("${check-user-scopes:false}")
	private Boolean checkUserScopes;
	
}
