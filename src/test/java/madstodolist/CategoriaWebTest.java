package madstodolist;


import madstodolist.authentication.ManagerUserSesion;
import madstodolist.model.Categoria;
import madstodolist.model.Equipo;
import madstodolist.model.Usuario;
import madstodolist.service.CategoriaService;
import madstodolist.service.EquipoService;
import madstodolist.service.UsuarioService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class CategoriaWebTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UsuarioService usuarioService;

    @MockBean
    private CategoriaService categoriaService;

    @MockBean
    private ManagerUserSesion managerUserSesion;

    @Test
    public void testListaCategoria() throws Exception {
        Usuario usuario = new Usuario("andres@ua.es");
        usuario.setId(1L);

        List<Categoria> categorias = new ArrayList<>();
        Categoria categoria = new Categoria("Casa", usuario);
        categorias.add(categoria);

        when(usuarioService.findById(null)).thenReturn(usuario);
        when(categoriaService.allCategoriasUsuario(1L)).thenReturn(categorias);

        this.mockMvc.perform(get("/usuarios/1/categoria"))
                .andDo(print())
                .andExpect(content().string(containsString("Casa")));
    }

    @Test
    public void nuevaCategoriaDevuelveForm() throws Exception {
        Usuario usuario = new Usuario("domingo@ua.es");
        usuario.setId(1L);

        when(usuarioService.findById(1L)).thenReturn(usuario);

        this.mockMvc.perform(get("/usuarios/1/categoria/nueva"))
                .andDo(print())
                .andExpect(content().string(containsString("action=\"/usuarios/1/categoria/nueva\"")));
    }
}