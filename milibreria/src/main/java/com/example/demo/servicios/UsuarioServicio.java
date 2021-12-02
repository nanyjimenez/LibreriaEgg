package com.example.demo.servicios;

import com.example.demo.Errores.MisErrores;
import com.example.demo.entidades.Usuario;
import com.example.demo.enumerado.Rol;
import com.example.demo.repositorios.UsuarioRepositorio;
import java.util.Collections;
import java.util.List;
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


@Service
public class UsuarioServicio implements UserDetailsService {
    
   @Autowired
    private UsuarioRepositorio usuarioRepository;

    @Autowired
    private EmailServicio emailService;

    @Autowired
    private BCryptPasswordEncoder encoder;

    private String mensaje = "No existe ningún usuario asociado con el ID %s";

    @Transactional
    public void crear(Usuario dto) throws MisErrores {
        if (usuarioRepository.existsByCorreo(dto.getCorreo())) {
            throw new MisErrores("Ya existe un usuario asociado al correo ingresado");
        }
        Usuario usuario = new Usuario();
        usuario.setNombre(dto.getNombre());
        usuario.setApellido(dto.getApellido());
        usuario.setCorreo(dto.getCorreo());
        usuario.setClave(encoder.encode(dto.getClave()));
        if (usuarioRepository.findAll().isEmpty()) {
            usuario.setRol(Rol.ADMIN);
        } else if (dto.getRol() == null) {
            usuario.setRol(Rol.USER);
        } else {
            usuario.setRol(dto.getRol());
        }
        usuario.setAlta(true);
        emailService.enviarThread(dto.getCorreo());
        usuarioRepository.save(usuario);
    }

    @Transactional
    public void modificar(Usuario dto) throws MisErrores {
        Usuario usuario = usuarioRepository.findById(dto.getId()).orElseThrow(() -> new MisErrores(String.format(mensaje, dto.getId())));
        usuario.setNombre(dto.getNombre());
        usuario.setApellido(dto.getApellido());
        usuario.setCorreo(dto.getCorreo());
        usuario.setRol(dto.getRol());
        usuarioRepository.save(usuario);
    }

    @Transactional(readOnly = true)
    public List<Usuario> buscarTodos() {
        return usuarioRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Usuario buscarPorId(String id) throws MisErrores {
        return usuarioRepository.findById(id).orElseThrow(() -> new MisErrores(String.format(mensaje, id)));
    }

    @Transactional
    public void habilitar(String id) {
        usuarioRepository.habilitar(id);
    }

    @Transactional
    public void eliminar(String id) {
        usuarioRepository.deleteById(id);
    }

    @Override
    public UserDetails loadUserByUsername(String correo) throws UsernameNotFoundException {
        Usuario usuario = usuarioRepository.findByCorreo(correo)
                .orElseThrow(() -> new UsernameNotFoundException("No existe un usuario asociado al correo ingresado"));
        GrantedAuthority authority = new SimpleGrantedAuthority("ROLE_" + usuario.getRol().name());

        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
        HttpSession session = attributes.getRequest().getSession(true);

        session.setAttribute("id", usuario.getId());
        session.setAttribute("nombre", usuario.getNombre());
        session.setAttribute("apellido", usuario.getApellido());
        session.setAttribute("rol", usuario.getRol().name());

        return new User(usuario.getCorreo(), usuario.getClave(), Collections.singletonList(authority));
    }
}
