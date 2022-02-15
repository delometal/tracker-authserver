package com.perigea.tracker.authserver.authuser;
import java.io.Serializable;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.perigea.tracker.commons.dto.RuoloDto;
import com.perigea.tracker.commons.enums.StatoUtenteType;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode
public class AuthUser implements Serializable, UserDetails {

	private static final long serialVersionUID = 4555043952496375034L;

	private String username;
	private String nome;
	private String cognome;
	private String tipo;
	private String password;
	private String email;
	private String emailPersonale;
	private String stato;	
	private List<RuoloDto> ruoli;

	private boolean accountNonLocked;
	private boolean accountNonExpired;
	private boolean credentialsNonExpired;
	
	@Override
	public boolean isEnabled() {
		return StatoUtenteType.A.getDescrizione().equalsIgnoreCase(stato);
	}

	@Override
	public boolean isAccountNonExpired() {
		return !accountNonExpired;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return !credentialsNonExpired;
	}

	@Override
	public boolean isAccountNonLocked() {
		return !accountNonLocked;
	}
	
	/*
	 * Get roles and permissions and add them as a Set of GrantedAuthority
	 */
	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		Set<GrantedAuthority> authorities = new HashSet<GrantedAuthority>();
		ruoli.forEach(r -> authorities.add(new SimpleGrantedAuthority("ROLE_" + r.getId().getDescrizione())));
		return authorities;
	}

}