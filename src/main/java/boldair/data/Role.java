package boldair.data;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = {"idRoles"})
@Table("roles")
public class Role {
      @Id
    @Column("id_roles")
    private Long idRoles;
    
    @Column("nomrole")
    private String nomRole;
    
    @Column("quantité")
    private String quantite;
    
    @Column("type_benevole")
    private String typeBenevole;
    
    @Column("horaire")
    private String horaire;
    
    @Column("bénévoles")
    private String benevoles;
    
    // Méthode utilitaire pour obtenir la quantité comme nombre
    public int getQuantiteNumerique() {
        if (quantite == null || quantite.trim().isEmpty()) {
            return 0;
        }
        
        String quantiteStr = quantite.trim();
        
        // Cas spéciaux pour les formats complexes
        if (quantiteStr.contains("postes 2 pers")) {
            // "3 postes 2 pers (6)" -> extraire le nombre entre parenthèses
            int start = quantiteStr.indexOf('(');
            int end = quantiteStr.indexOf(')');
            if (start != -1 && end != -1 && start < end) {
                try {
                    return Integer.parseInt(quantiteStr.substring(start + 1, end));
                } catch (NumberFormatException e) {
                    // Si ça échoue, utiliser la méthode normale
                }
            }
        }
        
        if (quantiteStr.contains("à")) {
            // "1 à 2" -> prendre le maximum
            String[] parts = quantiteStr.split("à");
            if (parts.length == 2) {
                try {
                    return Integer.parseInt(parts[1].trim());
                } catch (NumberFormatException e) {
                    // Continue avec la méthode normale
                }
            }
        }
        
        // Extraire le premier nombre de la chaîne (ex: "37 postes" -> 37)
        String[] parts = quantiteStr.split("\\s+");
        try {
            return Integer.parseInt(parts[0]);
        } catch (NumberFormatException e) {
            return 0;
        }
    }
}
