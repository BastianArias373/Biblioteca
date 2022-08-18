package com.BastianAriasDevArts.biblioteca.servicios;

import com.BastianAriasDevArts.biblioteca.entidades.Autor;
import com.BastianAriasDevArts.biblioteca.entidades.Editorial;
import com.BastianAriasDevArts.biblioteca.entidades.Libro;
import com.BastianAriasDevArts.biblioteca.excepciones.MiException;
import com.BastianAriasDevArts.biblioteca.repositorios.AutorRepositorio;
import com.BastianAriasDevArts.biblioteca.repositorios.EditorialRepositorio;
import com.BastianAriasDevArts.biblioteca.repositorios.LibroRepositorio;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
public class LibroServicio {
    
    @Autowired
    private LibroRepositorio libroRepositorio;
    @Autowired
    private AutorRepositorio autorRepositorio;
    @Autowired
    private EditorialRepositorio editorialRepositorio;
    
    @Transactional
    public void crearLibro(
            Long isbn, 
            String titulo, 
            Integer ejemplares, 
            String idAutor,
            String idEditorial) throws MiException{
        
        //excepciones
        validar(isbn, titulo, ejemplares, idAutor, idEditorial);
        
        //no hay libro previo, creamos uno
        Libro libro = new Libro();
        
        //instanciamos y asignamos a uno que ya este en el repo, lo buscamos por ID
        Autor autor = autorRepositorio.findById(idAutor).get();
        Editorial editorial = editorialRepositorio.findById(idEditorial).get();
        
        //atributos sin relacion
        libro.setIsbn(isbn);
        libro.setTitulo(titulo);
        libro.setEjemplares(ejemplares);
        libro.setAlta(new Date());
        
        //atributos con relacion ManyToOne
        libro.setAutor(autor);
        libro.setEditorial(editorial);
        
        //PERSISTIMOS todos los datos setados a "libro" con el metodo SAVE
        libroRepositorio.save(libro);
    }
    
    public List<Libro> listarLibros(){
        List<Libro> libros = new ArrayList();
        libros = libroRepositorio.findAll();
        return libros; 
    }
    
    @Transactional
    public void modificarLibro(
            Long isbn, 
            String titulo,
            Integer ejemplares,
            String idAutor,
            String idEditorial) throws MiException{
        
        //validacion
        validar(isbn, titulo, ejemplares, idAutor, idEditorial);
        
        //los Optional son un paso de comprobacion de existencia del objeto
        Optional<Libro> respuestaLibro = libroRepositorio.findById(isbn);
        Optional<Autor> respuestaAutor = autorRepositorio.findById(idAutor);
        Optional<Editorial> respuestaEditorial = editorialRepositorio.findById(idEditorial);
        
        //instancio los objetos para luego asignarles 
        //el objeto que traiga de la DB
        Libro libro = new Libro();
        Autor autor = new Autor();
        Editorial editorial = new Editorial();
        
        //si el elemento esta presente en el repositorio
        if (respuestaAutor.isPresent()) {
            autor = respuestaAutor.get();   //lo traigo
        }
        
        //si el elemento esta presente en el repositorio
        if (respuestaEditorial.isPresent()) {
            editorial = respuestaEditorial.get();   //lo traigo
        }
        
        //si el elemento esta presente en el repositorio
        if (respuestaLibro.isPresent()) {
            libro = respuestaLibro.get(); //lo traigo
            
            //ahora empiezo a setear
            libro.setTitulo(titulo);
            libro.setEjemplares(ejemplares);
            libro.setAutor(autor);
            libro.setEditorial(editorial);
            
            //PERSISTO los datos seteados
            libroRepositorio.save(libro);
        }
    }
    
    public Libro getOne(Long isbn){
        return libroRepositorio.getOne(isbn);
    }
    
    private void validar(
            Long isbn, 
            String titulo, 
            Integer ejemplares,
            String idAutor,
            String idEditorial) throws MiException{
        
        if(isbn == null){
            throw new MiException("isbn no puede ser nulo");
        }
        if(titulo.isEmpty() || titulo == null){
            throw new MiException("titulo no puede ser nulo ni estar vacio");
        }
        if(ejemplares == null){
            throw new MiException("ejemplares no puede ser nulo");
        }
        if(idAutor.isEmpty() || idAutor == null){
            throw new MiException("Autor no puede ser nulo ni estar vacio");
        }
        if(idEditorial.isEmpty() || idEditorial == null){
            throw new MiException("Editorial no puede ser nulo ni estar vacio");
        }
    }
}
