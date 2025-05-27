package boldair.data;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode( of = { "idCompte" } )
@Table( "compte" )
public class Compte {
	// -------
	// Champs
	// -------
	@Id
	@Column( "id_compte" )
	private Long	idCompte;
	@Column( "pseudo" )
	private String	pseudo;
	@Transient
	private String	motDePasse;
	@Column( "empreinte_mdp" )
	private String	empreinteMdp;
	@Column( "email" )
	private String	email;
	@Column( "role_admin" )
	private boolean	roleAdmin;
	@Column( "role_benevol" )
	private boolean	roleBenevol;

}
