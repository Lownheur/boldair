package boldair.web;

import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import boldair.service.ServiceInscription;
import boldair.service.exception.InscriptionException;
import boldair.util.Alert;
import boldair.util.Alert.Color;

@Controller
public class WebPublic {

	@Autowired
	private ServiceInscription serviceInscription;

	// -------
	// Endpoints
	// -------

	// -------
	// home()

	@GetMapping( "/" )
	public String home() {
		return "public/accueil";
	}

	// -------
	// mentionsLegales()

	@GetMapping( "/mentions-legales" )
	public String mentionsLegales() {
		return "public/mentions-legales";
	}

	// -------
	// quiSommesNous()

	@GetMapping( "/qui-sommes-nous" )
	public String quiSommesNous() {
		return "public/qui-sommes-nous";
	}

	// -------
	// inscription()

	@GetMapping( "/inscription" )
	public String inscription() {
		return "public/inscription";
	}

	// -------
	// inscriptionSubmit()
	@PostMapping( "/inscription" )
	public String inscriptionSubmit(
			// Informations du compte
			@RequestParam( "email" ) String email,
			@RequestParam( "password" ) String password,
			// Informations de l'équipe
			@RequestParam( "equipe" ) String equipe,
			@RequestParam( "categorie" ) String categorie,
			@RequestParam( "ticketRepas" ) String ticketRepas,
			// Informations du participant 1
			@RequestParam( "nom1" ) String nom1,
			@RequestParam( "prenom1" ) String prenom1,
			@RequestParam( "sexe1" ) String sexe1,
			@RequestParam( "status1" ) String status1,
			@RequestParam( "naissance1" ) String naissance1,
			// Informations du participant 2
			@RequestParam( "nom2" ) String nom2,
			@RequestParam( "prenom2" ) String prenom2,
			@RequestParam( "sexe2" ) String sexe2,
			@RequestParam( "status2" ) String status2,
			@RequestParam( "naissance2" ) String naissance2,
			// Redirection
			RedirectAttributes redirectAttributes,
			Model model ) {
		try {
			// Convertir les dates de naissance en LocalDate
			LocalDate	dateNaissance1	= LocalDate.parse( naissance1 );
			LocalDate	dateNaissance2	= LocalDate.parse( naissance2 );	// Appeler le service d'inscription
			serviceInscription.inscrireEquipe(
					email, password,
					equipe, categorie, ticketRepas,
					nom1, prenom1, sexe1, status1, dateNaissance1,
					nom2, prenom2, sexe2, status2, dateNaissance2 );// Ajouter un message de succès
			redirectAttributes.addFlashAttribute( "alert",
					new Alert(
							Color.SUCCESS,
							"Votre inscription a été enregistrée avec succès. Vous pouvez maintenant vous connecter." ) );

			// Rediriger vers la page de connexion
			return "redirect:/connexion";
		} catch ( InscriptionException e ) {
			// Ajouter un message d'erreur
			model.addAttribute( "alert", new Alert( Color.DANGER, e.getMessage() ) ); // Conserver les données saisies
			model.addAttribute( "email", email );
			model.addAttribute( "equipe", equipe );
			model.addAttribute( "categorie", categorie );
			model.addAttribute( "ticketRepas", ticketRepas );
			model.addAttribute( "nom1", nom1 );
			model.addAttribute( "prenom1", prenom1 );
			model.addAttribute( "sexe1", sexe1 );
			model.addAttribute( "status1", status1 );
			model.addAttribute( "naissance1", naissance1 );
			model.addAttribute( "nom2", nom2 );
			model.addAttribute( "prenom2", prenom2 );
			model.addAttribute( "sexe2", sexe2 );
			model.addAttribute( "status2", status2 );
			model.addAttribute( "naissance2", naissance2 );

			// Retourner à la page d'inscription
			return "public/inscription";
		}
	}

	// -------
	// classement()

	@GetMapping( "/classement" )
	public String classement() {
		return "public/classement";
	}

}
