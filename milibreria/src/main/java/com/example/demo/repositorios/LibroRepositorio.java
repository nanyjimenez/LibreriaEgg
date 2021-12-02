

package com.example.demo.repositorios;

import com.example.demo.entidades.Libro;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface LibroRepositorio extends JpaRepository<Libro,String> {

    
    List<Libro> findByAltaTrueOrderByAlta();
    
    List<Libro> findAllByOrderByTituloAsc();
    
    List<Libro> findByTituloContainingOrderByTitulo(String nombre);
    
    

}
