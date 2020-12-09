package madstodolist;

import madstodolist.model.Equipo;
import madstodolist.model.Usuario;
import madstodolist.service.EquipoService;
import madstodolist.service.UsuarioService;
import org.hibernate.LazyInitializationException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@RunWith(SpringRunner.class)
@SpringBootTest
public class EquipoServiceTest {

    @Autowired
    EquipoService equipoService;

    @Autowired
    UsuarioService usuarioService;

    @Test
    public void obtenerListadoEquipos() {
        // GIVEN
        // En el application.properties se cargan los datos de prueba del fichero datos-test.sql

        // WHEN
        List<Equipo> equipos = equipoService.findAllOrderedByName();

        // THEN
        assertThat(equipos).hasSize(2);
        assertThat(equipos.get(0).getNombre()).isEqualTo("Proyecto P1");
        assertThat(equipos.get(1).getNombre()).isEqualTo("Proyecto P3");
    }

    @Test
    public void obtenerEquipo() {
        // GIVEN
        // En el application.properties se cargan los datos de prueba del fichero datos-test.sql

        // WHEN
        Equipo equipo = equipoService.findById(1L);

        // THEN
        assertThat(equipo.getNombre()).isEqualTo("Proyecto P1");
        // Comprobamos que la relación con Usuarios es lazy: al
        // intentar acceder a la colección de usuarios se debe lanzar una
        // excepción de tipo LazyInitializationException.
        assertThatThrownBy(() -> {
            equipo.getUsuarios().size();
        }).isInstanceOf(LazyInitializationException.class);
    }

    @Test
    public void comprobarRelacionUsuarioEquipos() {
        // GIVEN
        // En el application.properties se cargan los datos de prueba del fichero datos-test.sql

        // WHEN
        Usuario usuario = usuarioService.findById(1L);

        // THEN

        assertThat(usuario.getEquipos()).hasSize(1);
    }

    @Test
    public void obtenerUsuariosEquipo() {
        // GIVEN
        // En el application.properties se cargan los datos de prueba del fichero datos-test.sql

        // WHEN
        List<Usuario> usuarios = equipoService.usuariosEquipo(1L);

        // THEN
        assertThat(usuarios).hasSize(1);
        assertThat(usuarios.get(0).getEmail()).isEqualTo("ana.garcia@gmail.com");
        // Comprobamos que la relación entre usuarios y equipos es eager
        // Primero comprobamos que la colección de equipos tiene 1 elemento
        assertThat(usuarios.get(0).getEquipos()).hasSize(1);
        // Y después que el elemento es el equipo Proyecto P1
        assertThat(usuarios.get(0).getEquipos().stream().findFirst().get().getNombre()).isEqualTo("Proyecto P1");
    }

    @Test
    @Transactional
    public void testNuevoEquipo(){
        // GIVEN
        // En el application.properties se cargan los datos de prueba del fichero datos-test.sql

        // WHEN
        Usuario usuario = usuarioService.findById(1L);
        Equipo equipo = equipoService.nuevoEquipoConAdmin("EquipoTest", usuario);
        List<Equipo> equipos = equipoService.findAllOrderedByName();

        // THEN
        assertThat(equipos).hasSize(3);
        assertThat(equipos.get(0).getNombre()).isEqualTo("EquipoTest");
    }

    @Test
    @Transactional
    public void testAnyadirUsuarioANuevoEquipo(){
        // GIVEN
        // En el application.properties se cargan los datos de prueba del fichero datos-test.sql

        // WHEN
        Usuario usuario = usuarioService.findById(1L);
        Equipo equipo = equipoService.nuevoEquipoConAdmin("EquipoTest", usuario);


        Boolean resultadoPrimeraOperacion = equipoService.anyadirUsuarioAEquipo(usuario.getId(), equipo.getId());
        //2a inserción, que no debe realizarse
        Boolean resultadoSegundaOperacion = equipoService.anyadirUsuarioAEquipo(usuario.getId(), equipo.getId());

        // THEN
        assertThat(resultadoPrimeraOperacion).isTrue();
        assertThat(resultadoSegundaOperacion).isFalse();
        assertThat(equipo.getUsuarios().size()).isEqualTo(1);
        assertThat(equipo.getUsuarios().contains(usuario));
        assertThat(usuario.getEquipos().contains(equipo));
    }

    @Test
    @Transactional
    public void testEliminarUsuarioDeEquipo(){
        // GIVEN
        // En el application.properties se cargan los datos de prueba del fichero datos-test.sql

        Equipo equipo = equipoService.findById(1L);
        Usuario usuario = usuarioService.findById(1L);

        assertThat(equipo.getUsuarios().size()).isEqualTo(1);
        assertThat(equipo.getUsuarios().contains(usuario));
        assertThat(usuario.getEquipos().contains(equipo));

        // WHEN

        Boolean resultadoPrimeraOperacion = equipoService.eliminarUsuarioDeEquipo(usuario.getId(), equipo.getId());
        //2o borrado, que no debe realizarse
        Boolean resultadoSegundaOperacion = equipoService.eliminarUsuarioDeEquipo(usuario.getId(), equipo.getId());

        // THEN
        assertThat(resultadoPrimeraOperacion).isTrue();
        assertThat(resultadoSegundaOperacion).isFalse();
        assertThat(equipo.getUsuarios().size()).isEqualTo(0);
        assertThat(!equipo.getUsuarios().contains(usuario));
        assertThat(!usuario.getEquipos().contains(equipo));
    }

    @Test
    @Transactional
    public void testEliminarEquipo(){

        // GIVEN
        // En el application.properties se cargan los datos de prueba del fichero datos-test.sql

        Equipo equipo = equipoService.findById(1L);
        assertThat(equipoService.findAllOrderedByName().size()).isEqualTo(2);
        assertThat(equipoService.findAllOrderedByName().contains(equipo));

        // WHEN
        equipoService.eliminarEquipo(equipo.getId());


        // THEN
        assertThat(equipoService.findAllOrderedByName().size()).isEqualTo(1);
        assertThat(!equipoService.findAllOrderedByName().contains(equipo));
    }

    @Test
    @Transactional
    public void testCambiarNombreEquipo(){

        // GIVEN
        // En el application.properties se cargan los datos de prueba del fichero datos-test.sql
        Usuario usuario = usuarioService.findById(1L);
        Equipo equipo = equipoService.nuevoEquipoConAdmin("EquipoTest", usuario);
        Long idEquipoAModificar = 1L;
        equipo.setId(idEquipoAModificar);

        // WHEN
        Equipo equipoNuevoNombre = equipoService.editarNombreEquipo(idEquipoAModificar, "EquipoTestNuevoNombre");
        Equipo equipoBD = equipoService.findById(idEquipoAModificar);

        // THEN
        assertThat(equipoNuevoNombre.getNombre()).isEqualTo("EquipoTestNuevoNombre");
        assertThat(equipoBD.getNombre()).isEqualTo("EquipoTestNuevoNombre");
    }

}