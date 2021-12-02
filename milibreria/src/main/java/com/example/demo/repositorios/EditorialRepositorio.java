

package com.example.demo.repositorios;

import com.example.demo.entidades.Editorial;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface EditorialRepositorio extends JpaRepository<Editorial,String>  {
    
    public List<Editorial> findByNombreContainingOrderByNombre(String nombre);
    
    public List<Editorial> findAllByOrderByNombreAsc();
    
        @Query("SELECT a FROM Editorial a WHERE a.alta = true")
        List<Editorial> buscarActivos(); 
}