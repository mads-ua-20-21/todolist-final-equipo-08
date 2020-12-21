package madstodolist.service;

import madstodolist.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

@Service
public class ProyectoService {

    private ProyectoRepository proyectoRepository;
    private EquipoRepository equipoRepository;
    private TareaRepository tareaRepository;

    @Autowired
    public ProyectoService (ProyectoRepository proyectoRepository, EquipoRepository equipoRepository, TareaRepository tareaRepository){
        this.proyectoRepository = proyectoRepository;
        this.equipoRepository = equipoRepository;
        this.tareaRepository = tareaRepository;
    }

    @Transactional(readOnly = true)
    public Proyecto findById(Long proyectoId) {
        return proyectoRepository.findById(proyectoId).orElse(null);
    }

    @Transactional(readOnly = true)
    public List<Proyecto> findAllProyectos(){

        List<Proyecto> proyectos = proyectoRepository.findAll();
        return proyectos;
    }

    @Transactional(readOnly = true)
    public Proyecto comprobarIdProyecto(Long idProyecto){
        Proyecto proyecto = proyectoRepository.findById(idProyecto).orElse(null);
        if (proyecto == null){
            throw new ProyectoServiceException("El proyecto " + idProyecto + " no existe ");
        }
        return proyecto;
    }

    @Transactional(readOnly = true)
    public List<Tarea> tareasProyecto(Long idProyecto){

        Proyecto proyecto = comprobarIdProyecto(idProyecto);
        List<Tarea> tareas = new ArrayList(proyecto.getTareas());
        return tareas;
    }

    @Transactional
    public Proyecto nuevoProyecto (Long idEquipo, String nombre){
        Equipo equipo = equipoRepository.findById(idEquipo).orElse(null);
        if (equipo == null){
            throw new ProyectoServiceException("El equipo " + idEquipo + " no existe ");
        }

        Proyecto proyecto = new Proyecto(nombre,equipo);
        proyecto = proyectoRepository.save(proyecto);
        return proyecto;
    }

    @Transactional
    public void borrarProyecto (Long idProyecto){
        Proyecto proyecto = proyectoRepository.findById(idProyecto).orElse(null);
        if (proyecto == null){
            throw new ProyectoServiceException("El proyecto " + idProyecto + " no existe ");
        }
        proyectoRepository.delete(proyecto);
    }

    @Transactional
    public Proyecto modificarProyecto (Long idProyecto, String nombre){
        Proyecto proyecto = proyectoRepository.findById(idProyecto).orElse(null);
        if (proyecto == null){
            throw new ProyectoServiceException("El proyecto " + idProyecto + " no existe ");
        }

        proyecto.setNombre(nombre);
        proyecto = proyectoRepository.save(proyecto);

        return proyecto;
    }

    @Transactional
    public Proyecto actualizarDescripcionProyecto (Long idProyecto, String descripcion){
        Proyecto proyecto = comprobarIdProyecto(idProyecto);

        proyecto.setDescripcion(descripcion);
        proyecto = proyectoRepository.save(proyecto);

        return proyecto;
    }

    @Transactional
    public Proyecto actualizarFechaProyecto (Long idProyecto, Date fecha){
        Proyecto proyecto = comprobarIdProyecto(idProyecto);

        proyecto.setFechaLimite(fecha);
        proyecto = proyectoRepository.save(proyecto);

        return proyecto;
    }





}
