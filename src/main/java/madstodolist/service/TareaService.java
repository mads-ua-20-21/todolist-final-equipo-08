package madstodolist.service;

import madstodolist.model.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.thymeleaf.util.ListUtils;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class TareaService {

    Logger logger = LoggerFactory.getLogger(TareaService.class);

    private UsuarioRepository usuarioRepository;
    private TareaRepository tareaRepository;
    private ProyectoRepository proyectoRepository;

    @Autowired
    public TareaService(UsuarioRepository usuarioRepository, TareaRepository tareaRepository, ProyectoRepository proyectoRepository) {
        this.usuarioRepository = usuarioRepository;
        this.tareaRepository = tareaRepository;
        this.proyectoRepository = proyectoRepository;
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

    @Transactional
    public Tarea nuevaTareaUsuario(Long idUsuario, Long idProyecto, String tituloTarea, Integer prioridad) {
        Usuario usuario = usuarioRepository.findById(idUsuario).orElse(null);
        if (usuario == null) {
            throw new TareaServiceException("Usuario " + idUsuario + " no existe al crear tarea " + tituloTarea);
        }
        Proyecto proyecto = proyectoRepository.findById(idProyecto).orElse(null);
        Tarea tarea = new Tarea(usuario, proyecto, tituloTarea, prioridad);
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
    public List<Tarea> filtrarTareasPorPalabra(Long idUsuario, String palabra) {
        List<Tarea> tareas = allTareasUsuario(idUsuario);
        if (palabra != ""){
         tareas = allTareasUsuario(idUsuario).stream()
                .filter(tarea -> tarea.getTitulo().toLowerCase(Locale.ROOT)
                        .contains(palabra.toLowerCase(Locale.ROOT)))
                .collect(Collectors.toList());
        }
        return tareas;
    }

    @Transactional(readOnly = true)
    public List<Tarea> filtrarTareasPorEstado(Long idUsuario, int estadoInt) {
        return allTareasUsuario(idUsuario).stream()
                .filter(tarea -> tarea.getEstado().ordinal() == estadoInt)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<Tarea> excluirTareasPorEstado(Long idUsuario, int estadoInt) {
        return allTareasUsuario(idUsuario).stream()
                .filter(tarea -> tarea.getEstado().ordinal() != estadoInt)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<Tarea> orndenarTareasPrimerEstado(Long idUsuario, int estadoInt) {
        return Stream.concat(
                filtrarTareasPorEstado(idUsuario, estadoInt).stream(),
                excluirTareasPorEstado(idUsuario, estadoInt).stream())
                .collect(Collectors.toList());
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

    @Transactional(readOnly = true)
    public Proyecto proyectoTarea(Long idTarea){
        Tarea tarea = tareaRepository.findById(idTarea).orElse(null);
        if (tarea == null) {
            throw new TareaServiceException("No existe tarea con id " + idTarea);
        }
       return tarea.getProyecto();
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

    @Transactional
    public Tarea actualizarEstado(Long idTarea, int nuevoEstado){
        Tarea tarea = tareaRepository.findById(idTarea).orElse(null);
        if (tarea == null) {
            throw new TareaServiceException("No existe tarea con id " + idTarea);
        }
        tarea.setEstado(enteroAEstado(nuevoEstado));
        tareaRepository.save(tarea);
        return tarea;
    }

    public Tarea.EstadoTarea enteroAEstado(int estadoInt) {
        Tarea.EstadoTarea estado = Tarea.EstadoTarea.PENDIENTE;
        if (estadoInt == 1) estado = Tarea.EstadoTarea.ACTIVA;
        else if (estadoInt == 2) estado = Tarea.EstadoTarea.TERMINADA;
        return estado;
    }

    @Transactional(readOnly = true)
    public List<Comentario> comentariosTarea(Long idTarea){

        Tarea tarea = tareaRepository.findById(idTarea).orElse(null);
        if (tarea == null) {
            throw new TareaServiceException("No existe tarea con id " + idTarea);
        }
        List<Comentario> comentarios = new ArrayList(tarea.getComentarios());
        comentarios.sort(Comparator.comparing(Comentario::getFechaHora));
        return comentarios;
    }
}
