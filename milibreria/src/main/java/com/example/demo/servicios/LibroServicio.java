package com.example.demo.servicios;

import com.example.demo.Errores.MisErrores;
import com.example.demo.entidades.Libro;
import com.example.demo.repositorios.AutorRepositorio;
import com.example.demo.repositorios.EditorialRepositorio;
import com.example.demo.repositorios.LibroRepositorio;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
public class LibroServicio {

    @Autowired
    private AutorRepositorio autorRepositorio;
    @Autowired
    private EditorialRepositorio editorialRepositorio;
    @Autowired
    private LibroRepositorio libroRepositorio;
    @Autowired
    private AutorServicio autorServicio;
    @Autowired
    private EditorialServicio editorialServicio;

    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = {Exception.class})
    public Libro guardarLibro(Long isbn, String titulo, Integer anio,
            Integer ejemplares, String autor, String editorial) throws Exception {

        validar(isbn, titulo, anio, ejemplares, autor, editorial);
        Libro libro = new Libro();

        libro.setIsbn(isbn);
        libro.setTitulo(titulo);
        libro.setAnio(anio);
        libro.setEjemplares(ejemplares);
        libro.setEPrestados(0);
        libro.setERestantes(0);
        libro.setAlta(true);
        libro.setAutor(autorRepositorio.getById(autor));
        libro.setEditorial(editorialRepositorio.getById(editorial));
        return libroRepositorio.save(libro);

    }

    @Transactional(readOnly = true)
    public List<Libro> listarActivos() {
        return libroRepositorio.findByAltaTrueOrderByAlta();
    }

    @Transactional(readOnly = true)
    public List<Libro> listarTodos() {
        return libroRepositorio.findAllByOrderByTituloAsc();
    }

    @Transactional(readOnly = true)
    public List<Libro> buscarPorTitulo(String titulo) {
        return libroRepositorio.findByTituloContainingOrderByTitulo(titulo);
    }

    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = {Exception.class})
    public Libro alta(String id) {

        Libro entidad = libroRepositorio.getOne(id);

        entidad.setAlta(true);
        return libroRepositorio.save(entidad);
    }

    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = {Exception.class})
    public Libro baja(String id) {

        Libro entidad = libroRepositorio.getOne(id);

        entidad.setAlta(false);
        return libroRepositorio.save(entidad);
    }

    @Transactional(readOnly = true)
    public Libro buscarPorId(String id) {
        return libroRepositorio.getById(id);
    }

    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = {Exception.class})
    public Libro modificar(String id, Long isbn, String titulo, Integer anio, Integer ejemplares, Integer ejemplaresPrestados, Integer ejemplaresRestantes, String autor, String editorial) throws Exception {
        validar(isbn, titulo, anio, ejemplares, autor, editorial);
        Libro libroEncontrado = libroRepositorio.getById(id);
        libroEncontrado.setAnio(anio);
        libroEncontrado.setIsbn(isbn);
        libroEncontrado.setTitulo(titulo);
        libroEncontrado.setEjemplares(ejemplares);
        if (ejemplaresPrestados < 0 || ejemplaresPrestados == null ) {
            throw new Exception();
        }
        libroEncontrado.setEPrestados(ejemplaresPrestados);
         if (ejemplaresRestantes < 0 || ejemplaresRestantes == null  || ejemplaresRestantes < ejemplaresPrestados) {
            throw new Exception();
        }
        libroEncontrado.setERestantes(ejemplaresRestantes);
        libroEncontrado.setAutor(autorServicio.buscarPorId(autor));
        libroEncontrado.setEditorial(editorialServicio.buscarPorId(editorial));

        return libroRepositorio.save(libroEncontrado);
    }

    public void validar(Long isbn, String titulo, Integer anio, Integer ejemplares, String autor, String editorial) throws Exception {

        if (titulo == null || titulo.isEmpty() || titulo.contains("  ")) {
            throw new Exception();
        }

        if (autor == null || autor.isEmpty() || autor.contains("  ")) {
            throw new Exception();
        }

        if (editorial == null || editorial.isEmpty() || editorial.contains("  ")) {
            throw new Exception();
        }

        if (isbn == null || anio == null || ejemplares == null ) {
            throw new Exception();
        }

    }
}
