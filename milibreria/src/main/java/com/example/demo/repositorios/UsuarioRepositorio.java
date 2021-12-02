/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.example.demo.repositorios;

import com.example.demo.entidades.Usuario;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UsuarioRepositorio extends JpaRepository<Usuario, String> {

    Optional<Usuario> findByCorreo(String corre);

    boolean existsByCorreo(String correo);

    @Modifying
    @Query("UPDATE Usuario u SET u.alta = true WHERE u.id = :id")
    void habilitar(@Param("id") String id);

    @Query("SELECT a from Usuario a WHERE a.correo LIKE :correo AND a.alta = true")
    public Usuario buscarPorEmail(@Param("correo") String correo);
}
