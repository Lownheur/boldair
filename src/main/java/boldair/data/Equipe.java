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
@EqualsAndHashCode( of = { "idEquipe" } )
@Table( "equipe" )
public class Equipe {

	// -------
	// Champs
	// -------
	@Id
	@Column( "id_equipe" )
	private Long	idEquipe;
	@Column( "nom_equipe" )
	private String	nomEquipe;
	@Column( "nom_bol_dair" )
	private String	nomBolDair;
	@Column( "num_classement" )
	private String	numClassement;
	@Column( "catégorie" )
	private String	categorie;
	@Column( "temps_retenu" )
	private String	tempsRetenu;
	@Column( "paid" )
	private Boolean	paid;
	@Column( "numero_dossard" )
	private String	numeroDossard;
	@Column( "ticket_repas" )
	private String	ticketRepas;
	@Column( "id_epreuve" )
	private Long	idEpreuve;
}
