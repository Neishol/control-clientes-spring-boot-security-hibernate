package com.example.HolaSpring_1.web;

import com.example.HolaSpring_1.dao.PersonaDao;
import com.example.HolaSpring_1.domain.Persona;
import com.example.HolaSpring_1.servicio.PersonaService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@Slf4j
public class ControladorRest {
    
    @Autowired
    private PersonaService personaService;
    
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    @GetMapping("/")
    public String inicio(Model model, @AuthenticationPrincipal User user){
        var personas = personaService.listarPersonas();
                
        log.info("ejecutando el controlador Spring MVC");
        log.info("Usuario que hizo login: " + user);
        model.addAttribute("personas", personas);
        
        return "index";
    }
    
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/agregar")
    public String agregar(Persona persona/*Inyecta el objeto y no es necesario instanciarlo en el metodo */){
        return "modificar";
    }
    
    @PostMapping("/guardar")
    public String guardar(@Valid Persona persona, Errors errores){
        if(errores.hasErrors()){
            return "modificar";
        }
        personaService.guardar(persona);
        return "redirect:/";
    }
    
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/editar/{idPersona}")
    public String editar(Persona persona, Model model){
        persona = personaService.encontrarPersona(persona);
        model.addAttribute("persona", persona);
        return "modificar";
    }
    
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/eliminar")
    public String eliminar(Persona persona){
//        persona = personaService.encontrarPersona(persona);
        personaService.eliminar(persona);
        return "redirect:/";
    }
       
    @RequestMapping("/login")
    public String login(){
        return "login";
    }
   
}
