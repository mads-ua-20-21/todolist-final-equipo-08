package madstodolist.controller;

import madstodolist.authentication.ManagerUserSesion;
import madstodolist.controller.exception.*;
import madstodolist.model.*;
import madstodolist.service.ComentarioService;
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
public class ComentarioController {

    @Autowired
    TareaService tareaService;

    @Autowired
    ManagerUserSesion managerUserSesion;

    @Autowired
    ComentarioService comentarioService;

    @Autowired
    UsuarioService usuarioService;

    @Autowired
    ProyectoService proyectoService;

    @GetMapping("/usuarios/{id}/proyectos/{proyecto}/tareas/{tarea}")
    public String comentariosTarea(@PathVariable(value="id") Long idUsuario,
                                 @PathVariable(value="proyecto") Long idProyecto,
                                 @PathVariable(value="tarea") Long idTarea,
                                   @ModelAttribute Comentario comentario,
                                 Model model, HttpSession session){

        managerUserSesion.comprobarUsuarioLogeado(session, idUsuario);
        Usuario usuario = usuarioService.findById((Long)session.getAttribute("idUsuarioLogeado"));
        if (usuario == null) {
            throw new UsuarioNotFoundException();
        }
        model.addAttribute("usuario", usuario);

        Proyecto proyecto = proyectoService.findById(idProyecto);
        if (proyecto == null){
            throw new ProyectoNotFoundException();
        }
        Tarea tarea = tareaService.findById(idTarea);
        if (tarea == null) {
            throw new TareaNotFoundException();
        }
        model.addAttribute("tarea", tarea);
        if (!proyectoService.tareasProyecto(idProyecto).contains(tarea)){
            throw  new ProyectoNotContainsTareaException();
        }

        List<Comentario> comentarios = tareaService.comentariosTarea(idTarea);
        model.addAttribute("comentarios", comentarios);
        return "comentariosTarea";
    }

    @PostMapping("/usuarios/{id}/proyectos/{proyecto}/tareas/{tarea}")
    public String nuevoComentario(@PathVariable(value="id") Long idUsuario,
                                  @PathVariable(value="proyecto") Long idProyecto,
                                  @PathVariable(value="tarea") Long idTarea,
                                @ModelAttribute Comentario comentario,
                                Model model, RedirectAttributes flash,
                                HttpSession session) {

        managerUserSesion.comprobarUsuarioLogeado(session, idUsuario);
        Usuario usuario = usuarioService.findById((Long)session.getAttribute("idUsuarioLogeado"));
        if (usuario == null) {
            throw new UsuarioNotFoundException();
        }
        model.addAttribute("usuario", usuario);

        Proyecto proyecto = proyectoService.findById(idProyecto);
        if (proyecto == null){
            throw new ProyectoNotFoundException();
        }
        Tarea tarea = tareaService.findById(idTarea);
        if (tarea == null) {
            throw new TareaNotFoundException();
        }
        if (!proyectoService.tareasProyecto(idProyecto).contains(tarea)){
            throw  new ProyectoNotContainsTareaException();
        }

        comentarioService.nuevoComentario(comentario.getMensaje(),usuario,tarea);
        flash.addFlashAttribute("mensaje", "Comentario creado correctamente");
        return "redirect:/usuarios/" + idUsuario + "/proyectos/" + idProyecto + "/tareas/" + idTarea;
    }

    @PostMapping("/comentarios/{id}/eliminar")
    public String eliminarComentario(@PathVariable(value="id") Long idComentario, Model model,
                                   RedirectAttributes flash, HttpSession session){

        Comentario comentario = comentarioService.findById(idComentario);
        if (comentario == null) {
            throw new ComentarioNotFoundException();
        }

        managerUserSesion.usuarioLogueado(session);

        Usuario usuario = usuarioService.findById((Long)session.getAttribute("idUsuarioLogeado"));
        if (usuario == null) {
            throw new UsuarioNotFoundException();
        }

        //Comprobamos que el usuario logeado es el creador del comentario
        if (!comentario.getUsuario().equals(usuario)){
            managerUserSesion.comprobarUsuarioAdministrador(session,(Boolean) session.getAttribute("administrador"));
        }

        comentarioService.eliminarComentario(usuario.getId(),idComentario);
        flash.addFlashAttribute("mensaje", "Comentario eliminado correctamente");

        return "redirect:/usuarios/" + usuario.getId() +
                "/proyectos/" + comentario.getTarea().getProyecto().getId() +
                "/tareas/" + comentario.getTarea().getId();
    }
}
