package boldair.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import boldair.dao.DaoBenevol;
import boldair.dao.DaoCompte;
import boldair.dao.DaoEquipe;
import boldair.dao.DaoParticipant;
import boldair.data.Benevol;
import boldair.data.Compte;
import boldair.data.Equipe;
import boldair.data.Participant;

@Service
public class ServiceAdmin {

    @Autowired
    private DaoBenevol daoBenevol;
    
    @Autowired
    private DaoEquipe daoEquipe;
    
    @Autowired
    private DaoParticipant daoParticipant;
    
    @Autowired
    private DaoCompte daoCompte;

    /**
     * Obtient le nombre total de bénévoles inscrits
     */
    public long getVolunteerCount() {
        return daoBenevol.countAllVolunteers();
    }

    /**
     * Obtient le nombre total d'équipes inscrites
     */
    public long getTeamCount() {
        return daoEquipe.countAllTeams();
    }

    /**
     * Obtient tous les bénévoles avec leurs comptes associés
     */
    public List<Benevol> getAllVolunteers() {
        return (List<Benevol>) daoBenevol.findAll();
    }

    /**
     * Obtient toutes les équipes triées par nom
     */
    public List<Equipe> getAllTeams() {
        return daoEquipe.findAllOrderByName();
    }

    /**
     * Obtient les participants d'une équipe spécifique
     */
    public List<Participant> getTeamParticipants(Long teamId) {
        return daoParticipant.findByIdEquipe(teamId);
    }

    /**
     * Obtient le compte associé à un bénévole
     */
    public Compte getVolunteerAccount(Long accountId) {
        return daoCompte.findById(accountId).orElse(null);
    }
} 