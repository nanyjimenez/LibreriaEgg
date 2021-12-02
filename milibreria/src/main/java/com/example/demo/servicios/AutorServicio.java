package com.example.demo.servicios;

import com.example.demo.entidades.Autor;
import com.example.demo.repositorios.AutorRepositorio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AutorServicio {

    @Autowired
    private AutorRepositorio autorRepositorio;

    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = {Exception.class})
    public Autor guardar(String nombre) {

        Autor autor = new Autor();

        autor.setNombre(nombre);
        autor.setAlta(true);

        return autorRepositorio.save(autor);
    }

    @Transactional(readOnly = true)
    public List<Autor> buscarPorNombre(String nombre) {
        return autorRepositorio.findByNombreContainingOrderByNombre(nombre);
    }

    @Transactional(readOnly = true)
    public List<Autor> listarActivos() {
        return autorRepositorio.buscarActivos();
    }

    @Transactional(readOnly = true)
    public List<Autor> listarTodos() {
        return autorRepositorio.findAllByOrderByNombreAsc();
    }

    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = {Exception.class})
    public Autor alta(String id) {

        Autor entidad = autorRepositorio.getOne(id);

        entidad.setAlta(true);
        return autorRepositorio.save(entidad);
    }

    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = {Exception.class})
    public Autor baja(String id) {

        Autor entidad = autorRepositorio.getOne(id);

        entidad.setAlta(false);
        return autorRepositorio.save(entidad);
    }

    @Transactional(readOnly = true)
    public Autor buscarPorId(String id) {
        return autorRepositorio.getById(id);
    }

    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = {Exception.class})
    public Autor modificar(String nombre, String id) throws Exception {

        Autor autorEncontrado = autorRepositorio.getById(id);
        autorEncontrado.setNombre(nombre);

        return autorRepositorio.save(autorEncontrado);
    }
}
