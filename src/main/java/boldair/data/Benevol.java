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
@EqualsAndHashCode( of = { "idBenevol" } )
@Table( "benevol" )
public class Benevol {
	// -------
	// Champs
	// -------
	@Id
	@Column( "id_benevol" )
	private Long	idBenevol;
	@Column( "interne___externe" )
	private Boolean	interneExterne;	// Corresponds to interne___externe in database
	@Column( "nom" )
	private String	nom;
	@Column( "email" )
	private String	email;
	@Column( "téléphone" )
	private String	téléphone;
	@Column( "prénom" )
	private String	prénom;
	@Column( "statut" )
	private String	statut;

	@Column( "heure_debut_dispo" )
	private java.time.LocalTime heureDebutDispo;

	@Column( "heure_fin_dispo" )
	private java.time.LocalTime heureFinDispo;

	@Column( "commentaire" )
	private String commentaire;

	@Column( "permis" )
	private Boolean permis;

	@Column( "interne" )
	private Boolean interne;

	@Column( "id_compte" )
	private Long idCompte;
}
