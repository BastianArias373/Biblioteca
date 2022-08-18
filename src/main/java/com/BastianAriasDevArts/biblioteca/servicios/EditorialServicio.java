package com.BastianAriasDevArts.biblioteca.servicios;

import com.BastianAriasDevArts.biblioteca.entidades.Editorial;
import com.BastianAriasDevArts.biblioteca.excepciones.MiException;
import com.BastianAriasDevArts.biblioteca.repositorios.EditorialRepositorio;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class EditorialServicio {
    
    @Autowired
    private EditorialRepositorio editorialRepositorio;
    
    @Transactional
    public void crearEditorial(String nombre) throws MiException{
        
        //validacion
        validar(nombre);
        
        Editorial editorial = new Editorial();
        editorial.setNombre(nombre);
        editorialRepositorio.save(editorial);
    }
    
    public List<Editorial> listarEditoriales(){
        List<Editorial> editoriales = new ArrayList();
        editoriales= editorialRepositorio.findAll();
        return editoriales; 
    }
    
    @Transactional
    public void modificarEditorial(String id, String nombre) throws MiException{
        
        //validacion
        validar(nombre);
        
        Optional<Editorial> respuestaEditorial = editorialRepositorio.findById(id);
        Editorial editorial = new Editorial();
        
        if (respuestaEditorial.isPresent()) {
            editorial = respuestaEditorial.get();
            editorial.setNombre(nombre);
            editorialRepositorio.save(editorial);
        }
    }
    
    public Editorial getOne(String id){ 
        return editorialRepositorio.getOne(id);
    }
    
    private void validar(String nombre) throws MiException{
        if(nombre.isEmpty() || nombre == null){
            throw new MiException("nombre no puede ir vacio ni ser nulo");
        }
    }
    
}
