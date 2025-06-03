package boldair.service;

import java.time.LocalTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import boldair.dao.DaoBenevol;
import boldair.dao.DaoCompte;
import boldair.data.Benevol;
import boldair.data.Compte;
import boldair.service.exception.InscriptionException;

@Service
public class ServiceInscriptionBenevol {

    @Autowired
    private DaoBenevol daoBenevol;
    @Autowired
    private DaoCompte daoCompte;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Transactional
    public void inscrireBenevole(
            String nom,
            String prenom,
            String email,
            String telephone,
            String statut,
            String motDePasse,
            LocalTime heureDebutDispo,
            LocalTime heureFinDispo,
            String commentaire,
            Boolean permis,
            Boolean interne
    ) throws InscriptionException {
        // Vérifier si l'email est déjà utilisé
        if (daoBenevol.findByEmail(email) != null || daoCompte.findByEmail(email) != null) {
            throw new InscriptionException("L'email est déjà utilisé");
        }

        // Créer le compte
        Compte compte = new Compte();
        compte.setPseudo(nom + " " + prenom);
        compte.setEmail(email);
        compte.setEmpreinteMdp(passwordEncoder.encode(motDePasse));
        compte.setRoleAdmin(false);
        compte.setRoleBenevol(true);
        compte = daoCompte.save(compte);

        // Créer le bénévole
        Benevol benevol = new Benevol();
        benevol.setNom(nom);
        benevol.setPrénom(prenom);
        benevol.setEmail(email);
        benevol.setTéléphone(telephone);
        benevol.setStatut(statut);
        benevol.setHeureDebutDispo(heureDebutDispo);
        benevol.setHeureFinDispo(heureFinDispo);
        benevol.setCommentaire(commentaire);
        benevol.setPermis(permis);
        benevol.setInterne(interne);
        benevol.setInterneExterne(interne); // ou autre logique selon le sens du champ
        benevol.setIdCompte(compte.getIdCompte());
        daoBenevol.save(benevol);
    }
} 