package com.manda.agenda.controller;

import java.security.Principal;
import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.manda.agenda.dto.UserDTO;
import com.manda.agenda.services.UserService;
import com.manda.agenda.utilitaires.PasswordEncryptionService;

import jakarta.validation.Valid;

@Controller
@RequestMapping("/admin")
public class UserController {

    @Autowired
    UserService userService;

    @GetMapping("/newUser")
    public String returnForm(Model model, Principal principal) {
        model.addAttribute("username", principal.getName());
        model.addAttribute("prenom", userService.getPrenomUser());
        return "newUser";
    }

    @GetMapping("/listUser")
    public String listUser(Model model, Principal principal) {
        model.addAttribute("users", userService.getAllUsers());
        model.addAttribute("username", principal.getName());
        model.addAttribute("prenom", userService.getPrenomUser());
        return "listUser";
    }

    @PostMapping("/saveUser")
    public String enregistrer(@Valid @ModelAttribute UserDTO userDTO, @RequestParam("password2") String password2,
            Model model, Principal principal) {
        System.out.println("Utilisateur connecté......" + principal.getName());
        System.out.println("Last name......" + userDTO.getLastName());
        System.out.println("Id......" + userDTO.getId());
        if (userDTO.getPassword().equals(password2)) {
            userDTO.setWhoCreated(principal.getName());
            userDTO.setPassword(new PasswordEncryptionService().encrypPassword(userDTO.getPassword()));
            userDTO.setDateCreated(LocalDate.now());
            userDTO.setFirstConnection("Oui");
            userService.saveUser(userDTO);
            model.addAttribute("users", userService.getAllUsers());
            model.addAttribute("username", principal.getName());
            model.addAttribute("prenom", userService.getPrenomUser());
            return "listUser";
        } else {
            model.addAttribute("message", "Les deux mots de passe ne correspondent pas. Veuillez réessayer.");
            model.addAttribute("username", principal.getName());
            model.addAttribute("prenom", userService.getPrenomUser());
            return "newUser";
        }

    }

    @PostMapping("/formModifier")
    public String formModifier(@RequestParam("id") Integer id, Model model, Principal principal) {

        model.addAttribute("user", userService.getUser(id));
        model.addAttribute("optionTypeUser", List.of("ADMIN", "USER"));
        model.addAttribute("username", principal.getName());
        model.addAttribute("prenom", userService.getPrenomUser());
        return "modifierUser";
    }

    @PostMapping("/modifierUser")
    public String modifier(@Valid @ModelAttribute UserDTO userDTO, @RequestParam("password2") String password2,
            Model model, Principal principal) {
        System.out.println("Utilisateur connecté......" + principal.getName());
        System.out.println("Last name......" + userDTO.getLastName());
        if (userDTO.getPassword().equals(password2)) {
            userDTO.setWhoModified(principal.getName());
            userDTO.setPassword(new PasswordEncryptionService().encrypPassword(userDTO.getPassword()));
            userDTO.setDateModified(LocalDate.now());
            userDTO.setFirstConnection("Oui");
            userService.modifierUser(userDTO);
            model.addAttribute("users", userService.getAllUsers());
            model.addAttribute("username", principal.getName());
            model.addAttribute("prenom", userService.getPrenomUser());
            return "listUser";
        } else {
            model.addAttribute("message", "Les deux mots de passe entrés ne sont pas identiques");
            model.addAttribute("username", principal.getName());
            model.addAttribute("prenom", userService.getPrenomUser());
            return "modifierUser";
        }

    }

}
