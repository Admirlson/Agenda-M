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

@Controller
public class FormeController {

    // private List<EvenementDTO> evenements = new ArrayList<EvenementDTO>();

    @Autowired
    EvenementService evenementService;

    @Autowired
    UserService userService;

    @GetMapping("/login")
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

    @PostMapping("/admin/saveEvenement")
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

    @PostMapping("/admin/modifierEvenement")
    String modifierEvenement(@ModelAttribute EvenementDTO evenement, Model model, Principal principal) {
        evenement.setStatut("Reporté");
        evenement.setWhomodified("admirl");
        evenement.setDatemodified(LocalDate.now());
        evenementService.modifier(evenement);
        model.addAttribute("username", principal.getName());
        model.addAttribute("prenom", userService.getPrenomUser());
        return "redirect:/listeEvenement";
    }

    @PostMapping("/admin/annulerEvenement")
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
        return "redirect:/listeEvenement";
    }

    @PostMapping("/admin/terminerEvenement")
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

    @PostMapping("/admin/validerTerminerEvenement")
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
        return "redirect:/listeEvenement";
    }

    /**
     * @return
     */
    @GetMapping("/admin/generatPdf")
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

    @GetMapping("/admin/generatCsv")
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

}
