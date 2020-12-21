package madstodolist.controller;

import madstodolist.authentication.ManagerUserSesion;
import madstodolist.authentication.UsuarioNoAdminException;
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
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

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
        managerUserSesion.comprobarUsuarioAdministrador(session, usuario.getAdministrador());
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

        //Una vez se ha comprobado que hay un usuario logueado se siguen dos caminos,
        //comprobar que coincide con el usuario descrito o que el usuario es admin

        model.addAttribute("usuario", usuario);

        Usuario usuarioDescrito = usuarioService.findById(idUsuario);
        if (usuarioDescrito == null) {
            throw new UsuarioNotFoundException();
        }
        model.addAttribute("usuarioDescrito", usuarioDescrito);

        if (!usuario.equals(usuarioDescrito) && !usuario.getAdministrador()){
            throw new UsuarioNoAdminException();
        }

        return"descripcionUsuario";
    }

    @PostMapping("/usuarios/{id}/bloquear")
    public String bloquearUsuario(@PathVariable(value="id") Long idUsuario,
                                  Model model, RedirectAttributes flash, HttpSession session){

        managerUserSesion.usuarioLogueado(session);
        managerUserSesion.comprobarUsuarioAdministrador(session, (Boolean) session.getAttribute("administrador"));

        Usuario usuario = usuarioService.findById(idUsuario);
        if (usuario == null) {
            throw new UsuarioNotFoundException();
        }
        usuarioService.modificaEstadoUsuario(idUsuario, !usuario.getBloqueado());

        model.addAttribute("usuario", usuario);

        List<Usuario> usuarios = usuarioService.allUsuarios();
        model.addAttribute("usuarios", usuarios);
        if (usuario.getBloqueado()) flash.addFlashAttribute("mensaje", "Usuario bloqueado");
        else flash.addFlashAttribute("mensaje", "Usuario habilitado");

        return "redirect:/usuarios";
    }

    @GetMapping("/usuarios/{id}/editar")
    public String formEditarUsuario(@PathVariable(value="id") Long idUsuario,
                                    @ModelAttribute Usuario usuarioMod,
                                    Model model, HttpSession session){

        managerUserSesion.usuarioLogueado(session);
        Usuario usuario = usuarioService.findById((Long)session.getAttribute("idUsuarioLogeado"));
        if (usuario == null) {
            throw new UsuarioNotFoundException();
        }

        //Una vez se ha comprobado que hay un usuario logueado se siguen dos caminos,
        //comprobar que coincide con el usuario descrito o que el usuario es admin
        model.addAttribute("usuario", usuario);

        Usuario usuarioDescrito = usuarioService.findById(idUsuario);
        if (usuarioDescrito == null) {
            throw new UsuarioNotFoundException();
        }
        model.addAttribute("usuarioDescrito", usuarioDescrito);

        if (!usuario.equals(usuarioDescrito) && !usuario.getAdministrador()){
            throw new UsuarioNoAdminException();
        }

        usuarioMod.setEmail(usuario.getEmail());
        usuarioMod.setNombre(usuario.getNombre());
        usuarioMod.setFechaNacimiento(usuario.getFechaNacimiento());

        return "formEditarUsuario";
    }
}
