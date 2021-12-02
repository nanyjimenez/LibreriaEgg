

package com.example.demo.repositorios;

import com.example.demo.entidades.Autor;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository 
public interface AutorRepositorio extends JpaRepository<Autor,String> {

    List<Autor> findByNombreContainingOrderByNombre(String nombre);
    
    List<Autor> findAllByOrderByNombreAsc();
    
    @Query("SELECT a FROM Autor a WHERE a.alta = true")
    List<Autor> buscarActivos(); 
    
}
