package madstodolist;


import madstodolist.authentication.ManagerUserSesion;
import madstodolist.model.Equipo;
import madstodolist.model.Usuario;
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
public class EquipoWebTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UsuarioService usuarioService;

    @MockBean
    private EquipoService equipoService;

    @MockBean
    private ManagerUserSesion managerUserSesion;

    /*@Test
    public void testListaEquipos() throws Exception {
        Usuario usuario = new Usuario("andres@ua.es");
        usuario.setId(1L);

        List<Equipo> equipos = new ArrayList<>();
        Equipo equipo = new Equipo("Proyecto P1", usuario);
        equipos.add(equipo);

        when(usuarioService.findById(null)).thenReturn(usuario);
        when(equipoService.findAllOrderedByName()).thenReturn(equipos);

        this.mockMvc.perform(get("/equipos"))
                .andDo(print())
                .andExpect(content().string(containsString("Proyecto P1")));
    }

    @Test
    public void testListarUsuariosEquipo() throws Exception {
        Usuario usuario = new Usuario("andres@ua.es");
        usuario.setId(1L);
        usuario.setNombre("Andres Tebar");

        List<Usuario> usuarios = new ArrayList<>();
        usuarios.add(usuario);

        Equipo equipo = new Equipo("Proyecto P1", usuario);
        equipo.setId(1L);

        when(usuarioService.findById(null)).thenReturn(usuario);
        when(equipoService.findById(1L)).thenReturn(equipo);
        when(equipoService.usuariosEquipo(1L)).thenReturn(usuarios);

        this.mockMvc.perform(get("/equipos/1"))
                .andDo(print())
                .andExpect(content().string(containsString("Andres Tebar")));
    }*/

    @Test
    public void nuevoEquipoDevuelveForm() throws Exception {
        Usuario usuario = new Usuario("andres@ua.es");
        usuario.setId(1L);

        when(usuarioService.findById(null)).thenReturn(usuario);

        this.mockMvc.perform(get("/equipos/nuevo"))
                .andDo(print())
                .andExpect(content().string(containsString("Nuevo Equipo")));
    }

    @Test
    public void crearNuevoEquipo() throws Exception {
        Usuario usuario = new Usuario("andres@ua.es");
        usuario.setId(1L);

        when(usuarioService.findById(null)).thenReturn(usuario);

        this.mockMvc.perform(post("/equipos/nuevo")
                .param("Nombre", "EquipoTest"))
                .andDo(print())
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/equipos"));

    }

    @Test
    public void testUnirseAEquipo() throws Exception {

        Usuario usuario = new Usuario("andres@ua.es");
        usuario.setId(1L);

        Equipo equipo = new Equipo("Proyecto P1");
        equipo.setId(1L);

        when(usuarioService.findById(null)).thenReturn(usuario);
        when(equipoService.anyadirUsuarioAEquipo(1L, 1L)).thenReturn(true);

        this.mockMvc.perform(post("/equipos/1"))
                .andDo(print())
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/equipos/1"));
    }

    @Test
    public void testSalirDeEquipo() throws Exception {

        Usuario usuario = new Usuario("andres@ua.es");
        usuario.setId(1L);

        Equipo equipo = new Equipo("Proyecto P1");
        equipo.setId(1L);

        when(usuarioService.findById(null)).thenReturn(usuario);
        when(equipoService.eliminarUsuarioDeEquipo(1L, 1L)).thenReturn(true);

        this.mockMvc.perform(post("/equipos/1/salir"))
                .andDo(print())
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/equipos/1"));
    }

    @Test
    public void testMostrarEditarForm() throws Exception{
        Usuario usuario = new Usuario("andres@ua.es");
        usuario.setId(1L);
        usuario.setAdministrador(true);

        Equipo equipo = new Equipo("EquipoTest");
        equipo.setId(1L);

        when(usuarioService.findById(null)).thenReturn(usuario);
        when(equipoService.findById(1L)).thenReturn(equipo);

        this.mockMvc.perform(get("/equipos/1/editar"))
                .andDo(print())
                .andExpect(content().string(containsString("Editar nombre del Equipo: EquipoTest")));

    }

    @Test
    public void testEditarEquipo() throws Exception{

        Usuario usuario = new Usuario("andres@ua.es");
        usuario.setId(1L);
        usuario.setAdministrador(true);

        Equipo equipo = new Equipo("EquipoTest");
        equipo.setId(1L);

        when(usuarioService.findById(null)).thenReturn(usuario);

        this.mockMvc.perform(post("/equipos/1/editar"))
                .andDo(print())
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/equipos"));
    }

    @Test
    public void testEliminarEquipo() throws Exception{

        Usuario usuario = new Usuario("andres@ua.es");
        usuario.setId(1L);
        usuario.setAdministrador(true);

        Equipo equipo = new Equipo("EquipoTest");
        equipo.setId(1L);

        when(usuarioService.findById(null)).thenReturn(usuario);

        this.mockMvc.perform(post("/equipos/1/eliminar"))
                .andDo(print())
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/equipos"));
    }
}
