package com.example.demo.controladores;

import com.example.demo.entidades.Autor;
import com.example.demo.entidades.Editorial;
import com.example.demo.entidades.Libro;
import com.example.demo.servicios.AutorServicio;
import com.example.demo.servicios.EditorialServicio;
import com.example.demo.servicios.LibroServicio;
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
@RequestMapping("/libro")
public class LibroController {

    @Autowired
    private LibroServicio libroServicio;
    @Autowired
    private AutorServicio autorServicio;
    @Autowired
    private EditorialServicio editorialServicio;

    @GetMapping("/registrar-libro")
    public String formulario(ModelMap modelo) {
        List<Autor> autores = autorServicio.listarActivos();
        modelo.addAttribute("autores", autores);
        List<Editorial> editoriales = editorialServicio.listarActivos();
        modelo.addAttribute("editoriales", editoriales);

        return "crear-libro";
    }

    @PostMapping("/registrar-libro")
    public String guardarLibro(ModelMap modelo, @RequestParam Long isbn,
            @RequestParam String titulo, @RequestParam Integer anio,
            @RequestParam Integer ejemplares,
            @RequestParam String autor, @RequestParam String editorial) {

        try {
            libroServicio.guardarLibro(isbn, titulo, anio, ejemplares, autor, editorial);
            modelo.put("exito", "Registro exitoso");
            return "crear-libro";
        } catch (Exception e) {
            modelo.put("error", "Falto algun dato");
            return "crear-libro";
        }
    }

    @GetMapping("/listar-libro")
    public String lista(ModelMap modelo) {

        List<Libro> todos = libroServicio.listarTodos();
        modelo.addAttribute("libros", todos);
        return "form-listlibros";
    }

    @GetMapping("/baja/{id}")
    public String baja(ModelMap modelo, @PathVariable String id) {

        try {
            libroServicio.baja(id);
            return "redirect:/libro/listar-libro";
        } catch (Exception e) {
            return "redirect:/";
        }
    }

    @GetMapping("/alta/{id}")
    public String alta(ModelMap modelo, @PathVariable String id) {

        try {
            libroServicio.alta(id);
            return "redirect:/libro/listar-libro";
        } catch (Exception e) {
            return "redirect:/";
        }
    }

    @GetMapping("/modificarl/{id}")
    public String modificarl(@PathVariable String id, ModelMap modelo) {
        modelo.put("libro", libroServicio.buscarPorId(id));
        List<Autor> autores = autorServicio.listarTodos();
        modelo.addAttribute("autores", autores);
        List<Editorial> editoriales = editorialServicio.listarTodos();
        modelo.addAttribute("editoriales", editoriales);
        return "form-modificarlibro";
    }

    @PostMapping("/modificarl/{id}")
    public String modificarl(ModelMap modelo, @PathVariable String id, @RequestParam(required = false) Long isbn,
            @RequestParam String titulo, @RequestParam(required = false) Integer anio,
            @RequestParam(required = false) Integer ejemplares,
            @RequestParam(required = false) Integer ejemplaresPrestados,
            @RequestParam(required = false) Integer ejemplaresRestantes,
            @RequestParam String autor, @RequestParam String editorial) {
        try {
            libroServicio.modificar(id, isbn, titulo, anio, ejemplares, ejemplaresPrestados, ejemplaresRestantes, autor, editorial);
            modelo.put("exito", "Modificación exitosa");
            modelo.put("libro", libroServicio.buscarPorId(id));
            return "form-modificarlibro";
        } catch (Exception e) {
            modelo.put("error", "Falta algun dato");
            modelo.put("libro", libroServicio.buscarPorId(id));
            return "form-modificarlibro";
        }
    }
}
