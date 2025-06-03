package boldair.web;

import boldair.service.ServiceInscriptionBenevol;
import boldair.service.exception.InscriptionException;
import boldair.util.Alert;
import boldair.util.Alert.Color;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalTime;

@Controller
@RequestMapping("/inscription-benevole")
public class WebInscriptionBenevol {

    @Autowired
    private ServiceInscriptionBenevol serviceInscriptionBenevol;

    @GetMapping
    public String afficherFormulaire() {
        // On peut renvoyer vers la page d'inscription existante ou une page dédiée
        return "public/inscription";
    }

    @PostMapping
    public String inscrireBenevole(
            @RequestParam("nom") String nom,
            @RequestParam("prenom") String prenom,
            @RequestParam("email") String email,
            @RequestParam("tel-benevole") String telephone,
            @RequestParam("statut") String statut,
            @RequestParam("password-benevole") String motDePasse,
            @RequestParam("heure_debut_dispo") @DateTimeFormat(iso = DateTimeFormat.ISO.TIME) LocalTime heureDebutDispo,
            @RequestParam("heure_fin_dispo") @DateTimeFormat(iso = DateTimeFormat.ISO.TIME) LocalTime heureFinDispo,
            @RequestParam(value = "commentaire", required = false) String commentaire,
            @RequestParam(value = "permis", required = false) Boolean permis,
            @RequestParam(value = "interne", required = false) Boolean interne,
            Model model
    ) {
        try {
            serviceInscriptionBenevol.inscrireBenevole(
                    nom,
                    prenom,
                    email,
                    telephone,
                    statut,
                    motDePasse,
                    heureDebutDispo,
                    heureFinDispo,
                    commentaire,
                    permis != null && permis,
                    interne != null && interne
            );
            model.addAttribute("alert", new Alert(Color.SUCCESS, "Inscription bénévole réussie !"));
            return "public/inscription";
        } catch (InscriptionException e) {
            model.addAttribute("alert", new Alert(Color.DANGER, e.getMessage()));
            return "public/inscription";
        }
    }
} 