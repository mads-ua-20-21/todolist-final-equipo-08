package madstodolist;

import madstodolist.model.Equipo;
import madstodolist.model.Proyecto;
import madstodolist.model.Tarea;
import madstodolist.service.EquipoService;
import madstodolist.service.ProyectoService;
import madstodolist.service.TareaService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ProyectoServiceTest {

    @Autowired
    ProyectoService proyectoService;

    @Autowired
    EquipoService equipoService;

    @Autowired
    TareaService tareaService;

    @Test
    public void comprobarProyectoId(){
        // GIVEN
        // En el application.properties se cargan los datos de prueba del fichero datos-test.sql

        //WHEN
        Proyecto proyecto = proyectoService.comprobarIdProyecto(1L);

        //THEN
        assertThat(proyecto).isNotNull();
        assertThat(proyecto.getNombre()).isEqualTo("Proyecto MADS");
    }

    @Test
    public void comprobarFechaProyecto() throws ParseException {
        // GIVEN
        // En el application.properties se cargan los datos de prueba del fichero datos-test.sql

        //WHEN
        Proyecto proyecto = proyectoService.comprobarIdProyecto(1L);
        Date fecha =new SimpleDateFormat("yyyy-MM-dd HH:mm").parse("2020-12-24 00:00");

        //THEN
        assertThat(proyecto).isNotNull();
        assertThat(proyecto.getNombre()).isEqualTo("Proyecto MADS");
        assertThat(proyecto.getFechaLimite()).isEqualTo(fecha);
        assertThat(proyecto.getStringFechaLimite()).isEqualTo("24-12-2020");
    }

    @Test
    public void obtenerTareasProyecto(){
        // GIVEN
        // En el application.properties se cargan los datos de prueba del fichero datos-test.sql

        //WHEN
        Proyecto proyecto = proyectoService.findById(1L);
        List<Tarea> tareas = proyectoService.tareasProyecto(1L);

        //THEN
        assertThat(proyecto).isNotNull();
        assertThat(tareas.size()).isEqualTo(2);
        assertThat(tareas.get(0).getTitulo()).isEqualTo("Crear mockups");
        assertThat(tareas.get(1).getTitulo()).isEqualTo("Implementar Tests");
    }

    @Test
    public void comprobarRelacionEquipoProyecto(){
        // GIVEN
        // En el application.properties se cargan los datos de prueba del fichero datos-test.sql

        //WHEN
        Equipo equipo = equipoService.findById(2L);

        //THEN
        assertThat(equipo).isNotNull();
        assertThat(equipo.getProyectos().size()).isEqualTo(2);
    }

    @Test
    @Transactional
    public void crearNuevoProyecto(){
        // GIVEN
        // En el application.properties se cargan los datos de prueba del fichero datos-test.sql

        //WHEN
        Proyecto proyecto = proyectoService.nuevoProyecto(1L,"Proyecto test");

        //THEN
        Equipo equipo = equipoService.findById(1L);
        assertThat(proyecto).isNotNull();
        assertThat(proyecto.getEquipo()).isEqualTo(equipo);
        assertThat(equipo.getProyectos().contains(proyecto));
    }

    @Test
    @Transactional
    public void modificarProyectoBD(){
        // GIVEN
        Proyecto proyecto = proyectoService.nuevoProyecto(1L,"Proyecto test");

        //WHEN
        Long idProyecto = proyecto.getId();
        proyectoService.modificarProyecto(idProyecto,"Proyecto test modificado");

        //THEN
        proyecto = proyectoService.findById(idProyecto);
        assertThat(proyecto).isNotNull();
        assertThat(proyecto.getNombre()).isEqualTo("Proyecto test modificado");
    }

    @Test
    @Transactional
    public void modificarDescripcionProyectoBD(){
        // GIVEN
        Proyecto proyecto = proyectoService.nuevoProyecto(1L,"Proyecto test");
        String descripcion = "Proyecto que tiene como objetivo probar la funcion en la BBDD";

        //WHEN
        Long idProyecto = proyecto.getId();
        proyectoService.actualizarDescripcionProyecto(idProyecto, descripcion);

        //THEN
        proyecto = proyectoService.findById(idProyecto);
        assertThat(proyecto).isNotNull();
        assertThat(proyecto.getDescripcion()).isEqualTo(descripcion);
    }

    @Test
    @Transactional
    public void modificarDFechaProyectoBD(){
        // GIVEN
        Proyecto proyecto = proyectoService.nuevoProyecto(1L,"Proyecto test");
        Date fecha = new Date(2020, 12, 21);

        //WHEN
        Long idProyecto = proyecto.getId();
        proyectoService.actualizarFechaProyecto(idProyecto, fecha);

        //THEN
        proyecto = proyectoService.findById(idProyecto);
        assertThat(proyecto).isNotNull();
        assertThat(proyecto.getFechaLimite()).isEqualTo(fecha);
    }

    @Test
    @Transactional
    public void borrarProyectoBD(){
        //GIVEN
        Proyecto proyecto = proyectoService.nuevoProyecto(1L,"Proyecto test a borrar");

        //WHEN
        Long idProyecto = proyecto.getId();
        proyectoService.borrarProyecto(idProyecto);

        //THEN
        assertThat(proyectoService.findById(idProyecto)).isNull();
    }
}
