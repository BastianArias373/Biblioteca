package com.BastianAriasDevArts.biblioteca.servicios;

import com.BastianAriasDevArts.biblioteca.entidades.Autor;
import com.BastianAriasDevArts.biblioteca.excepciones.MiException;
import com.BastianAriasDevArts.biblioteca.repositorios.AutorRepositorio;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AutorServicio {
    
    @Autowired
    private AutorRepositorio autorRepositorio;
    
    @Transactional
    public void crearAutor(String nombre) throws MiException{
        
        //validacion
        validar(nombre);
        
        Autor autor = new Autor();
        autor.setNombre(nombre);
        autorRepositorio.save(autor);
    }
    
    public List<Autor> listarAutores(){
        List<Autor> autores = new ArrayList();
        autores = autorRepositorio.findAll();
        return autores; 
    }
    
    @Transactional
    public void modificarAutor(String id, String nombre) throws MiException{
        
        //validacion
        validar(nombre);
        
        Optional<Autor> respuestaAutor = autorRepositorio.findById(id);
        Autor autor = new Autor();
        
        if (respuestaAutor.isPresent()) {
            autor = respuestaAutor.get();
            autor.setNombre(nombre);
            autorRepositorio.save(autor);
        }
    }
    
    public Autor getOne(String id){
        return autorRepositorio.getOne(id);
    }
    
    private void validar(String nombre) throws MiException{
        if(nombre.isEmpty() || nombre == null){
            throw new MiException("nombre no puede ir vacio");
        }
    }
}
