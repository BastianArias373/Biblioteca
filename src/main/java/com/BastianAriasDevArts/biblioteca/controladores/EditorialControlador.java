package com.BastianAriasDevArts.biblioteca.controladores;

import com.BastianAriasDevArts.biblioteca.entidades.Editorial;
import com.BastianAriasDevArts.biblioteca.excepciones.MiException;
import com.BastianAriasDevArts.biblioteca.servicios.EditorialServicio;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/editorial")
public class EditorialControlador {
    
    @Autowired
    private EditorialServicio editorialServicio;
    
    @GetMapping("/registrar")                      //localhost:8080/editorial/registrar
    public String vistaRegistrarEditorial(){     //metodo que me larga el html a pantalla
        return "editorial_form.html";
    }
    
    @PostMapping("/registro")                      //localhost:8080/editorial/registro
    public String registroEditorial(@RequestParam() String nombre, ModelMap modelo){
        try {
            editorialServicio.crearEditorial(nombre);
            modelo.put("exitoEditorial", "La Editorial fue cargado correctamente");
        } catch (MiException ex) {
            modelo.put("errorEditorial", ex.getMessage());
            return "editorial_form.html";
        }
        return "/index.html";
        
    }
    
    @GetMapping("/lista")                   //localhost:8080/editorial/lista
    public String vistaListaEditoriales(ModelMap modelo){
        List<Editorial> editoriales = editorialServicio.listarEditoriales();
        modelo.addAttribute("editoriales",editoriales);
        return "editorial_lista.html";
    }
    
    @GetMapping("/modificar/{id}")
    public String vistaModificarEditorial(@PathVariable String id, ModelMap modelo){
        modelo.put("editorial",editorialServicio.getOne(id));
        return "editorial_modificar.html";
    }
    
    @PostMapping("/modificar/{id}")
    public String modificarEditorial(
            @PathVariable String id, 
            String nombre, 
            ModelMap modelo){
        try {
            editorialServicio.modificarEditorial(id, nombre);
        } catch (MiException ex) {
            modelo.put("error", ex.getMessage());
            modelo.put("editorial",editorialServicio.getOne(id));
            return "editorial_modificar.html";
        }
        
        return "redirect:../lista";
    }
    
}