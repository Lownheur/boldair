package boldair.data;

import org.springframework.data.annotation.Id;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = { "idBenevol" })
public class Benevol {

    // -------
    // Champs
    // -------

    @Id
    private Long idBenevol;
    private Boolean interneExterne; // Corresponds to interne___externe in database
    private String nom;
    private String email;
    private String téléphone;
    private String prénom;
    private String statut;
} 