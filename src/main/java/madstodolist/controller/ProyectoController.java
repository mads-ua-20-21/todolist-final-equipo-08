package madstodolist.controller;

import madstodolist.authentication.ManagerUserSesion;
import madstodolist.authentication.UsuarioNoAdminException;
import madstodolist.controller.exception.EquipoNotFoundException;
import madstodolist.controller.exception.ProyectoNotFoundException;
import madstodolist.controller.exception.TareaNotFoundException;
import madstodolist.controller.exception.UsuarioNotFoundException;
import madstodolist.model.Equipo;
import madstodolist.model.Proyecto;
import madstodolist.model.Tarea;
import madstodolist.model.Usuario;
import madstodolist.service.EquipoService;
import madstodolist.service.ProyectoService;
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
public class ProyectoController {

    @Autowired
    ProyectoService proyectoService;

    @Autowired
    EquipoService equipoService;

    @Autowired
    TareaService tareaService;

    @Autowired
    UsuarioService usuarioService;

    @Autowired
    ManagerUserSesion managerUserSesion;

    @GetMapping("/usuarios/{id}/proyectos")
    public String listarProyectosUsuario(@PathVariable(value="id") Long idUsuario,
                                Model model, HttpSession session){

        managerUserSesion.comprobarUsuarioLogeado(session, idUsuario);

        Usuario usuario = usuarioService.findById((Long)session.getAttribute("idUsuarioLogeado"));
        if (usuario == null) {
            throw new UsuarioNotFoundException();
        }
        model.addAttribute("usuario", usuario);

        List<Proyecto> proyectos = usuarioService.proyectosUsuario(idUsuario);
        model.addAttribute("proyectos", proyectos);
        return "listaProyectos";
    }

    @GetMapping("/proyectos")
    public String listarProyectos(Model model, HttpSession session){

        managerUserSesion.usuarioLogueado(session);
        Usuario usuario = usuarioService.findById((Long)session.getAttribute("idUsuarioLogeado"));
        if (usuario == null) {
            throw new UsuarioNotFoundException();
        }
        managerUserSesion.comprobarUsuarioAdministrador(session,(Boolean) session.getAttribute("administrador"));
        model.addAttribute("usuario", usuario);

        List<Proyecto> proyectos = proyectoService.findAllProyectos();
        model.addAttribute("proyectos", proyectos);
        return "listaProyectos";
    }

    @GetMapping("/usuarios/{id}/proyectos/{proyecto}/tareas")
    public String tareasProyecto(@PathVariable(value="id") Long idUsuario,
                                 @PathVariable(value="proyecto") Long idProyecto,
                                 Model model, HttpSession session){

        managerUserSesion.comprobarUsuarioLogeado(session, idUsuario);
        Usuario usuario = usuarioService.findById((Long)session.getAttribute("idUsuarioLogeado"));
        if (usuario == null) {
            throw new UsuarioNotFoundException();
        }
        model.addAttribute("usuario", usuario);

        Proyecto proyecto = proyectoService.findById(idProyecto);
        model.addAttribute("proyecto", proyecto);
        Equipo equipo = proyecto.getEquipo();
        model.addAttribute("equipo", equipo);

        List<Tarea> tareas = proyectoService.tareasProyecto(idProyecto);
        model.addAttribute("tareas", tareas);
        return "tareasProyecto";
    }

    @GetMapping("/equipos/{id}/proyectos/nuevo")
    public String formNuevoProyecto(@PathVariable(value="id") Long idEquipo,
                                 @ModelAttribute Proyecto proyecto, Model model,
                                 HttpSession session) {

        managerUserSesion.usuarioLogueado(session);
        Usuario usuario = usuarioService.findById((Long)session.getAttribute("idUsuarioLogeado"));
        if (usuario == null) {
            throw new UsuarioNotFoundException();
        }

        Equipo equipo = equipoService.findById(idEquipo);
        if (equipo == null){
            throw new EquipoNotFoundException();
        }

        //Comprobamos que el usuario logeado sea el admin del equipo o el admin de la aplicacion
        if (!equipo.getUsuarioAdministrador().equals(usuario)){
            managerUserSesion.comprobarUsuarioAdministrador(session,(Boolean) session.getAttribute("administrador"));
        }

        model.addAttribute("equipo",equipo);
        model.addAttribute("usuario", usuario);
        return "formNuevoProyecto";
    }

    @PostMapping("/equipos/{id}/proyectos/nuevo")
    public String nuevoProyecto(@PathVariable(value="id") Long idEquipo,
                                @ModelAttribute Proyecto proyecto,
                             Model model, RedirectAttributes flash,
                             HttpSession session) {

        managerUserSesion.usuarioLogueado(session);
        Usuario usuario = usuarioService.findById((Long)session.getAttribute("idUsuarioLogeado"));
        if (usuario == null) {
            throw new UsuarioNotFoundException();
        }

        Equipo equipo = equipoService.findById(idEquipo);
        if (equipo == null){
            throw new EquipoNotFoundException();
        }

        //Comprobamos que el usuario logeado sea el admin del equipo
        if (!equipo.getUsuarioAdministrador().equals(usuario)){
            managerUserSesion.comprobarUsuarioAdministrador(session,(Boolean) session.getAttribute("administrador"));
        }

        proyectoService.nuevoProyecto(idEquipo,equipo.getNombre());
        flash.addFlashAttribute("mensaje", "Proyecto creada correctamente");
        return "redirect:/equipos/" + idEquipo;
    }

    @GetMapping("/proyectos/{id}/editar")
    public String formEditaProyecto(@PathVariable(value="id") Long idProyecto,
                                    @ModelAttribute Proyecto proyecto,
                                 Model model, HttpSession session) {

        Proyecto proyectoMod = proyectoService.findById(idProyecto);
        if (proyectoMod == null) {
            throw new ProyectoNotFoundException();
        }

        managerUserSesion.usuarioLogueado(session);
        Usuario usuario = usuarioService.findById((Long)session.getAttribute("idUsuarioLogeado"));
        if (usuario == null) {
            throw new UsuarioNotFoundException();
        }

        //Comprobamos que el usuario logeado es el administrador del equipo del proyecto
        if (!proyectoMod.getEquipo().getUsuarioAdministrador().equals(usuario)){
            managerUserSesion.comprobarUsuarioAdministrador(session,(Boolean) session.getAttribute("administrador"));
        }
        model.addAttribute("usuario", usuario);

        proyecto.setNombre(proyectoMod.getNombre());
        model.addAttribute("proyecto", proyecto);

        return "formEditarProyecto";
    }

    @PostMapping("/proyectos/{id}/editar")
    public String guardarProyectoModificado(@PathVariable(value="id") Long idProyecto,
                                       @ModelAttribute Proyecto proyecto,
                                       Model model, RedirectAttributes flash, HttpSession session) {

        Proyecto proyectoMod = proyectoService.findById(idProyecto);
        if (proyectoMod == null) {
            throw new ProyectoNotFoundException();
        }

        managerUserSesion.usuarioLogueado(session);
        Usuario usuario = usuarioService.findById((Long)session.getAttribute("idUsuarioLogeado"));
        if (usuario == null) {
            throw new UsuarioNotFoundException();
        }

        //Comprobamos que el usuario logeado es el administrador del equipo del proyecto
        if (!proyectoMod.getEquipo().getUsuarioAdministrador().equals(usuario)){
            managerUserSesion.comprobarUsuarioAdministrador(session,(Boolean) session.getAttribute("administrador"));
        }
        model.addAttribute("usuario", usuario);

        proyectoService.modificarProyecto(idProyecto,proyecto.getNombre());
        flash.addFlashAttribute("mensaje", "Proyecto modificado correctamente");
        return "redirect:/equipos/" + proyectoMod.getEquipo().getId();
    }

    @PostMapping("/proyectos/{id}/eliminar")
    public String eliminarProyecto(@PathVariable(value="id") Long idProyecto, Model model,
                                 RedirectAttributes flash, HttpSession session){

        Proyecto proyecto = proyectoService.findById(idProyecto);
        if (proyecto == null) {
            throw new ProyectoNotFoundException();
        }

        managerUserSesion.usuarioLogueado(session);

        Usuario usuario = usuarioService.findById((Long)session.getAttribute("idUsuarioLogeado"));
        if (usuario == null) {
            throw new UsuarioNotFoundException();
        }

        //Comprobamos que el usuario logeado es el administrador del equipo del proyecto
        if (!proyecto.getEquipo().getUsuarioAdministrador().equals(usuario)){
            managerUserSesion.comprobarUsuarioAdministrador(session,(Boolean) session.getAttribute("administrador"));
        }

        proyectoService.borrarProyecto(idProyecto);
        flash.addFlashAttribute("mensaje", "Proyecto eliminado correctamente");

        if ((Boolean) session.getAttribute("administrador")){
            return "redirect:/proyectos";
        }
        return "redirect:/usuarios/"+ usuario.getId() + "/proyectos";
    }

    @GetMapping("/usuarios/{id}/proyectos/{proyecto}/tareas/nueva")
    public String formNuevaTareaProyecto(@PathVariable(value="id") Long idUsuario,
                                 @PathVariable(value="proyecto") Long idProyecto,
                                 @ModelAttribute TareaData tareaData, Model model,
                                 HttpSession session) {

        managerUserSesion.comprobarUsuarioLogeado(session, idUsuario);

        Usuario usuario = usuarioService.findById(idUsuario);
        if (usuario == null) {
            throw new UsuarioNotFoundException();
        }
        Proyecto proyecto = proyectoService.findById(idProyecto);
        if (proyecto == null){
            throw new ProyectoNotFoundException();
        }

        model.addAttribute("proyecto",proyecto);
        model.addAttribute("usuario", usuario);
        return "formNuevaTarea";
    }

    @PostMapping("/usuarios/{id}/proyectos/{proyecto}/tareas/nueva")
    public String nuevaTareaProyecto(@PathVariable(value="id") Long idUsuario,
                             @PathVariable(value="proyecto") Long idProyecto,
                             @ModelAttribute TareaData tareaData,
                             Model model, RedirectAttributes flash,
                             HttpSession session) {

        managerUserSesion.comprobarUsuarioLogeado(session, idUsuario);

        Usuario usuario = usuarioService.findById(idUsuario);
        if (usuario == null) {
            throw new UsuarioNotFoundException();
        }
        Proyecto proyecto = proyectoService.findById(idProyecto);
        if (proyecto == null){
            throw new ProyectoNotFoundException();
        }

        tareaService.nuevaTareaUsuario(idUsuario, idProyecto, tareaData.getTitulo(), tareaData.getPrioridad());
        flash.addFlashAttribute("mensaje", "Tarea de proyecto creada correctamente");
        return "redirect:/usuarios/" + idUsuario + "/proyectos/" + idProyecto + "/tareas";
    }
}
