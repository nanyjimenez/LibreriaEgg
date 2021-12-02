package com.example.demo.controladores;

import com.example.demo.entidades.Editorial;
import com.example.demo.servicios.EditorialServicio;
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
@RequestMapping("/editorial")
public class EditorialController {

    @Autowired
    private EditorialServicio editorialServicio;

    @GetMapping("/registrar-editorial")
    public String formulario() {
        return "crear-editorial";
    }

    
    @GetMapping("/listar-editorial")
    public String lista(ModelMap modelo) {
        
        List<Editorial> todos = editorialServicio.listarTodos();
        modelo.addAttribute("editoriales", todos);
        return "form-listeditorial";
    }

    @PostMapping("/registrar-editorial")
    public String guardarAutor(ModelMap modelo, @RequestParam String nombre) {

        try {

            editorialServicio.guardar(nombre);
            modelo.put("exito", "Registro exitoso");
            return "crear-editorial";
        } catch (Exception e) {
            modelo.put("error", "Falto algun dato");
            return "crear-editorial";
        }
    }

    @GetMapping("/baja/{id}")
    public String baja(ModelMap modelo, @PathVariable String id) {

        try {
            editorialServicio.baja(id);
            return "redirect:/editorial/listar-editorial";
        } catch (Exception e) {
            return "redirect:/";
        }
    }

    @GetMapping("/alta/{id}")
    public String alta(ModelMap modelo, @PathVariable String id) {

        try {
            editorialServicio.alta(id);
            return "redirect:/editorial/listar-editorial";
        } catch (Exception e) {
            return "redirect:/";
        }
    }

    @GetMapping("/modificare/{id}") //PATHVARIABLE
    public String modificare(@PathVariable String id, ModelMap modelo) {

        modelo.put("editorial", editorialServicio.buscarPorId(id));
        return "form-modificareditorial";
    }

    @PostMapping("/modificare/{id}")
    public String modificare(ModelMap modelo, @PathVariable String id, @RequestParam String nombre) {
        try {
            editorialServicio.modificar(nombre, id);
            modelo.put("exito", "Modificacion exitosa");
            modelo.put("editorial", editorialServicio.buscarPorId(id));
            return "form-modificareditorial";
        } catch (Exception e) {
            modelo.put("error", "El nombre no puede ser vacio");
            modelo.put("editorial", editorialServicio.buscarPorId(id));
            return "form-modificareditorial";
        }
    }

}
