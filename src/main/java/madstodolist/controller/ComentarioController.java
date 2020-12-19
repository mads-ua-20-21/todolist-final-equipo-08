package madstodolist.controller;

import madstodolist.authentication.ManagerUserSesion;
import madstodolist.controller.exception.ProyectoNotContainsTareaException;
import madstodolist.controller.exception.ProyectoNotFoundException;
import madstodolist.controller.exception.TareaNotFoundException;
import madstodolist.controller.exception.UsuarioNotFoundException;
import madstodolist.model.*;
import madstodolist.service.ComentarioService;
import madstodolist.service.ProyectoService;
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
        if (!proyectoService.tareasProyecto(idProyecto).contains(tarea)){
            throw  new ProyectoNotContainsTareaException();
        }

        List<Comentario> comentarios = tareaService.comentariosTarea(idTarea);
        model.addAttribute("comentarios", comentarios);
        return "comentariosTarea";
    }
}
