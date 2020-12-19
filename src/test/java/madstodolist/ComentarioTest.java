package madstodolist;

import madstodolist.model.*;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.transaction.Transactional;

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
}