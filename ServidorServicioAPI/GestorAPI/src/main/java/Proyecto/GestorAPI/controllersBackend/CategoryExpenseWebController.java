package Proyecto.GestorAPI.controllersBackend;

import Proyecto.GestorAPI.models.CategoryExpense;
import Proyecto.GestorAPI.servicesimpl.CategoryExpenseServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controlador web para la gestión de categorías de gasto en el sistema.
 *
 * <p>
 * Provee funcionalidades para listar, crear, editar y eliminar categorías de gasto
 * mediante interfaces web con Thymeleaf.
 * </p>
 *
 * <p>
 * La ruta base para este controlador es <code>/admin/categorias</code>.
 * </p>
 *
 * Funcionalidades principales:
 * <ul>
 *   <li>Listar todas las categorías existentes</li>
 *   <li>Crear o actualizar una categoría</li>
 *   <li>Editar una categoría existente</li>
 *   <li>Eliminar una categoría por su ID</li>
 * </ul>
 */
@Controller
@RequestMapping("/admin/categorias")
public class CategoryExpenseWebController {

    /**
     * Ruta base para el retorno de vistas HTML relacionadas con categorías.
     */
    private final String rutaHTML = "/admin/categorias";

    @Autowired
    private CategoryExpenseServiceImpl categoriaService;

    /**
     * Lista interna temporal para almacenar categorías cargadas desde el servicio.
     */
    private List<CategoryExpense> categorias;

    /**
     * Método privado que inicializa los datos compartidos necesarios para las vistas,
     * en particular la lista de categorías.
     */
    private void initDatosCompartidos() {
        categorias = categoriaService.getAll();
    }

    /**
     * Endpoint GET para listar todas las categorías de gasto.
     *
     * <p>
     * Carga las categorías, agrega la lista y un objeto nuevo para el formulario al modelo,
     * y retorna la vista correspondiente.
     * </p>
     *
     * En caso de error, agrega un mensaje de error al modelo y retorna la misma vista.
     *
     * @param model objeto {@link Model} para pasar atributos a la vista.
     * @return nombre de la plantilla Thymeleaf para listar categorías.
     */
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

    /**
     * Endpoint POST para crear o actualizar una categoría.
     *
     * <p>
     * Recibe un objeto {@link CategoryExpense} desde el formulario, lo guarda o actualiza
     * mediante el servicio y redirige a la lista de categorías.
     * </p>
     *
     * En caso de error, recarga los datos y retorna la vista con mensajes de error y el formulario
     * prellenado con los datos ingresados.
     *
     * @param categoria objeto {@link CategoryExpense} con datos del formulario.
     * @param model objeto {@link Model} para pasar atributos a la vista.
     * @return redirección a la lista o la misma vista en caso de error.
     */
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

    /**
     * Endpoint GET para mostrar el formulario de edición de una categoría.
     *
     * <p>
     * Busca la categoría por su ID. Si existe, la agrega al modelo para ser editada.
     * También agrega la lista completa de categorías.
     * </p>
     *
     * En caso de error, agrega mensaje de error y retorna la vista de listado.
     *
     * @param id identificador único de la categoría a editar.
     * @param model objeto {@link Model} para pasar atributos a la vista.
     * @return nombre de la plantilla Thymeleaf para edición/listado de categorías.
     */
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

    /**
     * Endpoint GET para eliminar una categoría por su ID.
     *
     * <p>
     * Invoca el servicio para borrar la categoría y redirige a la lista de categorías.
     * En caso de error, agrega un mensaje de error al modelo pero igualmente redirige.
     * </p>
     *
     * @param id identificador único de la categoría a eliminar.
     * @param model objeto {@link Model} para pasar atributos a la vista.
     * @return redirección a la lista de categorías.
     */
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
