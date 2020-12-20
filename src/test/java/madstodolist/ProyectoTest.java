package madstodolist;

import madstodolist.model.*;
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
public class ProyectoTest {

    Logger logger = LoggerFactory.getLogger(UsuarioTest.class);

    @Autowired
    private ProyectoRepository proyectoRepository;

    @Autowired
    private EquipoRepository equipoRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private TareaRepository tareaRepository;

    @Test
    public void crearProyecto() {
        Equipo equipo = equipoRepository.findById(1L).orElse(null);
        Proyecto proyecto = new Proyecto("Proyecto prueba", equipo);
        assertThat(proyecto.getNombre()).isEqualTo("Proyecto prueba");
    }

    @Test
    public void igualdadProyectos(){
        Equipo equipo = equipoRepository.findById(1L).orElse(null);

        Proyecto proyecto1 = new Proyecto("Proyecto prueba 1", equipo);
        Proyecto proyecto2 = new Proyecto("Proyecto prueba 1", equipo);
        Proyecto proyecto3 = new Proyecto("Proyecto prueba 2", equipo);

        //Igualdad por nombre
        assertThat(proyecto1).isEqualTo(proyecto2);
        assertThat(proyecto1).isNotEqualTo(proyecto3);

        //Asignamos IPs
        proyecto1.setId(1L);
        proyecto2.setId(2L);
        proyecto3.setId(2L);

        //Igualdad por ID
        assertThat(proyecto1).isNotEqualTo(proyecto2);
        assertThat(proyecto2).isEqualTo(proyecto3);
    }

    @Test
    @Transactional
    public void guardarProyectoBD(){
        //GIVEN
        Equipo equipo = equipoRepository.findById(1L).orElse(null);
        Proyecto proyecto = new Proyecto("Proyecto prueba", equipo);

        // WHEN
        proyecto = proyectoRepository.save(proyecto);

        // THEN
        assertThat(proyecto.getId()).isNotNull();
    }

    @Test
    public void recuperacionProyectoBD(){
        // GIVEN
        // En el application.properties se cargan los datos de prueba del fichero datos-test.sql

        //WHEN
        Proyecto proyecto = proyectoRepository.findById(1L).orElse(null);

        //THEN
        assertThat(proyecto).isNotNull();
        assertThat(proyecto.getId()).isEqualTo(1L);
        assertThat(proyecto.getNombre()).isEqualTo("Proyecto MADS");
    }

    @Test
    public void comprobarRelacionConEquipo(){
        // GIVEN
        // En el application.properties se cargan los datos de prueba del fichero datos-test.sql
        Usuario usuario = usuarioRepository.findById(1L).orElse(null);
        Equipo equipo = new Equipo("Proyecto P3",usuario);

        //WHEN
        Proyecto proyecto = proyectoRepository.findById(1L).orElse(null);

        //THEN
        assertThat(proyecto).isNotNull();
        assertThat(proyecto.getEquipo()).isEqualTo(equipo);
    }

    @Test
    public void comprobarRelacionConTarea(){
        // GIVEN
        // En el application.properties se cargan los datos de prueba del fichero datos-test.sql
        Usuario usuario = usuarioRepository.findById(1L).orElse(null);
        Tarea tarea = new Tarea(usuario,"Crear mockups");

        //WHEN
        Proyecto proyecto = proyectoRepository.findById(1L).orElse(null);

        //THEN
        assertThat(proyecto).isNotNull();
        assertThat(proyecto.getTareas().size()).isEqualTo(2);
        assertThat(proyecto.getTareas().contains(tarea)).isTrue();
    }


}
