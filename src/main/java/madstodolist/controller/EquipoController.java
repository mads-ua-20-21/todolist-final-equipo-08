package madstodolist.controller;

import madstodolist.authentication.ManagerUserSesion;
import madstodolist.controller.exception.UsuarioNotFoundException;
import madstodolist.model.Equipo;
import madstodolist.model.Usuario;
import madstodolist.service.EquipoService;
import madstodolist.service.EquipoServiceException;
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
public class EquipoController {

    @Autowired
    EquipoService equipoService;

    @Autowired
    UsuarioService usuarioService;

    @Autowired
    ManagerUserSesion managerUserSesion;


    @GetMapping("/equipos")
    public String listarEquipos(Model model, HttpSession session){

        managerUserSesion.usuarioLogueado(session);
        Usuario usuario = usuarioService.findById((Long)session.getAttribute("idUsuarioLogeado"));
        if (usuario == null) {
            throw new UsuarioNotFoundException();
        }
        model.addAttribute("usuario", usuario);

        List<Equipo> equipos = equipoService.findAllOrderedByName();
        model.addAttribute("equipos", equipos);
        return "listaEquipos";
    }

    @GetMapping("/equipos/{id}")
    public String usuariosEquipo(@PathVariable(value="id") Long idEquipo, Model model, HttpSession session){

        managerUserSesion.usuarioLogueado(session);
        Usuario usuario = usuarioService.findById((Long)session.getAttribute("idUsuarioLogeado"));
        if (usuario == null) {
            throw new UsuarioNotFoundException();
        }
        model.addAttribute("usuario", usuario);

        Equipo equipo = equipoService.findById(idEquipo);
        model.addAttribute("equipo", equipo);

        List<Usuario> usuarios = equipoService.usuariosEquipo(idEquipo);
        model.addAttribute("usuarios", usuarios);
        return "usuariosEquipo";
    }

    @GetMapping("/equipos/nuevo")
    public String formNuevoEquipo(@ModelAttribute Equipo equipo, Model model, HttpSession session){

        managerUserSesion.usuarioLogueado(session);
        Usuario usuario = usuarioService.findById((Long)session.getAttribute("idUsuarioLogeado"));
        if (usuario == null) {
            throw new UsuarioNotFoundException();
        }
        model.addAttribute("usuario", usuario);
        return "formNuevoEquipo";
    }


    @PostMapping("/equipos/nuevo")
    public String nuevoEquipoConAdmin(@ModelAttribute Equipo equipo, Model model, RedirectAttributes flash,
                              HttpSession session){

        managerUserSesion.usuarioLogueado(session);
        Usuario usuario = usuarioService.findById((Long)session.getAttribute("idUsuarioLogeado"));
        if (usuario == null) {
            throw new UsuarioNotFoundException();
        }
        equipoService.nuevoEquipoConAdmin(equipo.getNombre(), usuario);
        flash.addFlashAttribute("mensaje", "Equipo creada correctamente");
        return "redirect:/equipos";
    }

    @PostMapping("/equipos/{id}")
    public String unirmeAEquipo(@PathVariable(value="id") Long idEquipo,Model model,
                                RedirectAttributes flash, HttpSession session){
        managerUserSesion.usuarioLogueado(session);
        Usuario usuario = usuarioService.findById((Long)session.getAttribute("idUsuarioLogeado"));
        if (usuario == null) {
            throw new UsuarioNotFoundException();
        }
        if (equipoService.anyadirUsuarioAEquipo(usuario.getId(), idEquipo)){
            flash.addFlashAttribute("mensaje", "Te has unido al equipo correctamente");
        }
        else flash.addFlashAttribute("mensaje", "Error al unirse al equipo");
        return "redirect:/equipos/" + idEquipo;
    }

    @PostMapping("/equipos/{id}/salir")
    public String salirDeEquipo(@PathVariable(value="id") Long idEquipo,Model model,
                                RedirectAttributes flash, HttpSession session){
        managerUserSesion.usuarioLogueado(session);
        Usuario usuario = usuarioService.findById((Long)session.getAttribute("idUsuarioLogeado"));
        if (usuario == null) {
            throw new UsuarioNotFoundException();
        }
        if (equipoService.eliminarUsuarioDeEquipo(usuario.getId(), idEquipo)){
            flash.addFlashAttribute("mensaje", "Has salido del equipo");
        }
        else flash.addFlashAttribute("mensaje", "Error al dejar el equipo");
        return "redirect:/equipos/" + idEquipo;
    }

    @PostMapping("/equipos/{id}/quitar/{idUsuario}")
    public String quitarDeEquipo(@PathVariable(value="id") Long idEquipo, @PathVariable(value = "idUsuario") Long idUsuario,Model model,
                                RedirectAttributes flash, HttpSession session){
        managerUserSesion.usuarioLogueado(session);
        Usuario usuario = usuarioService.findById((Long)session.getAttribute("idUsuarioLogeado"));
        if (usuario == null) {
            throw new UsuarioNotFoundException();
        }
        if (equipoService.eliminarUsuarioDeEquipo(idUsuario, idEquipo)){
            flash.addFlashAttribute("mensaje", "Usuario fuera del equipo");
        }
        else flash.addFlashAttribute("mensaje", "Error al dejar el equipo");
        return "redirect:/equipos/" + idEquipo;
    }

    @GetMapping("/equipos/{id}/editar")
    public String mostrarFormNuevoEquipo(@PathVariable(value="id") Long idEquipo, Model model, HttpSession session){

        managerUserSesion.usuarioLogueado(session);
        //managerUserSesion.comprobarUsuarioAdministrador(session, (Boolean) session.getAttribute("administrador"));
        Usuario usuario = usuarioService.findById((Long)session.getAttribute("idUsuarioLogeado"));
        if (usuario == null) {
            throw new UsuarioNotFoundException();
        }
        model.addAttribute("usuario", usuario);

        if (!equipoService.usuarioAdministraEquipo(usuario.getId(), idEquipo)) {
            managerUserSesion.comprobarUsuarioAdministrador(session, (Boolean) session.getAttribute("administrador"));
        }

        Equipo equipo = equipoService.findById(idEquipo);
        model.addAttribute("equipo", equipo);

        return "formEditarEquipo";
    }

    @PostMapping("/equipos/{id}/editar")
    public String formNuevoEquipo(@PathVariable(value="id") Long idEquipo, @ModelAttribute Equipo equipo,
                                  Model model, HttpSession session){
        managerUserSesion.usuarioLogueado(session);
        //managerUserSesion.comprobarUsuarioAdministrador(session, (Boolean) session.getAttribute("administrador"));
        Usuario usuario = usuarioService.findById((Long)session.getAttribute("idUsuarioLogeado"));
        if (usuario == null) {
            throw new UsuarioNotFoundException();
        }

        if (!equipoService.usuarioAdministraEquipo(usuario.getId(), idEquipo)) {
            managerUserSesion.comprobarUsuarioAdministrador(session, (Boolean) session.getAttribute("administrador"));
        }

        equipoService.editarNombreEquipo(idEquipo, equipo.getNombre());

        return "redirect:/equipos";
    }

    @PostMapping("/equipos/{id}/eliminar")
    public String eliminarEquipo(@PathVariable(value="id") Long idEquipo, Model model,
                                 RedirectAttributes flash, HttpSession session){

        managerUserSesion.usuarioLogueado(session);

        Usuario usuario = usuarioService.findById((Long)session.getAttribute("idUsuarioLogeado"));
        if (usuario == null) {
            throw new UsuarioNotFoundException();
        }
        if (!equipoService.usuarioAdministraEquipo(usuario.getId(), idEquipo)) {
            managerUserSesion.comprobarUsuarioAdministrador(session, (Boolean) session.getAttribute("administrador"));
        }

        equipoService.eliminarEquipo(idEquipo);
        flash.addFlashAttribute("mensaje", "Equipo eliminado");

        return "redirect:/equipos";
    }

}
