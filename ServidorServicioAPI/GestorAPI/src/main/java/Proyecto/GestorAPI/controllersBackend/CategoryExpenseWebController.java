package Proyecto.GestorAPI.controllersBackend;

import Proyecto.GestorAPI.models.CategoryExpense;
import Proyecto.GestorAPI.servicesimpl.CategoryExpenseServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/admin/categorias")
public class CategoryExpenseWebController {

    private final String rutaHTML = "/admin/categorias";

    @Autowired
    private CategoryExpenseServiceImpl categoriaService;

    private List<CategoryExpense> categorias;

    private void initDatosCompartidos() {
        categorias = categoriaService.getAll();
    }

    // READ: Listar todas las categorías
    @GetMapping
    public String listarCategorias(Model model) {
        try {
            initDatosCompartidos();
            model.addAttribute("categorias", categorias);
            model.addAttribute("categoria", new CategoryExpense()); // Para formulario nuevo
            return rutaHTML;
        } catch (Exception e) {
            model.addAttribute("error", "Error al cargar categorías: " + e.getMessage());
            return rutaHTML;
        }
    }

    // CREATE / UPDATE: Guardar categoría
    @PostMapping("/save")
    public String guardarCategoria(@ModelAttribute CategoryExpense categoria, Model model) {
        try {
            categoriaService.setItem(categoria);
            return "redirect:" + rutaHTML;
        } catch (Exception e) {
            initDatosCompartidos();
            model.addAttribute("error", "Error al guardar la categoría: " + e.getMessage());
            model.addAttribute("categorias", categorias);
            model.addAttribute("categoria", categoria);
            return rutaHTML;
        }
    }

    // UPDATE: Editar categoría
    @GetMapping("/edit/{id}")
    public String editarCategoria(@PathVariable Long id, Model model) {
        try {
            initDatosCompartidos();
            CategoryExpense categoria = categoriaService.getByID(id)
                    .orElse(new CategoryExpense());
            model.addAttribute("categoria", categoria);
            model.addAttribute("categorias", categorias);
            return rutaHTML;
        } catch (Exception e) {
            model.addAttribute("error", "Error al editar la categoría: " + e.getMessage());
            return rutaHTML;
        }
    }

    // DELETE: Eliminar categoría
    @GetMapping("/delete/{id}")
    public String eliminarCategoria(@PathVariable Long id, Model model) {
        try {
            categoriaService.deleteByID(id);
        } catch (Exception e) {
            model.addAttribute("error", "No se puede eliminar la categoría: " + e.getMessage());
        }
        return "redirect:" + rutaHTML;
    }
}
