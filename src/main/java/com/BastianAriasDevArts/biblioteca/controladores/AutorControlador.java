package com.BastianAriasDevArts.biblioteca.controladores;

import com.BastianAriasDevArts.biblioteca.entidades.Autor;
import com.BastianAriasDevArts.biblioteca.excepciones.MiException;
import com.BastianAriasDevArts.biblioteca.servicios.AutorServicio;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/autor")
public class AutorControlador {
    
    @Autowired
    private AutorServicio autorServicio;
    
    @GetMapping("/registrar")               //localhost:8080/autor/registar
    public String vistaRegistrarAutor(){    //metodo que me larga el html a pantalla
        return "autor_form.html";
    }
    
    @PostMapping("/registro")               //localhost:8080/autor/registro
    public String registroAutor(@RequestParam() String nombre, ModelMap modelo){
        try {
            autorServicio.crearAutor(nombre);
            modelo.put("exitoAutor", "El Autor fue creado con exito");
        } catch (MiException ex) {
            modelo.put("errorAutor", ex.getMessage());
            return "autor_form.html";
        }
        return "/index.html";
    }
    
    @GetMapping("/lista")                   //localhost:8080/autor/lista
    public String vistaListaAutores(ModelMap modelo){
        List<Autor> autores = autorServicio.listarAutores();
        modelo.addAttribute("autores",autores);
        return "autor_lista.html";
    }
    
    @GetMapping("/modificar/{id}")
    public String vistaModificarAutor(@PathVariable String id, ModelMap modelo){
        modelo.put("autor",autorServicio.getOne(id));
        return "autor_modificar.html";
    }
    
    @PostMapping("/modificar/{id}")
    public String modificarAutor(
            @PathVariable String id, 
            String nombre, 
            ModelMap modelo){
        try {
            autorServicio.modificarAutor(id, nombre);
        } catch (MiException ex) {
            modelo.put("error", ex.getMessage());
            modelo.put("autor",autorServicio.getOne(id));
            return "autor_modificar.html";
        }
        
        return "redirect:../lista";
    }
}
