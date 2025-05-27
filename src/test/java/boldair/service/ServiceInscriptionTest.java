package boldair.service;

import java.time.LocalDate;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.annotation.Transactional;

import boldair.StartWebServer;
import boldair.dao.DaoCompte;
import boldair.dao.DaoEquipe;
import boldair.dao.DaoParticipant;
import boldair.service.exception.InscriptionException;

@SpringBootTest( classes = { StartWebServer.class } )
@Sql( {
		"/db/1a-schema.sql",
		"/db/1b-tables.sql",
		"/db/3_delete.sql",
		"/db/3-compte.sql",
		"/db/4-epreuve-data.sql"
} )
@Transactional
public class ServiceInscriptionTest {

	@Autowired
	private ServiceInscription serviceInscription;

	@Autowired
	private DaoCompte daoCompte;

	@Autowired
	private DaoEquipe daoEquipe;

	@Autowired
	private DaoParticipant daoParticipant;

	@Test
	public void testInscrireEquipe_Success() throws InscriptionException {
		// Arrange
		String		email			= "test.equipe@example.com";
		String		motDePasse		= "motdepasse123";
		String		nomEquipe		= "Équipe Test";
		String		categorie		= "bol";
		String		nom1			= "Dupont";
		String		prenom1			= "Jean";
		String		sexe1			= "homme";
		String		statut1			= "capitaine";
		LocalDate	dateNaissance1	= LocalDate.of( 1990, 5, 15 );
		String		nom2			= "Martin";
		String		prenom2			= "Marie";
		String		sexe2			= "femme";
		String		statut2			= "suiveur";
		LocalDate	dateNaissance2	= LocalDate.of( 1992, 8, 20 );
		// Act
		serviceInscription.inscrireEquipe(
				email, motDePasse,
				nomEquipe, categorie, "2", // 2 tickets repas
				nom1, prenom1, sexe1, statut1, dateNaissance1,
				nom2, prenom2, sexe2, statut2, dateNaissance2 );

		// Assert
		// Vérifier que le compte a été créé
		var compte = daoCompte.findByEmail( email );
		assert compte != null;
		assert compte.getPseudo().equals( nomEquipe );
		assert compte.getEmail().equals( email );
		assert !compte.isRoleAdmin();
		assert !compte.isRoleBenevol(); // Vérifier que l'équipe a été créée
		var equipe = daoEquipe.findByNomEquipe( nomEquipe );
		assert equipe != null;
		assert equipe.getNomEquipe().equals( nomEquipe );
		assert equipe.getCategorie().equals( categorie );
		assert equipe.getNomBolDair().equals( "Bol d'Air" ); // Pour la catégorie "bol"
		assert equipe.getTicketRepas().equals( "2" );
		assert equipe.getIdEpreuve().equals( 2L ); // ID pour "Bol d'Air"
		assert !equipe.getPaid();

		// Vérifier que les participants ont été créés
		var participants = daoParticipant.findByIdEquipe( equipe.getIdEquipe() );
		assert participants.size() == 2;
		var participant1 = participants.stream()
				.filter( p -> p.getNom().equals( nom1 ) )
				.findFirst()
				.orElse( null );
		assert participant1 != null;
		assert participant1.getPrenom().equals( prenom1 );
		assert participant1.getSexe().equals( sexe1 );
		assert participant1.getStatus().equals( statut1 );
		assert participant1.getNomEquipe().equals( nomEquipe );
		assert participant1.getBolDAir().equals( categorie );
		assert participant1.getEmail().equals( email );
		assert participant1.getDateNaissance().equals( dateNaissance1 );
		var participant2 = participants.stream()
				.filter( p -> p.getNom().equals( nom2 ) )
				.findFirst()
				.orElse( null );
		assert participant2 != null;
		assert participant2.getPrenom().equals( prenom2 );
		assert participant2.getSexe().equals( sexe2 );
		assert participant2.getStatus().equals( statut2 );
		assert participant2.getNomEquipe().equals( nomEquipe );
		assert participant2.getBolDAir().equals( categorie );
		assert participant2.getEmail().equals( email );
		assert participant2.getDateNaissance().equals( dateNaissance2 );

		System.out.println( "✅ Test réussi : L'inscription d'équipe fonctionne correctement !" );
		System.out.println( "   - Compte créé pour : " + compte.getEmail() );
		System.out.println( "   - Équipe créée : " + equipe.getNomEquipe() );
		System.out.println( "   - Participants créés : " + participants.size() );
	}

	@Test
	public void testInscrireEquipe_EmailDejaUtilise() {
		// Arrange - utiliser un email qui existe déjà dans 3-compte.sql
		String emailExistant = "max@mail.com";

		// Act & Assert
		try {
			serviceInscription.inscrireEquipe(
					emailExistant, "motdepasse123",
					"Nouvelle Équipe", "bol", "1", // 1 ticket repas
					"Test", "Test", "homme", "capitaine", LocalDate.of( 1990, 1, 1 ),
					"Test2", "Test2", "femme", "suiveur", LocalDate.of( 1991, 1, 1 ) );
			assert false : "Une exception devrait être levée pour un email déjà utilisé";
		} catch ( InscriptionException e ) {
			assert e.getMessage().contains( "L'email est déjà utilisé" );
			System.out.println( "✅ Test réussi : Exception correctement levée pour email existant" );
		}
	}
}
