package com.BastianAriasDevArts.biblioteca.controladores;


import com.BastianAriasDevArts.biblioteca.excepciones.MiException;
import com.BastianAriasDevArts.biblioteca.servicios.UsuarioServicio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/")
public class PortalControlador {
    
    @Autowired
    private UsuarioServicio usuarioServicio;
    
    @GetMapping("/")
    public String Index(){
        return "index.html";
    }
    
    @GetMapping("/registrar")
    public String registrar(){
        return "registro.html";
    }
    
    @PostMapping("/registro")
    public String registro(
            @RequestParam String nombre, 
            @RequestParam String email, 
            @RequestParam String password, 
            @RequestParam String password2, 
            ModelMap modelo){
        
        try {
            //usamos el servicio del usuario para registrar
            usuarioServicio.registrar(nombre, email, password, password2);
            modelo.put("exitoUsuario","Usuario registrado exitosamente");
            return "index.html";
        } catch (MiException ex) {
            modelo.put("nombre", nombre);
            modelo.put("email", email);
            modelo.put("errorUsuario", ex.getMessage());
            return "registro.html";
        }
        
        
    }
    
    @GetMapping("/login")
    public String vistaLogin(
            @RequestParam(required = false) String error, 
            ModelMap modelo){
        if (error != null) {
            modelo.put("errorLogin", "Usuario o contraseña invalidos");
        }
        return "login.html";
    }
    
//    @PostMapping("login")
//    public String login(){
//        return "inicio.thml";
//    }
    
    @PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_ADMIN')") //permisos antes de la peticion GET
    @GetMapping("/inicio")
    public String inicio(){
        return "inicio.html";
    }
}
