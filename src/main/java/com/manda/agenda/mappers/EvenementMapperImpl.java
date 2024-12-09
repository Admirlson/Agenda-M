package com.manda.agenda.mappers;

import com.manda.agenda.dto.EvenementDTO;
import com.manda.agenda.models.Evenement;

public class EvenementMapperImpl implements EvenementMapper {

    @Override
    public Evenement toEvenement(EvenementDTO evenementDTO) {
        // TODO Auto-generated method stub
        return new Evenement(evenementDTO.getId(), evenementDTO.getDate(), evenementDTO.getFormat(),
                evenementDTO.getType(), evenementDTO.getHeure(), evenementDTO.getInstitution(),
                evenementDTO.getObjectif(), evenementDTO.getStatut(), evenementDTO.getNouvelleDate(),
                evenementDTO.getSuivis(), evenementDTO.getWhocreated(), evenementDTO.getDatecreated(),
                evenementDTO.getWhomodified(), evenementDTO.getDatemodified(), evenementDTO.isReminderSent());
    }

    @Override
    public EvenementDTO toEvenementDTO(Evenement evenement) {
        // TODO Auto-generated method stub
        return new EvenementDTO(evenement.getId(), evenement.getDate(), evenement.getFormat(),
                evenement.getType(), evenement.getHeure(), evenement.getInstitution(),
                evenement.getObjectif(), evenement.getStatut(), evenement.getNouvelleDate(),
                evenement.getSuivis(), evenement.getWhocreated(), evenement.getDatecreated(),
                evenement.getWhomodified(), evenement.getDatemodified(), evenement.isRemimderSent());
    }

}
