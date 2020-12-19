package madstodolist;

import madstodolist.model.Comentario;
import madstodolist.model.Equipo;
import madstodolist.model.Tarea;
import madstodolist.model.Usuario;
import madstodolist.service.ComentarioService;
import madstodolist.service.TareaService;
import madstodolist.service.UsuarioService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ComentarioServiceTest {

    @Autowired
    UsuarioService usuarioService;

    @Autowired
    TareaService tareaService;

    @Autowired
    ComentarioService comentarioService;

    @Test
    public void obtenerListadocomentarios() {
        // GIVEN
        // En el application.properties se cargan los datos de prueba del fichero datos-test.sql

        // WHEN
        List<Comentario> comentarios = comentarioService.findAllOrderedByName();

        // THEN
        assertThat(comentarios).hasSize(1);
        assertThat(comentarios.get(0).getMensaje()).isEqualTo("Primer comentario");
    }

    @Test
    @Transactional
    public void testNuevoComentario(){
        // GIVEN
        // En el application.properties se cargan los datos de prueba del fichero datos-test.sql

        // WHEN
        Usuario usuario = usuarioService.findById(1L);
        Tarea tarea = tareaService.findById(3L);

        Comentario comentario = comentarioService.nuevoComentario("Pole", usuario, tarea);


        // THEN
        assertThat(comentario).isNotNull();
        assertThat(comentario.getMensaje()).isEqualTo("Pole");
    }

}
