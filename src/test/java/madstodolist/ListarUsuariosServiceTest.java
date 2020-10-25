package madstodolist;

import madstodolist.model.Usuario;
import madstodolist.model.UsuarioRepository;
import madstodolist.service.UsuarioService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ListarUsuariosServiceTest {
    @Autowired
    UsuarioService usuarioService;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Test
    @Transactional
    public void CrearListarBorrarYListarNuevoUsuario() throws Exception {
        // GIVEN
        Usuario usuario = new Usuario("atm64@alu");
        usuario.setNombre("Andres Tebar");
        usuario.setPassword("1234");
        usuario.setId(2L);

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        usuario.setFechaNacimiento(sdf.parse("1996-12-19"));

        // WHEN
        usuarioRepository.save(usuario);
        List<Usuario> usuarios = usuarioService.allUsuarios();

        // THEN
        assertThat(usuarios.size()).isEqualTo(2);
        assertThat(usuarios.get(0).getNombre()).isEqualTo("Ana García Error");
        assertThat(usuarios.get(1).getNombre()).isEqualTo(usuario.getNombre());

        //AND
        usuarioRepository.delete(usuarios.get(1));
        usuarios = usuarioService.allUsuarios();
        assertThat(usuarios.size()).isEqualTo(1);
        assertThat(usuarios.get(0).getNombre()).isEqualTo("Ana García");
    }

}
