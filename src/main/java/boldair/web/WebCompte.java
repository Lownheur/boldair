package boldair.web;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
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

import boldair.dao.DaoCompte;
import boldair.data.Compte;
import boldair.util.Alert;
import boldair.util.Paging;
import jakarta.annotation.security.RolesAllowed;
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
@RolesAllowed( "ADMIN" )
@RequestMapping( "/compte" )
@SessionAttributes( "pagingCompte" )
public class WebCompte {

	// -------
	// Composants injectés
	// -------

	private final DaoCompte			daoCompte;
	private final PasswordEncoder	encoder;

	// -------
	// Attributs de session
	// -------

	@ModelAttribute
	public Paging getPaging( @ModelAttribute( "pagingCompte" ) Paging paging ) {
		return paging;
	}

	// -------
	// Endpoints
	// -------

	// -------
	// listContent()

	@PostMapping( "/list/content" )
	public String getListContent( Paging paging, Model model ) {

		var page = getPage( paging );

		// Si la n° de page demandé est > au nombre total, on affiche la dernière page 
		if ( paging.getPageNum() > page.getTotalPages() && page.getTotalPages() > 0 ) {
			paging.setPageNum( page.getTotalPages() );
			page = getPage( paging );
		}

		model.addAttribute( "list", page.getContent() );
		model.addAttribute( "totalItems", page.getTotalElements() );
		model.addAttribute( "totalPages", page.getTotalPages() );
		return "compte/list :: #dynamic_view";

	}

	// -------
	// list() - GET

	@GetMapping( "/list" )
	public String list( Paging paging, Model model ) {

		getListContent( paging, model );
		return "compte/list";

	}

	// -------
	// list() - POST

	@PostMapping( "/list" )
	public String list() {
		return "redirect:/compte/list";
	}

	// -------
	// edit()

	@GetMapping( path = "/form" )
	public String edit( Long id, Model model ) {

		Compte item;
		if ( id == null ) {
			item = new Compte();
		} else {
			item = daoCompte.findById( id ).get();
		}

		model.addAttribute( "item", item );
		return "compte/form";

	}

	// -------
	// save()

	@PostMapping( "/form" )
	public String save(
			@ModelAttribute( "item" ) Compte item,
			BindingResult result,
			RedirectAttributes ra ) {

		if ( !daoCompte.verifierUnicitePseudo( item.getPseudo(), item.getIdCompte() ) ) {
			result.rejectValue( "pseudo", "", "Ce pseudo est déjà utilisé" );
		}
		if ( !daoCompte.verifierUniciteEmail( item.getEmail(), item.getIdCompte() ) ) {
			result.rejectValue( "email", "", "Cet e-mail  est déjà utilisé" );
		}
		if ( result.hasErrors() ) {
			return "compte/form";
		}

		if ( !item.getMotDePasse().isBlank() ) {
			item.setEmpreinteMdp( encoder.encode( item.getMotDePasse() ) );
		}
		daoCompte.save( item );

		ra.addFlashAttribute( "alert", new Alert( Alert.Color.SUCCESS, "Mise à jour effectuée avec succès" ) );
		return "redirect:/compte/list";

	}

	// -------
	// listContent()

	@PostMapping( "/delete" )
	public String delete( Long id, Paging paging, Model model ) {

		daoCompte.deleteById( id );
		model.addAttribute( "alert", new Alert( Alert.Color.SUCCESS, "Suppression effectuée avec succès" ) );
		return getListContent( paging, model );

	}

	// -------
	// Méthodes auxiliaires
	// ------

	private Page<Compte> getPage( Paging paging ) {

		var pageable = PageRequest.of( paging.getPageNum() - 1, paging.getPageSize() );

		Page<Compte> page;
		if ( paging.getSearch() == null ) {
			page = daoCompte.findAllByOrderByPseudo( pageable );
		} else {
			page = daoCompte.findByPseudoContainingIgnoreCaseOrEmailContainingIgnoreCaseOrderByPseudo(
					paging.getSearch(), paging.getSearch(), pageable );
		}

		return page;

	}
}
