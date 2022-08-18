package com.BastianAriasDevArts.biblioteca.repositorios;

import com.BastianAriasDevArts.biblioteca.entidades.Editorial;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EditorialRepositorio extends JpaRepository<Editorial, String>{
    
    
}
