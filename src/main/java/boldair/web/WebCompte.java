package boldair.web;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import boldair.dao.DaoBenevol;
import boldair.dao.DaoCompte;
import boldair.dao.DaoParticipant;
import boldair.data.Benevol;
import boldair.data.Compte;
import boldair.data.Equipe;
import boldair.data.Participant;
import boldair.service.ServiceAdmin;
import boldair.util.Alert;
import boldair.util.Paging;
import jakarta.annotation.security.RolesAllowed;
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
@RequestMapping("/compte")
@SessionAttributes("pagingCompte")
public class WebCompte {

	// -------
	// Composants injectés
	// -------

	private final DaoCompte daoCompte;
	private final DaoBenevol daoBenevol;
	private final DaoParticipant daoParticipant;
	private final PasswordEncoder encoder;
	private final ServiceAdmin serviceAdmin;

	// -------
	// Attributs de session
	// -------

	@ModelAttribute
	public Paging getPaging(@ModelAttribute("pagingCompte") Paging paging) {
		return paging;
	}

	// -------
	// User and Admin Dashboards
	// -------

	@GetMapping("/utilisateur")
	@RolesAllowed("USER")
	public String userDashboard() {
		return "compte/utilisateur";
	}

	@GetMapping("/admin")
	@RolesAllowed("ADMIN")
	public String adminDashboard(Model model) {
		// Add statistics to the model
		model.addAttribute("volunteerCount", serviceAdmin.getVolunteerCount());
		model.addAttribute("teamCount", serviceAdmin.getTeamCount());
		return "compte/admin";
	}
	
	@GetMapping("/admin/benevoles")
	@RolesAllowed("ADMIN")
	public String adminVolunteers(Model model) {
		List<Benevol> volunteers = serviceAdmin.getAllVolunteers();
		model.addAttribute("volunteers", volunteers);
		model.addAttribute("volunteerCount", volunteers.size());
		return "compte/admin-benevoles";
	}
	
	@GetMapping("/admin/equipes")
	@RolesAllowed("ADMIN")
	public String adminTeams(Model model) {
		List<Equipe> teams = serviceAdmin.getAllTeams();
		model.addAttribute("teams", teams);
		model.addAttribute("teamCount", teams.size());
		
		// Also get participants for each team
		for (Equipe team : teams) {
			List<Participant> participants = serviceAdmin.getTeamParticipants(team.getIdEquipe());
			// Add participants to a map or handle them in template
		}
		
		return "compte/admin-equipes";
	}
	
	@GetMapping("/benevol")
	@RolesAllowed("BENEVOL")
	public String benevolDashboard(Model model) {
		// Get the current authenticated user
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		String username = auth.getName();
		
		// Find the compte by username (pseudo)
		Compte compte = daoCompte.findByPseudo(username);
		
		// Find the benevol associated with the compte
		Benevol benevol = daoBenevol.findByCompteId(compte.getIdCompte());
		
		// Add benevol to the model to use in the view
		model.addAttribute("benevol", benevol);
		model.addAttribute("compte", compte);
		
		return "compte/benevol";
	}

	// -------
	// Endpoints
	// -------

	// -------
	// listContent()
	// -------

	@GetMapping("/list")
	@RolesAllowed("ADMIN")
	public String listContent(Model model, @ModelAttribute("pagingCompte") Paging paging) {

		// -------
		// Génération des données
		// -------

		Page<Compte> pageCompte = daoCompte.findAll(PageRequest.of(paging.getPageNo(), paging.getPageSize()));

		// -------
		// Enrichissement du modèle
		// -------

		model.addAttribute("pageCompte", pageCompte);

		return "compte/list";
	}

	// -------
	// newContent()
	// -------

	@GetMapping("/new")
	@RolesAllowed("ADMIN")
	public String newContent(Model model) {

		// -------
		// Enrichissement du modèle
		// -------

		model.addAttribute("compte", new Compte());

		return "compte/new";
	}

	// -------
	// saveContent()
	// -------

	@PostMapping("/save")
	@RolesAllowed("ADMIN")
	public String saveContent(Model model, @ModelAttribute Compte compte, BindingResult bindingResult,
			RedirectAttributes redirectAttributes) {

		// -------
		// Contrôles
		// -------

		if (bindingResult.hasErrors()) {
			return "compte/new";
		}

		// -------
		// Encodage du mot de passe
		// -------

		compte.setEmpreinteMdp(encoder.encode(compte.getEmpreinteMdp()));

		// -------
		// Sauvegarde
		// -------

		daoCompte.save(compte);

		// -------
		// Redirection
		// -------

		redirectAttributes.addFlashAttribute("alert", new Alert("Compte créé avec succès", Alert.Color.SUCCESS));

		return "redirect:/compte/list";
	}

	// -------
	// deleteContent()
	// -------

	@PostMapping("/delete")
	@RolesAllowed("ADMIN")
	public String deleteContent(@ModelAttribute Compte compte, RedirectAttributes redirectAttributes) {

		// -------
		// Suppression
		// -------

		daoCompte.delete(compte);

		// -------
		// Redirection
		// -------

		redirectAttributes.addFlashAttribute("alert", new Alert("Compte supprimé", Alert.Color.WARNING));

		return "redirect:/compte/list";
	}
}
