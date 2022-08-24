package com.BastianAriasDevArts.biblioteca.controladores;

import com.BastianAriasDevArts.biblioteca.entidades.Usuario;
import com.BastianAriasDevArts.biblioteca.servicios.UsuarioServicio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/imagen")
public class ImagenControlador {
    
    @Autowired
    UsuarioServicio usuarioServicio;
    
    @GetMapping("/perfil/{id}")
    public ResponseEntity<byte[]> imagenUsuario(@PathVariable String id){
        Usuario usuario = usuarioServicio.getOne(id);       //guardo el usuario
        byte[] imagen = usuario.getImagen().getContenido(); //extraigo los bytes de la img
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.IMAGE_JPEG);
        
        
        return new ResponseEntity<>(imagen, headers, HttpStatus.OK);
    }
    
}
