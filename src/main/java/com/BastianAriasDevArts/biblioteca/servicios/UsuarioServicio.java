package com.BastianAriasDevArts.biblioteca.servicios;

import com.BastianAriasDevArts.biblioteca.entidades.Imagen;
import com.BastianAriasDevArts.biblioteca.entidades.Usuario;
import com.BastianAriasDevArts.biblioteca.enumeraciones.Rol;
import com.BastianAriasDevArts.biblioteca.excepciones.MiException;
import com.BastianAriasDevArts.biblioteca.repositorios.UsuarioRepositorio;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import javax.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.MultipartFile;

@Service
public class UsuarioServicio implements UserDetailsService{
    
    @Autowired
    private UsuarioRepositorio usuarioRepositorio;
    
    @Autowired
    private ImagenServicio imagenServicio;
    
    @Transactional
    public void registrar(
            String nombre, 
            String email, 
            String password, 
            String password2,
            MultipartFile archivo) throws MiException{
        
        //validamos
        validar(nombre, email, password, password2);
        //instanciamos un usuario
        Usuario usuario = new Usuario();
        
        //seteo atributos(menos id, el id se genera automaticamente)
        usuario.setNombre(nombre);
        usuario.setEmail(email);
        usuario.setPassword(new BCryptPasswordEncoder().encode(password));
        usuario.setRol(Rol.USER);
        
        //instanciamos una nueva imagen y la seteamos al Usuario
        Imagen imagen = imagenServicio.guardar(archivo);
        usuario.setImagen(imagen);
        //persisto los cambios en el repositorio con .save
        usuarioRepositorio.save(usuario);
    }
    
    @Transactional
    public void actualizar(
            String idUsuario,
            String nombre, 
            String email, 
            String password, 
            String password2,
            MultipartFile archivo) throws MiException{
        
        //validamos
        validar(nombre, email, password, password2);
        
        
        //optional para traernos el Usuario y tratarlo
        Optional<Usuario> respuesta = usuarioRepositorio.findById(idUsuario);
        if (respuesta != null) {
            Usuario usuario = respuesta.get();
            
            //seteamos
            usuario.setNombre(nombre);
            usuario.setEmail(email);
            usuario.setPassword(new BCryptPasswordEncoder().encode(password));
            usuario.setRol(Rol.USER);
            
            //nos guardamos la id de la imagen actual del usuario
            String idImagen = null;
            if (usuario.getImagen() != null) {
                idImagen = usuario.getImagen().getId();
            }
            
            //usuamos el metodo actualizar para traernos el obj por medio de su id
            Imagen imagen = imagenServicio.actualizar(archivo, idImagen);
            
            //seteamos 
            usuario.setImagen(imagen);
            
            usuarioRepositorio.save(usuario);
        }
        
    }
    
    public Usuario getOne(String id){
        return usuarioRepositorio.getOne(id);
    }
    
    
    public void validar(
            String nombre,
            String email, 
            String password, 
            String password2) throws MiException{
        
        if (nombre.isEmpty() || nombre == null) {
            throw new MiException("El nombre no puede ser nulo ni estar vacio");
        }
        
        if (email.isEmpty() || email == null) {
            throw new MiException("El email no puede ser nulo ni estar vacio");
        }
        
        if (password.isEmpty() || password == null) {
            throw new MiException("La contraseña no puede ser nula ni estar vacia");
        }
        
        if(password.trim().isEmpty() || password.length() <= 6){
            throw new MiException("La contraseña no debe contener espacios y debe tener mas de 6 caracteres");
        }
        
        if(!password.equals(password2)){
            throw new MiException("Las contraseñas deben ser iguales");
        }
        
        
        
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        
        Usuario usuario = usuarioRepositorio.buscarPorEmail(email);
        
        if (usuario != null) {
            List<GrantedAuthority> permisos = new ArrayList();
            GrantedAuthority p = new SimpleGrantedAuthority("ROLE_"+usuario.getRol().toString());
            //agregamos a la lista permisos
            permisos.add(p);
            
            //guardar al usuario autenticado en la sesion web
            ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
            
            //guardamos en un objeto HttpSession el llamado del atibuto que nos trae la session
            HttpSession session = attr.getRequest().getSession(true);
            
            //vamos a pedir datos de la session http para setearle los atributos
            session.setAttribute("usuariosession", usuario);    //llave para traer al usuario logueado
            
            User user = new User(usuario.getEmail(),usuario.getPassword(),permisos);
            return user;
            
        }
        else{
            return null;
        }
        
    }
    
}
