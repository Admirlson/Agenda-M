package com.manda.agenda.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.MapperConfig;

import com.manda.agenda.dto.EvenementDTO;
import com.manda.agenda.models.Evenement;

@Mapper(config = MapperConfig.class)
public interface EvenementMapper {
    Evenement toEvenement(EvenementDTO evenementDTO);

    EvenementDTO toEvenementDTO(Evenement evenement);
}
