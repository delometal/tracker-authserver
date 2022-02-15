package com.perigea.tracker.authserver.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AccountStatusUserDetailsChecker;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import com.perigea.tracker.authserver.authuser.AuthUser;
import com.perigea.tracker.authserver.entity.Utente;
import com.perigea.tracker.authserver.mapper.DtoEntityMapper;
import com.perigea.tracker.authserver.repository.UtenteRepository;
import com.perigea.tracker.commons.utils.Utils;

@Service(value = "userDetailsService")
public class CustomUserDetailsService implements UserDetailsService {

	@Autowired
	private UtenteRepository utenteRepository;
	
	@Autowired
	private DtoEntityMapper dtoEntityMapper;

	@Override
	public UserDetails loadUserByUsername(String input) {
		AccountStatusUserDetailsChecker checker = new AccountStatusUserDetailsChecker();
		Utente user = utenteRepository.findByUsername(input);

		if (user == null || (Utils.isEmpty(user.getRuoli())))
			throw new BadCredentialsException("Bad credentials");

		AuthUser authUser = dtoEntityMapper.entityToDto(user);
		checker.check(authUser);
		return authUser;
	}
}
