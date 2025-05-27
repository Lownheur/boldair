package boldair.service;

import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import boldair.dao.DaoCompte;
import boldair.dao.DaoEquipe;
import boldair.dao.DaoParticipant;
import boldair.data.Compte;
import boldair.data.Equipe;
import boldair.data.Participant;
import boldair.service.exception.InscriptionException;

@Service
public class ServiceInscription {

	@Autowired
	private DaoCompte	daoCompte;
	@Autowired
	private DaoEquipe	daoEquipe;

	@Autowired
	private DaoParticipant daoParticipant;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Transactional
	public void inscrireEquipe(
			// Informations du compte
			String email,
			String motDePasse,
			// Informations de l'équipe
			String nomEquipe,
			String categorie,
			String ticketRepas,
			// Informations du participant 1
			String nom1,
			String prenom1,
			String sexe1,
			String statut1,
			LocalDate dateNaissance1,
			// Informations du participant 2
			String nom2,
			String prenom2,
			String sexe2,
			String statut2,
			LocalDate dateNaissance2 ) throws InscriptionException {

		// Vérifier si l'email est déjà utilisé
		if ( daoCompte.findByEmail( email ) != null ) {
			throw new InscriptionException( "L'email est déjà utilisé" );
		}

		// Vérifier si le nom d'équipe est déjà utilisé
		if ( daoEquipe.findByNomEquipe( nomEquipe ) != null ) {
			throw new InscriptionException( "Le nom d'équipe est déjà utilisé" );
		}

		// Créer un compte
		Compte compte = new Compte();
		compte.setPseudo( nomEquipe ); // Utiliser le nom d'équipe comme pseudo
		compte.setEmail( email );
		compte.setEmpreinteMdp( passwordEncoder.encode( motDePasse ) );
		compte.setRoleAdmin( false );
		compte.setRoleBenevol( false );
		// Sauvegarder le compte
		compte = daoCompte.save( compte );

		// Déterminer l'épreuve selon la catégorie
		String	nomEpreuve;
		Long	idEpreuve;
		switch ( categorie ) {
		case "mini":
			nomEpreuve = "Mini Bol d'Air";
			idEpreuve = 1L;
			break;
		case "bol":
			nomEpreuve = "Bol d'Air";
			idEpreuve = 2L;
			break;
		case "decouverte":
			nomEpreuve = "Bol d'Air Découverte";
			idEpreuve = 3L;
			break;
		default:
			nomEpreuve = "Bol d'Air";
			idEpreuve = 2L;
			break;
		}

		// Créer une équipe
		Equipe equipe = new Equipe();
		equipe.setNomEquipe( nomEquipe );
		equipe.setCategorie( categorie );
		equipe.setNomBolDair( nomEpreuve ); // Nom complet de l'épreuve selon la catégorie
		equipe.setTicketRepas( ticketRepas ); // Nombre de tickets repas
		equipe.setPaid( false ); // Par défaut, le paiement n'est pas effectué
		equipe.setIdEpreuve( idEpreuve ); // ID de l'épreuve selon la catégorie

		// Sauvegarder l'équipe
		equipe = daoEquipe.save( equipe ); // Créer le participant 1
		Participant participant1 = new Participant();
		participant1.setNom( nom1 );
		participant1.setPrenom( prenom1 );
		participant1.setSexe( sexe1 );
		participant1.setStatus( statut1 );
		participant1.setNomEquipe( nomEquipe ); // pseudo du compte
		participant1.setBolDAir( categorie ); // catégorie de l'épreuve
		participant1.setDateNaissance( dateNaissance1 );
		participant1.setEmail( email ); // Même email que le compte
		participant1.setIdEquipe( equipe.getIdEquipe() );

		// Sauvegarder le participant 1
		daoParticipant.save( participant1 );

		// Créer le participant 2
		Participant participant2 = new Participant();
		participant2.setNom( nom2 );
		participant2.setPrenom( prenom2 );
		participant2.setSexe( sexe2 );
		participant2.setStatus( statut2 );
		participant2.setNomEquipe( nomEquipe ); // pseudo du compte
		participant2.setBolDAir( categorie ); // catégorie de l'épreuve
		participant2.setDateNaissance( dateNaissance2 );
		participant2.setEmail( email ); // Même email que le compte
		participant2.setIdEquipe( equipe.getIdEquipe() );

		// Sauvegarder le participant 2
		daoParticipant.save( participant2 );
	}
}
