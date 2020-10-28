package madstodolist.model;

import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface EquipoRepository extends CrudRepository<Equipo, Long> {
    Optional<Equipo> findById(Long l);
    List<Equipo> findAll();
}
