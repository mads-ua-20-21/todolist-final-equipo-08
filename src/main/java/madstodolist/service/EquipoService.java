package madstodolist.service;

import madstodolist.model.Equipo;
import madstodolist.model.EquipoRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.AbstractList;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;


@Service
public class EquipoService {

    Logger logger = LoggerFactory.getLogger(EquipoService.class);

    private EquipoRepository equipoRepository;

    @Autowired
    public EquipoService(EquipoRepository equipoRepository){ this.equipoRepository = equipoRepository;}

    @Transactional(readOnly = true)
    public Equipo findById(Long equipoId) {
        return equipoRepository.findById(equipoId).orElse(null);
    }

    @Transactional(readOnly = true)
    public List<Equipo> findAllOrderedByName(){
        List<Equipo> equipos = new ArrayList<Equipo>();
        equipoRepository.findAll().forEach(equipos::add);
        equipos.sort(Comparator.comparing(Equipo::getNombre).reversed());
        return equipos;
    }

}
