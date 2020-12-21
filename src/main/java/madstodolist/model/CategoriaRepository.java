package madstodolist.model;

import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface CategoriaRepository extends CrudRepository<Categoria, Long>{
    public List<Categoria> findAll();
}
