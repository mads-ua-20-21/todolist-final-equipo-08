package madstodolist.service;

import madstodolist.model.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
public class TareaService {

    Logger logger = LoggerFactory.getLogger(TareaService.class);

    private UsuarioRepository usuarioRepository;
    private TareaRepository tareaRepository;

    @Autowired
    public TareaService(UsuarioRepository usuarioRepository, TareaRepository tareaRepository) {
        this.usuarioRepository = usuarioRepository;
        this.tareaRepository = tareaRepository;
    }
    @Transactional
    public Tarea nuevaTareaUsuario(Long idUsuario, String tituloTarea) {
        Usuario usuario = usuarioRepository.findById(idUsuario).orElse(null);
        if (usuario == null) {
            throw new TareaServiceException("Usuario " + idUsuario + " no existe al crear tarea " + tituloTarea);
        }
        Tarea tarea = new Tarea(usuario, tituloTarea);
        tareaRepository.save(tarea);
        return tarea;
    }

    @Transactional
    public Tarea nuevaTareaUsuario(Long idUsuario, String tituloTarea, Integer prioridad) {
        Usuario usuario = usuarioRepository.findById(idUsuario).orElse(null);
        if (usuario == null) {
            throw new TareaServiceException("Usuario " + idUsuario + " no existe al crear tarea " + tituloTarea);
        }
        Tarea tarea = new Tarea(usuario, tituloTarea, prioridad);
        tareaRepository.save(tarea);
        return tarea;
    }

    @Transactional(readOnly = true)
    public List<Tarea> allTareasUsuario(Long idUsuario) {
        Usuario usuario = usuarioRepository.findById(idUsuario).orElse(null);
        if (usuario == null) {
            throw new TareaServiceException("Usuario " + idUsuario + " no existe al listar tareas ");
        }
        List<Tarea> tareas = new ArrayList(usuario.getTareas());
        Collections.sort(tareas, (a, b) -> a.getId() < b.getId() ? -1 : a.getId() == b.getId() ? 0 : 1);
        return tareas;
    }

    @Transactional(readOnly = true)
    public Tarea findById(Long tareaId) {
        return tareaRepository.findById(tareaId).orElse(null);
    }

    @Transactional
    public Tarea modificaTarea(Long idTarea, String nuevoTitulo) {
        Tarea tarea = tareaRepository.findById(idTarea).orElse(null);
        if (tarea == null) {
            throw new TareaServiceException("No existe tarea con id " + idTarea);
        }
        tarea.setTitulo(nuevoTitulo);
        tareaRepository.save(tarea);
        return tarea;
    }

    @Transactional
    public void borraTarea(Long idTarea) {
        Tarea tarea = tareaRepository.findById(idTarea).orElse(null);
        if (tarea == null) {
            throw new TareaServiceException("No existe tarea con id " + idTarea);
        }
        tareaRepository.delete(tarea);
    }

    @Transactional
    public void asignarEditarPrioridad(Long idTarea, Integer prioridad) {
        Tarea tarea = tareaRepository.findById(idTarea).orElse(null);
        if (tarea == null) {
            throw new TareaServiceException("No existe tarea con id " + idTarea);
        }
        tarea.setPrioridad(prioridad);
        tareaRepository.save(tarea);
    }

    @Transactional
    public void asignarCategoria(Long idTarea, Categoria categoria) {
        Tarea tarea = tareaRepository.findById(idTarea).orElse(null);
        if (tarea == null) {
            throw new TareaServiceException("No existe tarea con id " + idTarea);
        }
        tarea.getCategoria().add(categoria);
        categoria.getTareas().add(tarea);
        tareaRepository.save(tarea);
    }

    @Transactional
    public void borrarCategoriasDeTarea(Long idTarea) {
        Tarea tarea = tareaRepository.findById(idTarea).orElse(null);
        if (tarea == null) {
            throw new TareaServiceException("No existe tarea con id " + idTarea);
        }
        tarea.getCategoria().forEach(categoria -> categoria.getTareas().remove(tarea));
        tarea.getCategoria().removeAll(tarea.getCategoria());
        tareaRepository.save(tarea);
    }
}
