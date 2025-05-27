package boldair.data;

import java.time.LocalDate;

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
@EqualsAndHashCode( of = { "idParticipant" } )
@Table( "participant" )
public class Participant {

	// -------
	// Champs
	// -------

	@Id
	@Column( "id_participateur" )
	private Long		idParticipant;
	@Column( "nom" )
	private String		nom;
	@Column( "prénom" )
	private String		prenom;
	@Column( "status" )
	private String		status;				// capitaine ou suiveur
	@Column( "sexe" )
	private String		sexe;
	@Column( "date_de_naissance" )
	private LocalDate	dateNaissance;
	@Column( "email" )
	private String		email;
	@Column( "certificat_médical" )
	private String		certificatMedical;
	@Column( "num_puce" )
	private String		numPuce;
	@Column( "id_equipe" )
	private Long		idEquipe;
}
