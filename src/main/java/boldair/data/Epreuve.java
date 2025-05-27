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
@EqualsAndHashCode( of = { "idEpreuve" } )
@Table( "epreuve" )
public class Epreuve {

	// -------
	// Champs
	// -------
	@Id
	@Column( "id_epreuve" )
	private Long	idEpreuve;
	@Column( "nom_bol_dair" )
	private String	nomBolDair;
}
