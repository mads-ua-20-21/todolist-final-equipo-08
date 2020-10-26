package madstodolist.service;

import madstodolist.model.Equipo;
import madstodolist.model.EquipoRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class EquipoService {

    Logger logger = LoggerFactory.getLogger(EquipoService.class);

    private EquipoRepository equipoRepository;

    @Autowired
    public EquipoService(EquipoRepository equipoRepository){ this.equipoRepository = equipoRepository;}

    @Transactional(readOnly = true)
    public Equipo findById(Long id) {
        return equipoRepository.findById(id).orElse(null);
    }
}
