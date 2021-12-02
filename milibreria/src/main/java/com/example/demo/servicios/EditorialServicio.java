package com.example.demo.servicios;

import com.example.demo.entidades.Editorial;
import com.example.demo.repositorios.EditorialRepositorio;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
public class EditorialServicio {

    @Autowired
    private EditorialRepositorio editorialRepositorio;

    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = {Exception.class})
    public Editorial guardar(String nombre) {

        Editorial editorial = new Editorial();

        editorial.setNombre(nombre);
        editorial.setAlta(true);

        return editorialRepositorio.save(editorial);
    }

    @Transactional(readOnly = true)
    public List<Editorial> buscarPorNombre(String nombre) {
        return editorialRepositorio.findByNombreContainingOrderByNombre(nombre);
    }

    @Transactional(readOnly = true)
    public List<Editorial> listarActivos() {
        return editorialRepositorio.buscarActivos();
    }

    @Transactional(readOnly = true)
    public List<Editorial> listarTodos() {
        return editorialRepositorio.findAllByOrderByNombreAsc();
    }

    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = {Exception.class})
    public Editorial alta(String id) {

        Editorial entidad = editorialRepositorio.getOne(id);

        entidad.setAlta(true);
        return editorialRepositorio.save(entidad);
    }

    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = {Exception.class})
    public Editorial baja(String id) {

        Editorial entidad = editorialRepositorio.getOne(id);

        entidad.setAlta(false);
        return editorialRepositorio.save(entidad);
    }

    @Transactional(readOnly = true)
    public Editorial buscarPorId(String id) {
        return editorialRepositorio.getById(id);
    }

    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = {Exception.class})
    public Editorial modificar(String nombre, String id) throws Exception {

        Editorial autorEncontrado = editorialRepositorio.getById(id);
        autorEncontrado.setNombre(nombre);

        return editorialRepositorio.save(autorEncontrado);
    }
}
