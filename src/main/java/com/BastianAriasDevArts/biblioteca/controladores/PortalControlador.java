package com.BastianAriasDevArts.biblioteca.controladores;


import com.BastianAriasDevArts.biblioteca.entidades.Usuario;
import com.BastianAriasDevArts.biblioteca.excepciones.MiException;
import com.BastianAriasDevArts.biblioteca.servicios.UsuarioServicio;
import javax.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

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
            MultipartFile archivo,
            ModelMap modelo){
        
        try {
            //usamos el servicio del usuario para registrar
            usuarioServicio.registrar(nombre, email, password, password2, archivo);
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
    public String inicio(HttpSession session){
        
        //traemos al usuario con los datos de la sesion
        Usuario logueado = (Usuario) session.getAttribute("usuariosession");
        
        //comparamos si este tiene el role ADMIN
        if (logueado.getRol().toString().equals("ADMIN")) {
            return "redirect:/admin/dashboard";         //redirect para ADMINs
        }
        
        return "inicio.html";       //si no es admin va al inicio para USERs
    }
    
    @PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_ADMIN')")
    @GetMapping("/perfil")
    public String perfil (ModelMap modelo, HttpSession session){
        Usuario usuario = (Usuario) session.getAttribute("usuariosession");
        modelo.put("usuario", usuario);
        return "usuario_modificar.html";
    }
    
    @PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_ADMIN')")
    @PostMapping("/perfil/{id}")
    public String actualizar(
            @PathVariable String id,
            @RequestParam String nombre,
            @RequestParam String email,
            @RequestParam String password,
            @RequestParam String password2,
            MultipartFile archivo,
            ModelMap modelo) {

        try {
            System.out.println("###"+archivo.getOriginalFilename()+"###");
            usuarioServicio.actualizar(id, nombre, email, password, password2, archivo);

            modelo.put("exito", "Usuario actualizado correctamente!");

            return "inicio.html";
        } catch (MiException ex) {

            modelo.put("error", ex.getMessage());
            modelo.put("nombre", nombre);
            modelo.put("email", email);

            return "usuario_modificar.html";

        }
    }
     
}
