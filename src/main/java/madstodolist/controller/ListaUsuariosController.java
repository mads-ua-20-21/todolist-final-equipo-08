package madstodolist.controller;

import madstodolist.authentication.ManagerUserSesion;
import madstodolist.authentication.UsuarioNoLogeadoException;
import madstodolist.controller.exception.UsuarioNotFoundException;
import madstodolist.model.Tarea;
import madstodolist.model.Usuario;
import madstodolist.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
public class ListaUsuariosController {

    @Autowired
    UsuarioService usuarioService;

    @Autowired
    ManagerUserSesion managerUserSesion;

    @GetMapping("/usuarios")
    public String listadoUsuarios(/*@PathVariable(value="id") Long idUsuario,*/ Model model, HttpSession session){

        /*managerUserSesion.comprobarUsuarioLogeado(session, idUsuario);

        Usuario usuario = usuarioService.findById(idUsuario);
        if (usuario == null) {
            throw new UsuarioNotFoundException();
        }*/

        if(session.getAttribute("idUsuarioLogeado") != null){
            Usuario usuario = usuarioService.findById((Long) session.getAttribute("idUsuarioLogeado"));
            model.addAttribute("usuario", usuario);
        }
        else {
            throw new UsuarioNoLogeadoException();
        }

        List<Usuario> usuarios = usuarioService.allUsuarios();
        //model.addAttribute("usuario", usuario);
        model.addAttribute("usuarios", usuarios);
        return "listaUsuarios";
    }
}
