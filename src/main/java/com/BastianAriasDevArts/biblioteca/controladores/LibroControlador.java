package com.BastianAriasDevArts.biblioteca.controladores;

import com.BastianAriasDevArts.biblioteca.entidades.Autor;
import com.BastianAriasDevArts.biblioteca.entidades.Editorial;
import com.BastianAriasDevArts.biblioteca.entidades.Libro;
import com.BastianAriasDevArts.biblioteca.excepciones.MiException;
import com.BastianAriasDevArts.biblioteca.servicios.AutorServicio;
import com.BastianAriasDevArts.biblioteca.servicios.EditorialServicio;
import com.BastianAriasDevArts.biblioteca.servicios.LibroServicio;
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
@RequestMapping("/libro")           //localhost:8080/libro
public class LibroControlador {
    
    @Autowired
    LibroServicio libroServicio;
    @Autowired
    AutorServicio autorServicio;
    @Autowired
    EditorialServicio editorialServicio;
    
    @GetMapping("/registrar")       //localhost:8080/libro/registrar
    public String vistaRegistrarLibro(ModelMap modelo){
        
        List<Autor> autores = autorServicio.listarAutores();
        List<Editorial> editoriales = editorialServicio.listarEditoriales();
        
        modelo.addAttribute("autores",autores);
        modelo.addAttribute("editoriales",editoriales);
        
        return "libro_form.html";
    }
    
    @PostMapping("/registro")       //localhost:8080/libro/registro
    public String registroLibro(
            @RequestParam(required = false) Long isbn, 
            @RequestParam() String titulo, 
            @RequestParam(required = false) Integer ejemplares,
            @RequestParam() String idAutor, 
            @RequestParam() String idEditorial,
            ModelMap modelo){
        
        try {
            libroServicio.crearLibro(isbn, titulo, ejemplares, idAutor, idEditorial);
            modelo.put("exitoLibro", "Libro creado con exito");
        } catch (MiException ex) {
            modelo.put("errorLibro", ex.getMessage());
            
            List<Autor> autores = autorServicio.listarAutores();
            List<Editorial> editoriales = editorialServicio.listarEditoriales();
        
            modelo.addAttribute("autores",autores);
            modelo.addAttribute("editoriales",editoriales);
            
            return "libro_form.html";
        }
        return "/index.html";
    }
    
    @GetMapping("/lista")                   //localhost:8080/libro/lista
    public String vistaListaLibros(ModelMap modelo){
        List<Libro> libros = libroServicio.listarLibros();
        modelo.addAttribute("libros",libros);
        return "libro_lista.html";
    }
    
    @GetMapping("/modificar/{isbn}")
    public String vistaModificarLibro(@PathVariable Long isbn, ModelMap modelo){
        
        List<Autor> autores = autorServicio.listarAutores();
        List<Editorial> editoriales = editorialServicio.listarEditoriales();
        modelo.addAttribute("autores", autores);
        modelo.addAttribute("editoriales", editoriales);

        instanciasLibroAutorEditorialDelLibroActualyModelMap(isbn, modelo);

        return "libro_modificar.html";
    }
    
    @PostMapping("/modificar/{isbn}")
    public String modificarLibro(
            @PathVariable(required = false) Long isbn, 
            String titulo, 
            Integer ejemplares, 
            String idAutor, 
            String idEditorial, 
            ModelMap modelo){
        try {
            libroServicio.modificarLibro(isbn, titulo, ejemplares, idAutor, idEditorial);
            
            return "redirect:../lista";
        } catch (MiException ex) {
            System.out.println(ex);
            modelo.put("error", ex.getMessage());
            List<Autor> autores = autorServicio.listarAutores();
            List<Editorial> editoriales = editorialServicio.listarEditoriales();
            modelo.addAttribute("autores", autores);
            modelo.addAttribute("editoriales", editoriales);
            
            instanciasLibroAutorEditorialDelLibroActualyModelMap(isbn, modelo);
            
            return "libro_modificar.html";
        }
    }
    
    
    public void instanciasLibroAutorEditorialDelLibroActualyModelMap(Long isbn, ModelMap modelo){
        
        Libro libro = libroServicio.getOne(isbn);
        Autor autorDelLibro = libro.getAutor();
        Editorial editorialDelLibro = libro.getEditorial();
        
        modelMapLibroAutorEditorialActual(libro, autorDelLibro, editorialDelLibro, modelo);
        
    }
    
    public void modelMapLibroAutorEditorialActual(
            Libro libro, 
            Autor autorDelLibro,
            Editorial editorialDelLibro,
            ModelMap modelo){
        
        modelo.addAttribute("autorDelLibro",autorDelLibro);
        modelo.addAttribute("editorialDelLibro", editorialDelLibro);
        modelo.put("libro", libro);
    }
}
