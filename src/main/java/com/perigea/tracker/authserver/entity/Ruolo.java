package com.perigea.tracker.authserver.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.Table;

import com.perigea.tracker.commons.enums.RuoloType;

import lombok.Data;

@Data
@Entity
@Table(name = "ruoli")
public class Ruolo implements Serializable {

	private static final long serialVersionUID = -870008112526011657L;

	@Id
	@Column(name = "id")
	@Enumerated(EnumType.STRING)
	private RuoloType id;

	@Column(name = "descrizione_ruolo")
	private String descrizione;
	
}