package com.example.demo.controladores;

import com.example.demo.entidades.Autor;
import com.example.demo.servicios.AutorServicio;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/autor")
public class AutorController {

    @Autowired
    private AutorServicio autorServicio;

    @GetMapping("/registrar-autor")
    public String formulario() {
        return "crear-autor";
    }

    
    @GetMapping("/listar-autor")
    public String lista(ModelMap modelo) {
        
        List<Autor> todos = autorServicio.listarTodos();
        modelo.addAttribute("autores", todos);
        return "form-listautor";
    }

    @PostMapping("/registrar-autor")
    public String guardarAutor(ModelMap modelo, @RequestParam String nombre) {

        try {

            autorServicio.guardar(nombre);
            modelo.put("exito", "Registro exitoso");
            return "crear-autor";
        } catch (Exception e) {
            modelo.put("error", "Falto algun dato");
            return "crear-autor";
        }
    }

    @GetMapping("/baja/{id}")
    public String baja(ModelMap modelo, @PathVariable String id) {

        try {
            autorServicio.baja(id);
            return "redirect:/autor/listar-autor";
        } catch (Exception e) {
            return "redirect:/";
        }
    }

    @GetMapping("/alta/{id}")
    public String alta(ModelMap modelo, @PathVariable String id) {

        try {
            autorServicio.alta(id);
            return "redirect:/autor/listar-autor";
        } catch (Exception e) {
            return "redirect:/";
        }
    }

    @GetMapping("/modificara/{id}") //PATHVARIABLE
    public String modificara(@PathVariable String id, ModelMap modelo ) {

        modelo.put("autor", autorServicio.buscarPorId(id));
        return "form-modificarautor";
    }

    @PostMapping("/modificara/{id}")
    public String modificara(ModelMap modelo, @PathVariable String id, @RequestParam String nombre) {
        try {
            autorServicio.modificar(nombre, id);
            modelo.put("exito", "Modificacion exitosa");
            modelo.put("autor", autorServicio.buscarPorId(id));
            return "form-modificarautor";
        } catch (Exception e) {
            modelo.put("error", "El nombre no puede ser vacio");
            modelo.put("autor", autorServicio.buscarPorId(id));
            return "form-modificarautor";
        }
    }

}
