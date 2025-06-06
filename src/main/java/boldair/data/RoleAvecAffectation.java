package boldair.data;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RoleAvecAffectation {
    private Role role;
    private int quantiteRequise;
    private int quantiteAffectee;
    private int quantiteRestante;
    
    public RoleAvecAffectation(Role role, int quantiteAffectee) {
        this.role = role;
        this.quantiteRequise = role.getQuantiteNumerique();
        this.quantiteAffectee = quantiteAffectee;
        this.quantiteRestante = Math.max(0, this.quantiteRequise - this.quantiteAffectee);
    }
    
    public boolean isComplet() {
        return quantiteAffectee >= quantiteRequise;
    }
    
    public String getStatutAffectation() {
        if (quantiteAffectee == 0) {
            return "Non affecté";
        } else if (isComplet()) {
            return "Complet";
        } else {
            return "Partiellement affecté";
        }
    }
    
    // Méthodes pour accéder aux propriétés du rôle
    public Long getIdRoles() {
        return role != null ? role.getIdRoles() : null;
    }
    
    public String getNomRole() {
        return role != null ? role.getNomRole() : null;
    }
    
    public int getQuantiteNumerique() {
        return role != null ? role.getQuantiteNumerique() : 0;
    }
    
    public String getHoraire() {
        return role != null ? role.getHoraire() : null;
    }
}
