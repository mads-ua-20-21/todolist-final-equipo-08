package madstodolist;

import madstodolist.model.Proyecto;
import madstodolist.model.Tarea;
import madstodolist.model.Usuario;
import madstodolist.service.UsuarioService;
import madstodolist.service.UsuarioServiceException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest
public class UsuarioServiceTest {

    Logger logger = LoggerFactory.getLogger(UsuarioServiceTest.class);

    @Autowired
    private UsuarioService usuarioService;

    @Test
    public void servicioLoginUsuario() {
        // GIVEN
        // Datos cargados de datos-test.sql

        // WHEN

        UsuarioService.LoginStatus loginStatusOK = usuarioService.login("ana.garcia@gmail.com", "12345678");
        UsuarioService.LoginStatus loginStatusErrorPassword = usuarioService.login("ana.garcia@gmail.com", "000");
        UsuarioService.LoginStatus loginStatusNoUsuario = usuarioService.login("pepito.perez@gmail.com", "12345678");

        // THEN

        assertThat(loginStatusOK).isEqualTo(UsuarioService.LoginStatus.LOGIN_OK);
        assertThat(loginStatusErrorPassword).isEqualTo(UsuarioService.LoginStatus.ERROR_PASSWORD);
        assertThat(loginStatusNoUsuario).isEqualTo(UsuarioService.LoginStatus.USER_NOT_FOUND);
    }

    @Test
    @Transactional
    public void loginUsuarioBloqueado(){
        // GIVEN

        Usuario usuario = new Usuario("usuario.prueba2@gmail.com");
        usuario.setPassword("12345678");
        usuario.setBloqueado(true);

        // WHEN

        usuarioService.registrar(usuario);

        UsuarioService.LoginStatus loginStatusUsuarioBloqueado = usuarioService.login("usuario.prueba2@gmail.com", "12345678");

        // THEN

        assertThat(loginStatusUsuarioBloqueado).isEqualTo(UsuarioService.LoginStatus.USUARIO_BLOQUEADO);
    }

    @Test
    @Transactional
    public void servicioRegistroUsuario() {
        // GIVEN

        Usuario usuario = new Usuario("usuario.prueba2@gmail.com");
        usuario.setPassword("12345678");

        // WHEN

        usuarioService.registrar(usuario);

        // THEN

        Usuario usuarioBaseDatos = usuarioService.findByEmail("usuario.prueba2@gmail.com");
        assertThat(usuarioBaseDatos).isNotNull();
        assertThat(usuarioBaseDatos.getPassword()).isEqualTo(usuario.getPassword());
    }

    @Test
    @Transactional
    public void servicioRegistroUsuarioAdministrador() {
        // GIVEN

        Usuario usuario = new Usuario("usuario.prueba2@gmail.com");
        usuario.setPassword("12345678");
        usuario.setAdministrador(true);

        // WHEN

        usuarioService.registrar(usuario);

        // THEN

        Usuario usuarioBaseDatos = usuarioService.findByEmail("usuario.prueba2@gmail.com");
        assertThat(usuarioBaseDatos).isNotNull();
        assertThat(usuarioBaseDatos.getPassword()).isEqualTo(usuario.getPassword());

        Boolean existeAdministrador = usuarioService.existeAdmin();
        assertThat(existeAdministrador).isEqualTo(usuario.getAdministrador());
    }

    @Test(expected = UsuarioServiceException.class)
    public void servicioRegistroUsuarioExcepcionConNullPassword() {
        // Pasamos como argumento un usario sin contraseña
        Usuario usuario =  new Usuario("usuario.prueba@gmail.com");
        usuarioService.registrar(usuario);
    }


    @Test(expected = UsuarioServiceException.class)
    public void servicioRegistroUsuarioExcepcionConEmailRepetido() {
        // GIVEN
        // Datos cargados de datos-test.sql

        // WHEN
        // Pasamos como argumento un usario con emaii existente en datos-test.sql
        Usuario usuario =  new Usuario("ana.garcia@gmail.com");
        usuario.setPassword("12345678");
        usuarioService.registrar(usuario);

        // THEN
        // Se produce una excepción comprobada con el expected del test
    }

    @Test
    @Transactional
    public void servicioRegistroUsuarioDevuelveUsuarioConId() {
        // GIVEN

        Usuario usuario = new Usuario("usuario.prueba@gmail.com");
        usuario.setPassword("12345678");

        // WHEN

        usuario = usuarioService.registrar(usuario);

        // THEN

        assertThat(usuario.getId()).isNotNull();
    }

    @Test
    public void servicioConsultaUsuarioDevuelveUsuario() {
        // GIVEN
        // Datos cargados de datos-test.sql

        // WHEN

        Usuario usuario = usuarioService.findByEmail("ana.garcia@gmail.com");

        // THEN

        assertThat(usuario.getId()).isEqualTo(1L);
    }

    @Test
    @Transactional
    public void testBloquearUsuario() {
        // GIVEN
        // En el application.properties se cargan los datos de prueba del fichero datos-test.sql

        Long idUsuarioABloquear = 1L;


        // WHEN


        Usuario usuarioModificado = usuarioService.modificaEstadoUsuario(idUsuarioABloquear, true);
        Usuario usuarioBD = usuarioService.findById(idUsuarioABloquear);


        // THEN

        assertThat(usuarioModificado.getBloqueado()).isEqualTo(true);
        assertThat(usuarioBD.getBloqueado()).isEqualTo(true);

    }

    @Test
    public void obtenerProyectosUsuario (){
        // GIVEN
        // En el application.properties se cargan los datos de prueba del fichero datos-test.sql

        // WHEN
        Usuario usuario = usuarioService.findById(2L);

        //THEN
        assertThat(usuario).isNotNull();
        assertThat(usuarioService.equiposUsuario(2L).get(0).getNombre()).isEqualTo("Proyecto P4");
        assertThat(usuarioService.proyectosUsuario(2L).size()).isEqualTo(2);
        assertThat(usuarioService.proyectosUsuario(2L).get(0).getNombre()).isEqualTo("Proyecto IW");
        assertThat(usuarioService.proyectosUsuario(2L).get(1).getNombre()).isEqualTo("Proyecto DCA");

    }
}