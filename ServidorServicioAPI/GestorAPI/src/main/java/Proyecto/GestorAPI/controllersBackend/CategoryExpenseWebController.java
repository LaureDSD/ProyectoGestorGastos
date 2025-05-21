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

    // CREATE: Mostrar formulario
    @GetMapping("/nueva")
    public String mostrarFormulario(Model model) {
        model.addAttribute("categoria", new CategoryExpense());
        return rutaHTML + "-form";
    }

    // CREATE: Guardar nueva categoría
    @PostMapping("/guardar")
    public String guardarCategoria(@ModelAttribute CategoryExpense categoria) {
        categoriaService.setItem(categoria);
        return "redirect:" + rutaHTML;
    }

    // READ: Listar todas las categorías
    @GetMapping
    public String listarCategorias(Model model) {
        List<CategoryExpense> categorias = categoriaService.getAll();
        model.addAttribute("categorias", categorias);
        return rutaHTML;
    }

    // UPDATE: Mostrar formulario de edición
    @GetMapping("/editar/{id}")
    public String editarCategoria(@PathVariable Long id, Model model) {
        CategoryExpense categoria = categoriaService.getByID(id)
                .orElseThrow(() -> new RuntimeException("Categoría no encontrada"));
        model.addAttribute("categoria", categoria);
        return rutaHTML + "-form";
    }

    // DELETE: Eliminar categoría
    @GetMapping("/eliminar/{id}")
    public String eliminarCategoria(@PathVariable Long id, Model model) {
        try {
            categoriaService.deleteByID(id);
        } catch (Exception e) {
            model.addAttribute("error",
                    "No se puede eliminar la categoría porque tiene gastos asociados");
        }
        return "redirect:" + rutaHTML;
    }
}