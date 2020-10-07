package madstodolist;

import madstodolist.model.Usuario;
import madstodolist.model.UsuarioRepository;
import madstodolist.service.UsuarioService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class ListaUsuariosWebTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UsuarioService usuarioService;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Test
    //@Transactional
    public void accesoSinLogin() throws Exception {

        // GIVEN
        /*Usuario usuario = new Usuario("atm64@alu");
        usuario.setNombre("Andres Tebar");
        usuario.setPassword("1234");

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        usuario.setFechaNacimiento(sdf.parse("1996-12-19"));*/

        // WHEN

        //usuarioRepository.save(usuario);

        this.mockMvc.perform(get("/usuarios"));
                //.andExpect(result -> assertEquals("resource not found", result.getResolvedException().getMessage()));
                //.andDo(print())
                //.andExpect(content().string(containsString("Usuario no autorizado")));

    }

    @Test
    @Transactional
    public void mostrarUsuarios() throws Exception{
        // GIVEN
        Usuario usuario = new Usuario("atm64@alu");
        usuario.setNombre("Andres Tebar");
        usuario.setPassword("1234");
        usuario.setId(2L);

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        usuario.setFechaNacimiento(sdf.parse("1996-12-19"));

        // WHEN

        usuarioRepository.save(usuario);

        // THEN
        this.mockMvc.perform(post("/login")
                .param("eMail", "atm64@alu")
                .param("password", "1234"))
                .andDo(print());
                //.andExpect(content().string(containsString("Listado de tareas de Andres")));
                //.andExpect(status().is3xxRedirection())
                //.andExpect(redirectedUrl("/usuarios/1/tareas"));

        this.mockMvc.perform(get("/usuarios"))
                .andDo(print())
                .andExpect(content().string(containsString("Andres Tebar")));
    }

    /*@Test
    @Transactional
    public void registroYMostrarUsuarios(){

    }*/
}
