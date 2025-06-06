package boldair.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import boldair.dao.DaoBenevol;
import boldair.dao.DaoCompte;
import boldair.dao.DaoEquipe;
import boldair.dao.DaoParticipant;
import boldair.dao.DaoRole;
import boldair.data.Benevol;
import boldair.data.Compte;
import boldair.data.Equipe;
import boldair.data.Participant;
import boldair.data.Role;
import boldair.data.RoleAvecAffectation;

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
    
    @Autowired
    private DaoRole daoRole;
    
    @Autowired
    private JdbcTemplate jdbcTemplate;

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
    
    /**
     * Obtient tous les rôles disponibles
     */
    public List<Role> getAllRoles() {
        return daoRole.findAllByOrderByNomRoleAsc();
    }
      /**
     * Obtient tous les rôles avec leurs informations d'affectation
     */
    public List<RoleAvecAffectation> getAllRolesWithAffectation() {
        List<Role> roles = (List<Role>) daoRole.findAll();
        System.out.println("Nombre de rôles trouvés: " + roles.size());
        
        return roles.stream()
            .map(role -> {
                long currentAffected = daoBenevol.countByRoleId(role.getIdRoles());
                System.out.println("Rôle " + role.getNomRole() + " - Quantité: " + role.getQuantiteNumerique() + " - Affectés: " + currentAffected);
                
                return new RoleAvecAffectation(role, (int) currentAffected);
            })
            .collect(Collectors.toList());
    }
    
    /**
     * Calcule le total des postes restants à affecter
     */
    public int getTotalPostesRestants() {
        return getAllRolesWithAffectation().stream()
            .mapToInt(RoleAvecAffectation::getQuantiteRestante)
            .sum();
    }
    
    /**
     * Affecte un rôle à un bénévole
     */
    @Transactional
    public boolean assignRoleToBenevol(Long benevolId, Long roleId) {
        try {
            System.out.println("=== DEBUT assignRoleToBenevol ===");
            System.out.println("benevolId: " + benevolId + ", roleId: " + roleId);
            
            // Vérifier si le bénévole a déjà un rôle
            Benevol benevol = daoBenevol.findById(benevolId).orElse(null);
            if (benevol == null) {
                System.out.println("ERREUR: Bénévole introuvable");
                return false;
            }
            
            if (benevol.getIdRole() != null) {
                System.out.println("ERREUR: Bénévole a déjà un rôle assigné (ID: " + benevol.getIdRole() + ")");
                return false; // Déjà affecté à un rôle
            }
            
            // Vérifier si le rôle a encore de la place
            Role role = daoRole.findById(roleId).orElse(null);
            if (role == null) {
                System.out.println("ERREUR: Rôle introuvable");
                return false;
            }
            
            System.out.println("Rôle trouvé: " + role.getNomRole());
            System.out.println("Quantité du rôle: " + role.getQuantite());
            System.out.println("Quantité numérique: " + role.getQuantiteNumerique());
            
            long currentAffected = daoBenevol.countByRoleId(roleId);
            System.out.println("Bénévoles actuellement affectés: " + currentAffected);
            
            if (currentAffected >= role.getQuantiteNumerique()) {
                System.out.println("ERREUR: Rôle complet (" + currentAffected + " >= " + role.getQuantiteNumerique() + ")");
                return false; // Rôle complet
            }
            
            // Affecter le rôle au bénévole
            System.out.println("Affectation du rôle...");
            daoBenevol.assignRole(benevolId, roleId);
            System.out.println("Rôle affecté avec succès");
            
            System.out.println("=== FIN assignRoleToBenevol ===");
            return true;
        } catch (Exception e) {
            System.err.println("ERREUR dans assignRoleToBenevol: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
    
    /**
     * Retire un rôle d'un bénévole
     */
    @Transactional
    public boolean removeRoleFromBenevol(Long benevolId, Long roleId) {
        try {
            System.out.println("=== DEBUT removeRoleFromBenevol ===");
            System.out.println("benevolId: " + benevolId + ", roleId: " + roleId);
            
            // Vérifier si le bénévole a bien ce rôle
            Benevol benevol = daoBenevol.findById(benevolId).orElse(null);
            if (benevol == null) {
                System.out.println("ERREUR: Bénévole introuvable");
                return false;
            }
            
            if (benevol.getIdRole() == null || !benevol.getIdRole().equals(roleId)) {
                System.out.println("ERREUR: Le bénévole n'a pas ce rôle assigné");
                return false;
            }
            
            // Retirer le rôle
            daoBenevol.removeRole(benevolId);
            System.out.println("Rôle retiré avec succès");
            System.out.println("=== FIN removeRoleFromBenevol ===");
            return true;
        } catch (Exception e) {
            System.err.println("ERREUR dans removeRoleFromBenevol: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
    
    /**
     * Obtient le rôle affecté à un bénévole
     */
    public Role getBenevolRole(Long benevolId) {
        Benevol benevol = daoBenevol.findById(benevolId).orElse(null);
        if (benevol == null || benevol.getIdRole() == null) {
            return null;
        }
        return daoRole.findById(benevol.getIdRole()).orElse(null);
    }
    
    /**
     * Obtient tous les bénévoles assignés à un rôle spécifique
     */
    public List<Benevol> getBenevolsByRole(Long roleId) {
        return daoBenevol.findByRoleId(roleId);
    }
    
    /**
     * Obtient tous les bénévoles sans rôle assigné
     */
    public List<Benevol> getBenevolsWithoutRole() {
        return daoBenevol.findBenevolsWithoutRole();
    }
}