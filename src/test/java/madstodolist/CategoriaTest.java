package madstodolist;


import madstodolist.model.Tarea;
import madstodolist.model.TareaRepository;
import madstodolist.model.Categoria;
import madstodolist.model.CategoriaRepository;
import madstodolist.model.Usuario;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;


@RunWith(SpringRunner.class)
@SpringBootTest
public class CategoriaTest {

    @Autowired
    CategoriaRepository categoriaRepository;

    @Autowired
    TareaRepository tareaRepository;

    //
    // Tests modelo Categoria
    //

    @Test
    public void crearCategoria() throws Exception {
        // GIVEN
        Usuario usuario = new Usuario("juan.gutierrez@gmail.com");

        // WHEN

        Categoria categoria = new Categoria("Casa");

        // THEN

        assertThat(categoria.getTitulo()).isEqualTo("Casa");
    }

}