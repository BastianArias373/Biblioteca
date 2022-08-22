package com.BastianAriasDevArts.biblioteca.entidades;

import javax.persistence.Basic;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Lob;
import org.hibernate.annotations.GenericGenerator;

@Entity
public class Imagen {
    
    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    private String id;
    private String nombre;
    private String mime;        //asigna el formato del archivo de la imagen
    
    @Lob                            //para datos GRANDES, pesados en bytes
    @Basic(fetch = FetchType.LAZY)  //El contenido se carga cuando lo pidamos
    private byte[] contenido;       //arreglo de bytes, de esta forma se guarda el contenido

    //constructor
    public Imagen() {
    }

    //getters y setters
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getMime() {
        return mime;
    }
    public void setMime(String mime) {
        this.mime = mime;
    }

    public byte[] getContenido() {
        return contenido;
    }
    public void setContenido(byte[] contenido) {
        this.contenido = contenido;
    }
    
    
    

    
    
    
    
    
    
}
