package madstodolist.controller;

import madstodolist.authentication.ManagerUserSesion;
import madstodolist.controller.exception.ProyectoNotFoundException;
import madstodolist.controller.exception.TareaNotFoundException;
import madstodolist.controller.exception.UsuarioNotFoundException;
import madstodolist.model.Categoria;
import madstodolist.model.Proyecto;
import madstodolist.model.Tarea;
import madstodolist.model.Usuario;
import madstodolist.service.CategoriaService;
import madstodolist.service.TareaService;
import madstodolist.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;
import java.util.Collections;
import java.util.List;

@Controller
public class TareaController {

    @Autowired
    UsuarioService usuarioService;

    @Autowired
    TareaService tareaService;

    @Autowired
    CategoriaService categoriaService;

    @Autowired
    ManagerUserSesion managerUserSesion;


    @GetMapping("/usuarios/{id}/tareas/nueva")
    public String formNuevaTarea(@PathVariable(value="id") Long idUsuario,
                                 @ModelAttribute TareaData tareaData, Model model,
                                 HttpSession session) {

        managerUserSesion.comprobarUsuarioLogeado(session, idUsuario);

        Usuario usuario = usuarioService.findById(idUsuario);
        if (usuario == null) {
            throw new UsuarioNotFoundException();
        }
        model.addAttribute("usuario", usuario);
        return "formNuevaTarea";
    }

    @PostMapping("/usuarios/{id}/tareas/nueva")
    public String nuevaTarea(@PathVariable(value="id") Long idUsuario, @ModelAttribute TareaData tareaData,
                             Model model, RedirectAttributes flash,
                             HttpSession session) {

        managerUserSesion.comprobarUsuarioLogeado(session, idUsuario);

        Usuario usuario = usuarioService.findById(idUsuario);
        if (usuario == null) {
            throw new UsuarioNotFoundException();
        }
        Tarea tarea = tareaService.nuevaTareaUsuario(idUsuario, tareaData.getTitulo(), tareaData.getPrioridad());
        if (tareaData.getCategoria() != null) {
            Categoria categoria = categoriaService.findById(tareaData.getCategoria());
            tareaService.asignarCategoria(tarea.getId(), categoria);
        }
        flash.addFlashAttribute("mensaje", "Tarea creada correctamente");
        return "redirect:/usuarios/" + idUsuario + "/tareas";
    }

    @GetMapping("/usuarios/{id}/tareas")
    public String listadoTareas(@PathVariable(value="id") Long idUsuario, Model model, HttpSession session) {

        managerUserSesion.comprobarUsuarioLogeado(session, idUsuario);

        Usuario usuario = usuarioService.findById(idUsuario);
        if (usuario == null) {
            throw new UsuarioNotFoundException();
        }
        List<Tarea> tareas = tareaService.allTareasUsuario(idUsuario);
        //Collections.sort(tareas, (a, b) -> a.getCategoria().getId() < b.getCategoria().getId() ? -1 : a.getId() == b.getId() ? 0 : 1);
        model.addAttribute("usuario", usuario);
        model.addAttribute("tareas", tareas);
        return "listaTareas";
    }


    @GetMapping("/usuarios/{id}/tareas/filtrar")
    public String listadoTareasFiltrarEstado(@PathVariable(value="id") Long idUsuario,
                                             @ModelAttribute(value="filtrarEstado") int filtrarEstado,
                                Model model, HttpSession session) {

        managerUserSesion.comprobarUsuarioLogeado(session, idUsuario);

        Usuario usuario = usuarioService.findById(idUsuario);
        if (usuario == null) {
            throw new UsuarioNotFoundException();
        }
        List<Tarea> tareas = tareaService.filtrarTareasPorEstado(idUsuario, filtrarEstado);
        model.addAttribute("usuario", usuario);
        model.addAttribute("tareas", tareas);

        return "listaTareas";
    }

    @GetMapping("/usuarios/{id}/tareas/excluir")
    public String listadoTareasExcluirEstado(@PathVariable(value="id") Long idUsuario,
                                             @ModelAttribute(value="excluir") int estado,
                                             Model model, HttpSession session) {

        managerUserSesion.comprobarUsuarioLogeado(session, idUsuario);

        Usuario usuario = usuarioService.findById(idUsuario);
        if (usuario == null) {
            throw new UsuarioNotFoundException();
        }
        List<Tarea> tareas = tareaService.excluirTareasPorEstado(idUsuario, estado);
        model.addAttribute("usuario", usuario);
        model.addAttribute("tareas", tareas);
        return "listaTareas";
    }

    @GetMapping("/usuarios/{id}/tareas/ordenar")
    public String orndenarTareasPrimerEstado(@PathVariable(value="id") Long idUsuario,
                                             @ModelAttribute(value="ordenar") int estado,
                                             Model model, HttpSession session) {

        managerUserSesion.comprobarUsuarioLogeado(session, idUsuario);

        Usuario usuario = usuarioService.findById(idUsuario);
        if (usuario == null) {
            throw new UsuarioNotFoundException();
        }
        List<Tarea> tareas = tareaService.orndenarTareasPrimerEstado(idUsuario, estado);
        model.addAttribute("usuario", usuario);
        model.addAttribute("tareas", tareas);
        return "listaTareas";
    }

    @GetMapping("/usuarios/{id}/tareas/buscar")
    public String buscarTareaPalabra(@PathVariable(value="id") Long idUsuario,
                                     @ModelAttribute(value="palabra") String palabra,
                                             Model model, HttpSession session) {

        managerUserSesion.comprobarUsuarioLogeado(session, idUsuario);

        Usuario usuario = usuarioService.findById(idUsuario);
        if (usuario == null) {
            throw new UsuarioNotFoundException();
        }
        List<Tarea> tareas = tareaService.filtrarTareasPorPalabra(idUsuario, palabra);
        model.addAttribute("usuario", usuario);
        model.addAttribute("tareas", tareas);
        return "listaTareas";
    }

    @GetMapping("/tareas/{id}/editar")
    public String formEditaTarea(@PathVariable(value="id") Long idTarea, @ModelAttribute TareaData tareaData,
                                 Model model, HttpSession session) {

        Tarea tarea = tareaService.findById(idTarea);
        if (tarea == null) {
            throw new TareaNotFoundException();
        }

        managerUserSesion.comprobarUsuarioLogeado(session, tarea.getUsuario().getId());

        Usuario usuario = usuarioService.findById((Long)session.getAttribute("idUsuarioLogeado"));

        model.addAttribute("usuario", usuario);

        model.addAttribute("tarea", tarea);
        tareaData.setTitulo(tarea.getTitulo());
        tareaData.setPrioridad(tarea.getPrioridad());
        return "formEditarTarea";
    }

    @PostMapping("/tareas/{id}/editar")
    public String grabaTareaModificada(@PathVariable(value="id") Long idTarea, @ModelAttribute TareaData tareaData,
                                       Model model, RedirectAttributes flash, HttpSession session) {
        Tarea tarea = tareaService.findById(idTarea);
        if (tarea == null) {
            throw new TareaNotFoundException();
        }

        managerUserSesion.comprobarUsuarioLogeado(session, tarea.getUsuario().getId());

        tareaService.modificaTarea(idTarea, tareaData.getTitulo());
        tareaService.asignarEditarPrioridad(idTarea, tareaData.getPrioridad());
        if (tareaData.getCategoria() != null) {
            Categoria categoria = categoriaService.findById(tareaData.getCategoria());
            tareaService.asignarCategoria(tarea.getId(), categoria);
        }
        if (tareaData.getBorrarCategorias()) {
            tareaService.borrarCategoriasDeTarea(tarea.getId());
        }
        tareaService.actualizarEstado(idTarea, tareaData.getEstado());
        flash.addFlashAttribute("mensaje", "Tarea modificada correctamente");

        if (tarea.getProyecto() == null)
            return "redirect:/usuarios/" + tarea.getUsuario().getId() + "/tareas";
        else
            return "redirect:/usuarios/" + tarea.getUsuario().getId() + "/proyectos/" + tarea.getProyecto().getId() + "/tareas";
    }

    @PostMapping("/tareas/{id}/estado")
    public String modificarEstadoTarea(@PathVariable(value="id") Long idTarea,
                                       @ModelAttribute(value="estado") int estado,
                                       Model model, RedirectAttributes flash, HttpSession session) {
        Tarea tarea = tareaService.findById(idTarea);
        if (tarea == null) {
            throw new TareaNotFoundException();
        }

        managerUserSesion.comprobarUsuarioLogeado(session, tarea.getUsuario().getId());

        tareaService.actualizarEstado(idTarea, estado);
        flash.addFlashAttribute("mensaje", "Estado de la tarea modificado correctamente");
        return "redirect:/usuarios/" + tarea.getUsuario().getId() + "/tareas";
    }

    @DeleteMapping("/tareas/{id}")
    @ResponseBody
    // La anotación @ResponseBody sirve para que la cadena devuelta sea la resupuesta
    // de la petición HTTP, en lugar de una plantilla thymeleaf
    public String borrarTarea(@PathVariable(value="id") Long idTarea, RedirectAttributes flash, HttpSession session) {
        Tarea tarea = tareaService.findById(idTarea);
        if (tarea == null) {
            throw new TareaNotFoundException();
        }

        managerUserSesion.comprobarUsuarioLogeado(session, tarea.getUsuario().getId());
        tareaService.borrarCategoriasDeTarea(tarea.getId());
        tareaService.borraTarea(idTarea);
        return "redirect:/usuarios/" + tarea.getUsuario().getId() + "/tareas";
    }

    @PostMapping("/tareas/{id}/eliminar")
    public String eliminarTareaProyecto(@PathVariable(value="id") Long idTarea, Model model,
                                   RedirectAttributes flash, HttpSession session){

        Tarea tarea = tareaService.findById(idTarea);
        if (tarea == null) {
            throw new TareaNotFoundException();
        }

        managerUserSesion.usuarioLogueado(session);

        managerUserSesion.comprobarUsuarioLogeado(session, tarea.getUsuario().getId());

        Usuario usuario = usuarioService.findById((Long)session.getAttribute("idUsuarioLogeado"));
        if (usuario == null) {
            throw new UsuarioNotFoundException();
        }
        tareaService.borrarCategoriasDeTarea(tarea.getId());
        tareaService.borraTarea(idTarea);

        flash.addFlashAttribute("mensaje", "Tarea eliminada correctamente");

        return "redirect:/usuarios/" + usuario.getId() + "/proyectos/" + tarea.getProyecto().getId() + "/tareas";
    }
}

