package com.perigea.tracker.authserver.mapper;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.perigea.tracker.authserver.authuser.AuthUser;
import com.perigea.tracker.authserver.entity.Ruolo;
import com.perigea.tracker.authserver.entity.Utente;
import com.perigea.tracker.commons.dto.RuoloDto;
import com.perigea.tracker.commons.enums.AnagraficaType;
import com.perigea.tracker.commons.enums.StatoUtenteType;

@Mapper(componentModel = "spring")
public interface DtoEntityMapper {
	
	@Mapping(target= "email", source= "mailAziendale")
	@Mapping(target= "emailPersonale", source= "mailPrivata")
	AuthUser entityToDto(Utente entity);
	
	Ruolo dtoToEntity(RuoloDto dto);
	RuoloDto entityToDto(Ruolo entity);

	List<RuoloDto> dtoToEntity(List<Ruolo> list);
	List<Ruolo> dtoToEntityRuoloList(List<RuoloDto> list);
	
	default String anagraficaTypeToString(AnagraficaType anagraficaType){
        return anagraficaType.getDescrizione();
    }
	default String statoUtenteTypeToString(StatoUtenteType statoUtenteType){
        return statoUtenteType.getDescrizione();
    }
	
}