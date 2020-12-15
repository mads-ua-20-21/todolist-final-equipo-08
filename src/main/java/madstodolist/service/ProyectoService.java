package madstodolist.service;

import madstodolist.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    public Proyecto comprobarIdProyecto(Long idProyecto){
        Proyecto proyecto = proyectoRepository.findById(idProyecto).orElse(null);
        if (proyecto == null){
            throw new EquipoServiceException("Equipo " + idProyecto + " no existe ");
        }
        return proyecto;
    }
}
