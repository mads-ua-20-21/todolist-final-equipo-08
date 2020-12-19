package madstodolist;

import madstodolist.model.*;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ComentarioTest {

    @Autowired
    private ComentarioRepository comentarioRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private TareaRepository tareaRepository;


    @Test
    public void nuevoComentario() {
        Usuario usuario = usuarioRepository.findById(1L).orElse(null);
        Tarea tarea = tareaRepository.findById(3L).orElse(null);
        Comentario comentario = new Comentario(tarea, usuario, "Primer comentario funcionado");
        assertThat(comentario.getMensaje()).isEqualTo("Primer comentario funcionado");
    }

    @Test
    public void igualdadComentarios(){
        Usuario usuario = usuarioRepository.findById(1L).orElse(null);
        Tarea tarea = tareaRepository.findById(3L).orElse(null);

        Comentario comentario1 = new Comentario(tarea, usuario, "Primer comentario funcionado");
        Comentario comentario2 = new Comentario(tarea, usuario, "Primer comentario funcionado");
        Comentario comentario3 = new Comentario(tarea, usuario, "Segundo comentario funcionado");

        //Igualdad por mensaje
        assertThat(comentario1.getMensaje()).isEqualTo(comentario2.getMensaje());
        assertThat(comentario1.getMensaje()).isNotEqualTo(comentario3.getMensaje());

        //Asignamos IPs
        comentario1.setId(1L);
        comentario2.setId(2L);
        comentario3.setId(2L);

        //Desigualdad por ID
        assertThat(comentario1).isNotEqualTo(comentario2);
        assertThat(comentario1.getId()).isNotEqualTo(comentario3.getId());
    }

    @Test
    @Transactional
    public void guardarComentarioBD(){
        //GIVEN
        Usuario usuario = usuarioRepository.findById(1L).orElse(null);
        Tarea tarea = tareaRepository.findById(3L).orElse(null);
        Comentario comentario = new Comentario(tarea, usuario, "Primer comentario funcionado");

        // WHEN
        comentario = comentarioRepository.save(comentario);

        // THEN
        assertThat(comentario.getId()).isNotNull();
    }

    @Test
    public void recuperacionComentarioBD(){
        // GIVEN
        // En el application.properties se cargan los datos de prueba del fichero datos-test.sql

        //WHEN
        Comentario comentario = comentarioRepository.findById(1L).orElse(null);

        //THEN
        assertThat(comentario).isNotNull();
        assertThat(comentario.getId()).isEqualTo(1L);
        assertThat(comentario.getMensaje()).isEqualTo("Primer comentario");
    }

    @Test
    public void comprobarRelacionConUsuario(){
        // GIVEN
        // En el application.properties se cargan los datos de prueba del fichero datos-test.sql
        Usuario usuario = usuarioRepository.findById(1L).orElse(null);

        //WHEN
        Comentario comentario = comentarioRepository.findById(1L).orElse(null);

        //THEN
        assertThat(comentario).isNotNull();
        assertThat(comentario.getUsuario()).isEqualTo(usuario);
    }

    @Test
    public void comprobarRelacionConTarea(){
        // GIVEN
        // En el application.properties se cargan los datos de prueba del fichero datos-test.sql
        Usuario usuario = usuarioRepository.findById(1L).orElse(null);
        Tarea tarea = new Tarea(usuario,"Crear mockups");

        //WHEN
        Comentario comentario = comentarioRepository.findById(1L).orElse(null);

        //THEN
        assertThat(comentario).isNotNull();
        assertThat(comentario.getTarea()).isEqualTo(tarea);
    }
}