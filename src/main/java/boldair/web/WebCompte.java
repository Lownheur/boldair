package boldair.web;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import boldair.dao.DaoBenevol;
import boldair.dao.DaoCompte;
import boldair.dao.DaoRole;
import boldair.data.Benevol;
import boldair.data.Compte;
import boldair.data.Equipe;
import boldair.data.Role;
import boldair.data.RoleAvecAffectation;
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
	private final DaoRole daoRole;
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
	public String userDashboard(Model model) {
		// Get the current authenticated user
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		String username = auth.getName();
		
		// Find the compte by username (pseudo)
		Compte compte = daoCompte.findByPseudo(username);
		
		// Find the equipe by searching participants with the same email as the compte
		Equipe equipe = null;
		List<boldair.data.Participant> participants = null;
		
		if (compte != null) {
			// Get participants by email to find the team
			participants = serviceAdmin.getParticipantsByEmail(compte.getEmail());
			if (participants != null && !participants.isEmpty()) {
				// Get the team of the first participant (they should all be in the same team)
				Long teamId = participants.get(0).getIdEquipe();
				equipe = serviceAdmin.getTeamById(teamId);
			}
		}
		
		// Add data to the model
		model.addAttribute("compte", compte);
		model.addAttribute("equipe", equipe);
		model.addAttribute("participants", participants);
		
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
		List<RoleAvecAffectation> rolesAvecAffectation = serviceAdmin.getAllRolesWithAffectation();
		List<Role> allRoles = serviceAdmin.getAllRoles();
		
		System.out.println("Nombre de bénévoles: " + volunteers.size());
		System.out.println("Nombre de rôles: " + allRoles.size());
		System.out.println("Nombre de rôles avec affectation: " + rolesAvecAffectation.size());
		
		if (!rolesAvecAffectation.isEmpty()) {
			System.out.println("Premier rôle: " + rolesAvecAffectation.get(0).getRole().getNomRole());
			System.out.println("Quantité requise: " + rolesAvecAffectation.get(0).getQuantiteRequise());
			System.out.println("Quantité affectée: " + rolesAvecAffectation.get(0).getQuantiteAffectee());
			System.out.println("Quantité restante: " + rolesAvecAffectation.get(0).getQuantiteRestante());
		}
				// Create a map of volunteer roles for easy access in template
		Map<Long, Role> volunteerRoles = new HashMap<>();
		for (Benevol volunteer : volunteers) {
			if (volunteer.getIdRole() != null) {
				// Use the role directly from the database to ensure fresh data
				Role role = daoRole.findById(volunteer.getIdRole()).orElse(null);
				if (role != null) {
					volunteerRoles.put(volunteer.getIdBenevol(), role);
					System.out.println("Bénévole " + volunteer.getIdBenevol() + " a le rôle: " + role.getNomRole());
				}
			} else {
				System.out.println("Bénévole " + volunteer.getIdBenevol() + " n'a pas de rôle");
			}
		}
		
		model.addAttribute("volunteers", volunteers);
		model.addAttribute("volunteerCount", volunteers.size());
		model.addAttribute("rolesAvecAffectation", rolesAvecAffectation);
		model.addAttribute("allRoles", allRoles);
		model.addAttribute("volunteerRoles", volunteerRoles);
		model.addAttribute("totalPostesRestants", serviceAdmin.getTotalPostesRestants());
		
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
			serviceAdmin.getTeamParticipants(team.getIdEquipe());
			// Add participants to a map or handle them in template
		}
		
		return "compte/admin-equipes";
	}
	
	// -------
	// Export endpoints
	// -------
	
	@GetMapping("/admin/benevoles/export")
	@RolesAllowed("ADMIN")
	public String exportBenevoles(Model model) {
		List<Benevol> volunteers = serviceAdmin.getAllVolunteers();
		List<Role> allRoles = serviceAdmin.getAllRoles();
		
		// Create a map of volunteer roles for easy access in template
		Map<Long, Role> volunteerRoles = new HashMap<>();
		for (Benevol volunteer : volunteers) {
			if (volunteer.getIdRole() != null) {
				Role role = daoRole.findById(volunteer.getIdRole()).orElse(null);
				if (role != null) {
					volunteerRoles.put(volunteer.getIdBenevol(), role);
				}
			}
		}
		
		model.addAttribute("volunteers", volunteers);
		model.addAttribute("volunteerCount", volunteers.size());
		model.addAttribute("volunteerRoles", volunteerRoles);
		model.addAttribute("exportDate", java.time.LocalDateTime.now());
		
		return "compte/export-benevoles";
	}
	
	@GetMapping("/admin/equipes/export")
	@RolesAllowed("ADMIN")
	public String exportEquipes(Model model) {
		List<Equipe> teams = serviceAdmin.getAllTeams();
		
		// Get participants for each team
		Map<Long, List<boldair.data.Participant>> teamParticipants = new HashMap<>();
		for (Equipe team : teams) {
			List<boldair.data.Participant> participants = serviceAdmin.getTeamParticipants(team.getIdEquipe());
			teamParticipants.put(team.getIdEquipe(), participants);
		}
		
		model.addAttribute("teams", teams);
		model.addAttribute("teamCount", teams.size());
		model.addAttribute("teamParticipants", teamParticipants);
		model.addAttribute("exportDate", java.time.LocalDateTime.now());
		
		return "compte/export-equipes";
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

		Page<Compte> pageCompte = daoCompte.findAll(PageRequest.of(paging.getPageNum(), paging.getPageSize()));

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

		redirectAttributes.addFlashAttribute("alert", new Alert(Alert.Color.SUCCESS, "Compte créé avec succès"));

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

		redirectAttributes.addFlashAttribute("alert", new Alert(Alert.Color.WARNING, "Compte supprimé"));

		return "redirect:/compte/list";
	}
	
	// -------
	// assignRole()
	// -------
	@PostMapping("/assign-role")
	@RolesAllowed("ADMIN")
	public String assignRole(@RequestParam("benevolId") Long benevolId, 
							@RequestParam("roleId") Long roleId, 
							RedirectAttributes redirectAttributes) {
		
		try {
			System.out.println("=== DEBUT WebCompte.assignRole ===");
			System.out.println("benevolId reçu: " + benevolId);
			System.out.println("roleId reçu: " + roleId);
			
			boolean success = serviceAdmin.assignRoleToBenevol(benevolId, roleId);
			
			if (success) {
				redirectAttributes.addFlashAttribute("alert", 
					new Alert(Alert.Color.SUCCESS, "Rôle affecté avec succès"));
				System.out.println("Affectation réussie");
			} else {
				redirectAttributes.addFlashAttribute("alert", 
					new Alert(Alert.Color.DANGER, "Erreur lors de l'affectation du rôle"));
				System.out.println("Affectation échouée");
			}
			
		} catch (Exception e) {
			System.err.println("Exception dans WebCompte.assignRole: " + e.getMessage());
			e.printStackTrace();
			redirectAttributes.addFlashAttribute("alert", 
				new Alert(Alert.Color.DANGER, "Erreur technique lors de l'affectation"));
		}
		
		System.out.println("=== FIN WebCompte.assignRole ===");
		return "redirect:/compte/admin/benevoles";
	}
	
	// -------
	// removeRole()
	// -------
		@PostMapping("/remove-role")
	@RolesAllowed("ADMIN")
	public String removeRole(@RequestParam("benevolId") Long benevolId, 
							@RequestParam("roleId") Long roleId, 
							RedirectAttributes redirectAttributes) {
		
		boolean success = serviceAdmin.removeRoleFromBenevol(benevolId, roleId);
		
		if (success) {
			redirectAttributes.addFlashAttribute("alert", 
				new Alert(Alert.Color.WARNING, "Rôle retiré avec succès"));
		} else {
			redirectAttributes.addFlashAttribute("alert", 
				new Alert(Alert.Color.DANGER, "Erreur lors de la suppression du rôle"));
		}
		
		return "redirect:/compte/admin/benevoles";
	}
	
	// -------	// getBenevolRole() - AJAX endpoint
	// -------
	
	@GetMapping("/benevol/{benevolId}/roles")
	@RolesAllowed("ADMIN")
	@ResponseBody
	public Role getBenevolRole(@PathVariable Long benevolId) {
		return serviceAdmin.getBenevolRole(benevolId);
	}
}
