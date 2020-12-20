package madstodolist.controller;

import madstodolist.authentication.ManagerUserSesion;
import madstodolist.controller.exception.UsuarioNotFoundException;
import madstodolist.model.Categoria;
import madstodolist.model.Equipo;
import madstodolist.model.Usuario;
import madstodolist.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
public class CategoriaController {

    @Autowired
    CategoriaService categoriaService;

    @Autowired
    UsuarioService usuarioService;

    @Autowired
    TareaService tareaService;

    @Autowired
    ManagerUserSesion managerUserSesion;


    @GetMapping("/usuarios/{id}/categoria")
    public String listarEquipos(Model model, HttpSession session) {

        managerUserSesion.usuarioLogueado(session);
        Usuario usuario = usuarioService.findById((Long) session.getAttribute("idUsuarioLogeado"));

        if (usuario == null) {
            throw new UsuarioNotFoundException();
        }
        model.addAttribute("usuario", usuario);

        List<Categoria> categorias = categoriaService.allCategoriasUsuario(usuario.getId());
        model.addAttribute("categorias", categorias);
        return "listaCategorias";
    }

    @GetMapping("/usuarios/{id}/categoria/nueva")
    public String formNuevaCategoria(@PathVariable(value="id") Long idUsuario,
                                 @ModelAttribute CategoriaData categoriaData,
                                     Model model, HttpSession session) {

        managerUserSesion.comprobarUsuarioLogeado(session, idUsuario);

        Usuario usuario = usuarioService.findById(idUsuario);
        if (usuario == null) {
            throw new UsuarioNotFoundException();
        }
        model.addAttribute("usuario", usuario);
        return "formNuevaCategoria";
    }

    @PostMapping("/usuarios/{id}/categoria/nueva")
    public String nuevaCategoria(@PathVariable(value="id") Long idUsuario, @ModelAttribute CategoriaData categoriaData,
                             Model model, RedirectAttributes flash,
                             HttpSession session) {

        managerUserSesion.comprobarUsuarioLogeado(session, idUsuario);

        Usuario usuario = usuarioService.findById(idUsuario);
        if (usuario == null) {
            throw new UsuarioNotFoundException();
        }
        categoriaService.nuevaCategoriaUsuario(idUsuario, categoriaData.getTitulo());
        flash.addFlashAttribute("mensaje", "Categoria creada correctamente");
        return "redirect:/usuarios/" + idUsuario + "/categoria";
    }
}
