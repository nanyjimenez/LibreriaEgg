
package com.example.demo.controladores;

import com.example.demo.Errores.MisErrores;
import com.example.demo.entidades.Usuario;
import com.example.demo.servicios.UsuarioServicio;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.support.RequestContextUtils;
import org.springframework.web.servlet.view.RedirectView;



@Controller
@RequestMapping("/usuario")
public class UsuarioController {
@Autowired
    private UsuarioServicio usuarioService;

    @GetMapping
    public ModelAndView mostrar(HttpServletRequest request) {
        ModelAndView mav = new ModelAndView("usuarios");
        Map<String, ?> flashMap = RequestContextUtils.getInputFlashMap(request);

        if (flashMap != null) {
            mav.addObject("exito", flashMap.get("exito"));
            mav.addObject("error", flashMap.get("error"));
        }

        mav.addObject("usuarios", usuarioService.buscarTodos());
        return mav;
    }

    @GetMapping("/crear")
    public ModelAndView crear(HttpServletRequest request) {
        ModelAndView mav = new ModelAndView("usuario-formulario");
        Map<String, ?> flashMap = RequestContextUtils.getInputFlashMap(request);

      if (flashMap != null) {
            mav.addObject("error", flashMap.get("error"));
            mav.addObject("usuario", flashMap.get("usuario"));
        } else {
            mav.addObject("usuario", new Usuario());
        }
        mav.addObject("title", "Crear Usuario");
        mav.addObject("action", "guardar");
        return mav;
    }

    @GetMapping("/editar/{id}")
    public ModelAndView editar(@PathVariable String id, HttpServletRequest request, HttpSession session, RedirectAttributes attributes) {
        if (!session.getAttribute("id").equals(id)) {
            return new ModelAndView(new RedirectView("/index"));
        }

        ModelAndView mav = new ModelAndView("form-usuario");
        Map<String, ?> flashMap = RequestContextUtils.getInputFlashMap(request);

        try {
            if (flashMap != null) {
                mav.addObject("error", flashMap.get("error"));
                mav.addObject("usuario", flashMap.get("usuario"));
            } else {
                mav.addObject("usuario", usuarioService.buscarPorId(id));
            }

            mav.addObject("title", "Editar Usuario");
            mav.addObject("action", "modificar");
        } catch (MisErrores e) {
            attributes.addFlashAttribute("error", e.getMessage());
            mav.setViewName("redirect:/usuario");
        }

        return mav;
    }

    @PostMapping("/guardar")
    public RedirectView guardar(@ModelAttribute Usuario usuario, RedirectAttributes attributes) {
        RedirectView redirectView = new RedirectView("/usuario");

        try {
            usuarioService.crear(usuario);
            attributes.addFlashAttribute("exito", "La creación ha sido realizada satisfactoriamente");
        } catch (MisErrores e) {
            attributes.addFlashAttribute("usuario", usuario);
            attributes.addFlashAttribute("error", e.getMessage());
            redirectView.setUrl("/usuario/crear");
        }

        return redirectView;
    }

    @PostMapping("/modificar")
    public RedirectView modificar(@ModelAttribute Usuario usuario, RedirectAttributes attributes) {
        RedirectView redirectView = new RedirectView("/usuario");

        try {
            usuarioService.modificar(usuario);
            attributes.addFlashAttribute("exito", "La actualización ha sido realizada satisfactoriamente");
        } catch (MisErrores e) {
            attributes.addFlashAttribute("usuario", usuario);
            attributes.addFlashAttribute("error", e.getMessage());
            redirectView.setUrl("/usuario/editar/" + usuario.getId());
        }

        return redirectView;
    }

    @PostMapping("/habilitar/{id}")
    public RedirectView habilitar(@PathVariable String id) {
        usuarioService.habilitar(id);
        return new RedirectView("/usuario");
    }

    @PostMapping("/eliminar/{id}")
    public RedirectView eliminar(@PathVariable String id) {
        usuarioService.eliminar(id);
        return new RedirectView("/usuario");
    }
}
