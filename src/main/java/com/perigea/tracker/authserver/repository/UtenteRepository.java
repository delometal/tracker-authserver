package com.perigea.tracker.authserver.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.perigea.tracker.authserver.entity.Utente;


@Repository
public interface UtenteRepository extends JpaRepository<Utente, String> {
	
	public Utente findByUsername(@Param("username") String username);
	
} 
