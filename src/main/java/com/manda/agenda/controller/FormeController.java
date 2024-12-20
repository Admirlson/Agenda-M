package com.manda.agenda.controller;

import java.security.Principal;
import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.manda.agenda.dto.EvenementDTO;
import com.manda.agenda.services.EvenementService;
import com.manda.agenda.services.UserService;
import com.manda.agenda.utilitaires.PasswordEncryptionService;

@Controller
public class FormeController {

    // private List<EvenementDTO> evenements = new ArrayList<EvenementDTO>();

    @Autowired
    EvenementService evenementService;

    @Autowired
    UserService userService;

    @GetMapping("/")
    String login() {
        return "login";
    }

    @GetMapping("/listeEvenement")
    String listeEvenement(Model model, Principal principal) {
        System.out.println("Prenom de l'utilisateur====================" + userService.getPrenomUser());
        model.addAttribute("evenements", evenementService.listEvenementDTOs());
        model.addAttribute("username", principal.getName());
        model.addAttribute("prenom", userService.getPrenomUser());
        return "listeEvenement";
    }

    @PostMapping("/annuler")
    String annuler(Model model, Principal principal) {
        model.addAttribute("evenements", evenementService.listEvenementDTOs());
        model.addAttribute("username", principal.getName());
        model.addAttribute("prenom", userService.getPrenomUser());
        return "listeEvenement";
    }

    @GetMapping("/formAgenda")
    String formeAgenda(Model model, Principal principal) {
        model.addAttribute("username", principal.getName());
        model.addAttribute("prenom", userService.getPrenomUser());
        return "formAgenda";
    }

    @GetMapping("/rechercherEvenement")
    String rechercherEvenement(@RequestParam("id") int id, Model model, Principal principal) {
        model.addAttribute("evenement", evenementService.getEvenement(id));
        model.addAttribute("evenements", evenementService.listEvenementDTOs());
        model.addAttribute("optionFormat", List.of("En ligne", "Présentiel"));
        model.addAttribute("optionType", List.of("Interne", "Externe"));
        model.addAttribute("username", principal.getName());
        model.addAttribute("prenom", userService.getPrenomUser());
        return "modifierEvenement";
    }

    @PostMapping("/user/saveEvenement")
    String saveEvenement(@ModelAttribute EvenementDTO evenement, Model model, Principal principal) {

        try {
            System.out.println("null==========================");
            evenement.setStatut("Planifié");
            evenement.setWhocreated("admirl");
            evenement.setDatecreated(LocalDate.now());
            evenementService.enregistrer(evenement);
            model.addAttribute("evenements", evenementService.listEvenementDTOs());
            model.addAttribute("username", principal.getName());
            model.addAttribute("prenom", userService.getPrenomUser());

        } catch (Exception e) {
            e.printStackTrace();
        }
        model.addAttribute("evenements", evenementService.listEvenementDTOs());

        return "listeEvenement";
    }

    @PostMapping("/user/modifierEvenement")
    String modifierEvenement(@ModelAttribute EvenementDTO evenement, Model model, Principal principal) {
        evenement.setStatut("Reporté");
        evenement.setWhomodified("admirl");
        evenement.setDatemodified(LocalDate.now());
        evenementService.modifier(evenement);
        model.addAttribute("username", principal.getName());
        model.addAttribute("prenom", userService.getPrenomUser());
        model.addAttribute("evenements", evenementService.listEvenementDTOs());
        return "redirect:/listeEvenement";
    }

    @PostMapping("/user/annulerEvenement")
    String anuulerEvenement(@RequestParam("id") int id, Model model, Principal principal) {
        EvenementDTO evenement = evenementService.getEvenement(id);
        System.out.println("Id de l'evenement===========" + evenement.getId());
        System.out.println("Institution==========" + evenement.getInstitution());
        evenement.setStatut("Annulé");
        evenement.setWhomodified("admirl");
        evenement.setDatemodified(LocalDate.now());
        evenementService.modifier(evenement);
        model.addAttribute("username", principal.getName());
        model.addAttribute("prenom", userService.getPrenomUser());
        model.addAttribute("evenements", evenementService.listEvenementDTOs());
        return "redirect:/listeEvenement";
    }

    @PostMapping("/user/terminerEvenement")
    String terminerEvenement(@RequestParam("id") int id, @RequestParam("statut") String statut, Model model,
            Principal principal) {
        if (statut.equals("Terminé")) {
            model.addAttribute("modalVisible", "oui");
            // System.out.println("Id de l'evenement===========" + id);
            // System.out.println("Statut==========" + statut);
            model.addAttribute("evenementId", id);
            model.addAttribute("evenement", evenementService.listEvenementDTOs());
            model.addAttribute("username", principal.getName());

        }
        model.addAttribute("prenom", userService.getPrenomUser());
        return "listeEvenement";
    }

    @PostMapping("/user/validerTerminerEvenement")
    String validerTerminerEvenement(@RequestParam("id") int id, @RequestParam("suivis") String suivis, Model model,
            Principal principal) {
        System.out.println("Id de l'evenement===========" + id);
        EvenementDTO evenement = evenementService.getEvenement(id);
        evenement.setStatut("Terminé");
        evenement.setSuivis(suivis);
        evenement.setWhomodified("admirl");
        evenement.setDatemodified(LocalDate.now());
        evenementService.modifier(evenement);
        model.addAttribute("username", principal.getName());
        model.addAttribute("prenom", userService.getPrenomUser());
        model.addAttribute("evenements", evenementService.listEvenementDTOs());
        return "redirect:/listeEvenement";
    }

    /**
     * @return
     */
    @GetMapping("/user/generatPdf")
    public ResponseEntity<byte[]> generatePdf() {
        byte[] liste = null;
        try {

            // List<EvenementDTO> list = evenementService.listEvenementDTOs(evenements);
            // if (list != null) {
            // for (EvenementDTO evenementDTO : list) {
            // System.out.println(evenementDTO.getDate());
            // System.out.println(evenementDTO.getHeure());
            // }
            // } else {
            // System.out.println("List est nulle");
            // }
            liste = evenementService.listeEvenement(evenementService.listEvenementDTOs());
            // evenementService.sendReminders(evenementService.listEvenementDTOs());
            System.out.println("Apres sendReminders");
            if (liste != null) {
                System.out.println("Liste n'est pas nulle");
            }

        } catch (Exception ex) {
        }
        return ResponseEntity.ok().contentType(MediaType.APPLICATION_PDF)
                .header("Content-Disposition", "inline; filename=raport.pdf").body(liste);
    }

    @GetMapping("/user/generatCsv")
    public ResponseEntity<byte[]> generateCsv() {
        byte[] liste = null;
        try {
            liste = evenementService.generateCsv(evenementService.listEvenementDTOs());
            if (liste != null) {
                System.out.println("Liste n'est pas nulle");
            }

        } catch (Exception ex) {
        }
        // return ResponseEntity.ok().contentType(MediaType.APPLICATION_PDF)
        // .header("Content-Disposition", "inline; filename=raport.pdf").body(liste);

        return ResponseEntity.ok().contentType(MediaType.TEXT_PLAIN)
                .header("Content-Disposition", "attachment; filename=raport.csv").body(liste);
    }

    @PostMapping("/user/formModifierPassword")
    public String formModiierPassword(@RequestParam("username") String username, Model model, Principal principal) {
        model.addAttribute("username", username);
        model.addAttribute("username", principal.getName());
        model.addAttribute("prenom", userService.getPrenomUser());
        model.addAttribute("first", "Non");
        return "modifierPassword";
    }

    @PostMapping("/user/modifierPassword")
    public String modifierPasswordUser(@RequestParam("password") String password,
            @RequestParam("password2") String password2,
            @RequestParam("username") String username, Model model, Principal principal) {
        if (password.equals(password2)) {
            userService.modifierPassword(new PasswordEncryptionService().encrypPassword(password), username);
            return "login";
        } else {
            model.addAttribute("message", "Les deux mots de passe entrés ne sont pas identiques");
            model.addAttribute("username", principal.getName());
            model.addAttribute("prenom", userService.getPrenomUser());
            return "modifierPassword";
        }

    }

    @GetMapping("/first/formModifierPassword")
    public String formModiierPasswordFirstConnection(Model model,
            Principal principal) {
        model.addAttribute("username", principal.getName());
        model.addAttribute("prenom", userService.getPrenomUser());
        model.addAttribute("first", "Oui");
        return "modifierPasswordFirstConnection";
    }

    @PostMapping("/first/modifierPassword")
    public String modifierPasswordUserFirstConnection(@RequestParam("password") String password,
            @RequestParam("password2") String password2,
            @RequestParam("username") String username, Model model, Principal principal) {

        if (password.equals(password2)) {
            userService.modifierPasswordPourPremiereConnexion(new PasswordEncryptionService().encrypPassword(password),
                    "Non", username);

            return "login";
        } else {
            model.addAttribute("message", "Les deux mots de passe entrés ne sont pas identiques");
            model.addAttribute("username", principal.getName());
            model.addAttribute("prenom", userService.getPrenomUser());
            System.out.println("username====================11111111===" + username);
            return "modifierPasswordFirstConnection";
        }

    }

    @GetMapping("/user/evenement/planifer")
    public String evenementPlanifie(Model model, Principal principal) {
        System.out.println("Prenom de l'utilisateur====================" + userService.getPrenomUser());
        model.addAttribute("evenements", evenementService.listEvenementDTOParStatut("Planifié"));
        model.addAttribute("username", principal.getName());
        model.addAttribute("prenom", userService.getPrenomUser());
        return "listeEvenementPlanifie";
    }

    @GetMapping("/user/evenement/reporter")
    public String evenementReporte(Model model, Principal principal) {
        System.out.println("Prenom de l'utilisateur====================" + userService.getPrenomUser());
        model.addAttribute("evenements", evenementService.listEvenementDTOParStatut("Reporté"));
        model.addAttribute("username", principal.getName());
        model.addAttribute("prenom", userService.getPrenomUser());
        return "listeEvenementReporte";
    }

    @GetMapping("/user/evenement/annuler")
    public String evenementAnnule(Model model, Principal principal) {
        System.out.println("Prenom de l'utilisateur====================" + userService.getPrenomUser());
        model.addAttribute("evenements", evenementService.listEvenementDTOParStatut("Annulé"));
        model.addAttribute("username", principal.getName());
        model.addAttribute("prenom", userService.getPrenomUser());
        return "listeEvenementAnnule";
    }

    @GetMapping("/user/generatPdfParStatut")
    public ResponseEntity<byte[]> generatePdfParStatut(@RequestParam("statut") String statut) {
        byte[] liste = null;
        try {

            liste = evenementService.listeEvenement(evenementService.listEvenementDTOParStatut(statut));
            // evenementService.sendReminders(evenementService.listEvenementDTOs());
            System.out.println("Apres sendReminders");
            if (liste != null) {
                System.out.println("Liste n'est pas nulle");
            }

        } catch (Exception ex) {
        }
        return ResponseEntity.ok().contentType(MediaType.APPLICATION_PDF)
                .header("Content-Disposition", "inline; filename=raport.pdf").body(liste);
    }

    @GetMapping("/user/generatCsvParStatut")
    public ResponseEntity<byte[]> generateCsvParStatut(@RequestParam("statut") String statut) {
        byte[] liste = null;
        try {
            liste = evenementService.generateCsv(evenementService.listEvenementDTOParStatut(statut));
            if (liste != null) {
                System.out.println("Liste n'est pas nulle");
            }

        } catch (Exception ex) {
        }
        // return ResponseEntity.ok().contentType(MediaType.APPLICATION_PDF)
        // .header("Content-Disposition", "inline; filename=raport.pdf").body(liste);

        return ResponseEntity.ok().contentType(MediaType.TEXT_PLAIN)
                .header("Content-Disposition", "attachment; filename=raport.csv").body(liste);
    }
}
