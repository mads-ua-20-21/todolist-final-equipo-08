package madstodolist.controller;

import madstodolist.authentication.ManagerUserSesion;
import madstodolist.authentication.UsuarioNoLogeadoException;
import madstodolist.controller.exception.UsuarioNotFoundException;
import madstodolist.model.Tarea;
import madstodolist.model.Usuario;
import madstodolist.service.TareaService;
import madstodolist.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
public class UsuariosController {

    @Autowired
    UsuarioService usuarioService;

    @Autowired
    TareaService tareaService;

    @Autowired
    ManagerUserSesion managerUserSesion;

    @GetMapping("/usuarios")
    public String listadoUsuarios(Model model, HttpSession session){


        managerUserSesion.usuarioLogueado(session);
        Usuario usuario = usuarioService.findById((Long)session.getAttribute("idUsuarioLogeado"));
        if (usuario == null) {
            throw new UsuarioNotFoundException();
        }
        model.addAttribute("usuario", usuario);

        List<Usuario> usuarios = usuarioService.allUsuarios();
        model.addAttribute("usuarios", usuarios);
        return "listaUsuarios";
    }

    @GetMapping("/usuarios/{id}")
    public String descripcionUsuario(@PathVariable(value="id") Long idUsuario, Model model, HttpSession session){


        managerUserSesion.usuarioLogueado(session);
        Usuario usuario = usuarioService.findById((Long)session.getAttribute("idUsuarioLogeado"));
        if (usuario == null) {
            throw new UsuarioNotFoundException();
        }
        model.addAttribute("usuario", usuario);

        Usuario usuarioDescrito = usuarioService.findById(idUsuario);
        if (usuarioDescrito == null) {
            throw new UsuarioNotFoundException();
        }
        model.addAttribute("usuarioDescrito", usuarioDescrito);


        return"descripcionUsuario";
    }
}
