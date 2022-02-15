package com.perigea.tracker.authserver.entity;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

import com.perigea.tracker.commons.enums.AnagraficaType;
import com.perigea.tracker.commons.enums.StatoUtenteType;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@Entity
@Table(name = "utente")
@EqualsAndHashCode(callSuper = false, exclude = {"password"})
@ToString(exclude = {"password", "ruoli"})
public class Utente implements Serializable {

	private static final long serialVersionUID = -3328921626742587531L;

	@Id
	@Column(name = "codice_persona", nullable = false)
	private String codicePersona;

	@Column(name = "password")
	private String password;

	@Column(name = "username", unique = true)
	private String username;
	
	@Column(name = "stato_utente")
	@Enumerated(EnumType.STRING)
	private StatoUtenteType stato;
	
	@Column(name = "account_locked")
	private boolean accountNonLocked;
	
	@Column(name = "account_expired")
	private boolean accountNonExpired;

	@Column(name = "credentials_expired")
	private boolean credentialsNonExpired;

	@Enumerated(EnumType.STRING)
	private AnagraficaType tipo;
	
	@Column(name = "nome", nullable = false)
	private String nome;

	@Column(name = "cognome", nullable = false)
	private String cognome;

	@Column(name = "mail_aziendale")
	private String mailAziendale;

	@Column(name = "mail_privata")
	private String mailPrivata;

	@ManyToMany(cascade = { CascadeType.ALL }, fetch = FetchType.EAGER)
    @JoinTable(
        name = "utente_ruolo", 
        joinColumns = { @JoinColumn(name = "codice_persona") }, 
        inverseJoinColumns = { @JoinColumn(name = "id") }
    )
	private List<Ruolo> ruoli = new ArrayList<>();

	public void addRuolo(Ruolo ruolo) {
		this.ruoli.add(ruolo);
	}
	
	public void removeRuolo(Ruolo ruolo) {
		this.ruoli.remove(ruolo);
	}
	
}