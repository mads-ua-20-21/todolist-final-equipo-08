package madstodolist;


import madstodolist.model.Tarea;
import madstodolist.model.Usuario;
import madstodolist.service.TareaService;
import madstodolist.service.UsuarioService;
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
public class TareaServiceTest {

    @Autowired
    UsuarioService usuarioService;

    @Autowired
    TareaService tareaService;


    @Test
    @Transactional
    public void testNuevaTareaUsuario() {
        // GIVEN
        // En el application.properties se cargan los datos de prueba del fichero datos-test.sql

        // WHEN
        Tarea tarea = tareaService.nuevaTareaUsuario(1L, "Práctica 1 de MADS");

        // THEN

        Usuario usuario = usuarioService.findByEmail("ana.garcia@gmail.com");
        assertThat(usuario.getTareas()).contains(tarea);
    }

    @Test
    @Transactional
    public void testNuevaTareaUsuarioConPrioridad() {
        // GIVEN
        // En el application.properties se cargan los datos de prueba del fichero datos-test.sql

        // WHEN
        Tarea tarea = tareaService.nuevaTareaUsuario(1L, "Práctica 1 de MADS", 1);

        // THEN

        Usuario usuario = usuarioService.findByEmail("ana.garcia@gmail.com");
        assertThat(usuario.getTareas()).contains(tarea);
    }

    @Test
    public void testListadoTareas() {
        // GIVEN
        // En el application.properties se cargan los datos de prueba del fichero datos-test.sql

        Usuario usuario = new Usuario("ana.garcia@gmail.com");
        usuario.setId(1L);

        Tarea lavarCoche = new Tarea(usuario, "Lavar coche");
        lavarCoche.setId(1L);

        // WHEN

        List<Tarea> tareas = tareaService.allTareasUsuario(1L);

        // THEN

        assertThat(tareas.size()).isEqualTo(2);
        assertThat(tareas).contains(lavarCoche);
    }

    @Test
    public void testFiltrarTareasPorPalabra(){
        // GIVEN
        // En el application.properties se cargan los datos de prueba del fichero datos-test.sql

        Usuario usuario = new Usuario("ana.garcia@gmail.com");
        usuario.setId(1L);

        Tarea lavarCoche = new Tarea(usuario, "Lavar coche");
        lavarCoche.setId(1L);

        Tarea renovarDNI = new Tarea(usuario, "Renovar DNI");
        lavarCoche.setId(2L);


        // WHEN

        List<Tarea> tareas = tareaService.allTareasUsuario(1L);
        List<Tarea> tareasSinFiltro = tareaService.filtrarTareasPorPalabra(1L, "");
        List<Tarea> tareasConDNI = tareaService.filtrarTareasPorPalabra(1L, "dni");

        // THEN

        assertThat(tareas.size()).isEqualTo(2);
        assertThat(tareas).contains(lavarCoche);
        assertThat(tareasSinFiltro.size()).isEqualTo(2);
        assertThat(tareasSinFiltro).contains(lavarCoche);
        assertThat(tareasSinFiltro).contains(renovarDNI);
        assertThat(tareasConDNI.size()).isEqualTo(1);
        assertThat(tareasConDNI).contains(renovarDNI);

    }

    @Test
    public void testFiltrarYExcluirTareaPorEstado(){
        // GIVEN
        // En el application.properties se cargan los datos de prueba del fichero datos-test.sql

        Usuario usuario = new Usuario("ana.garcia@gmail.com");
        usuario.setId(1L);

        Tarea lavarCoche = new Tarea(usuario, "Lavar coche");
        lavarCoche.setId(1L);

        Tarea renovarDNI = new Tarea(usuario, "Renovar DNI");
        lavarCoche.setId(2L);


        // WHEN

        List<Tarea> tareas = tareaService.allTareasUsuario(1L);
        List<Tarea> tareasEnProceso = tareaService.filtrarTareasPorEstado(1L, 1);
        List<Tarea> tareasNoEnProceso = tareaService.excluirTareasPorEstado(1L, 1);

        // THEN

        assertThat(tareas.size()).isEqualTo(2);
        assertThat(tareas).contains(lavarCoche);
        assertThat(tareasEnProceso.size()).isEqualTo(1);
        assertThat(tareasEnProceso).contains(renovarDNI);
        assertThat(tareasNoEnProceso.size()).isEqualTo(1);
        assertThat(tareasNoEnProceso).doesNotContain(renovarDNI);

    }

    @Test
    public void testOrdenarTareaPorEstado(){
        // GIVEN
        // En el application.properties se cargan los datos de prueba del fichero datos-test.sql

        Usuario usuario = new Usuario("ana.garcia@gmail.com");
        usuario.setId(1L);

        Tarea lavarCoche = new Tarea(usuario, "Lavar coche");
        lavarCoche.setId(1L);

        Tarea renovarDNI = new Tarea(usuario, "Renovar DNI");
        lavarCoche.setId(2L);


        // WHEN

        List<Tarea> tareas = tareaService.allTareasUsuario(1L);
        List<Tarea> tareasPrimeroEnProceso = tareaService.orndenarTareasPrimerEstado(1L, 1);

        // THEN

        assertThat(tareas.size()).isEqualTo(2);
        assertThat(tareas).contains(lavarCoche);
        assertThat(tareas.get(0)).isNotEqualTo(renovarDNI);
        assertThat(tareasPrimeroEnProceso.size()).isEqualTo(2);
        assertThat(tareasPrimeroEnProceso).contains(lavarCoche);
        assertThat(tareasPrimeroEnProceso.get(0)).isEqualTo(renovarDNI);

    }

    @Test
    public void testBuscarTarea() {
        // GIVEN
        // En el application.properties se cargan los datos de prueba del fichero datos-test.sql

        // WHEN

        Tarea lavarCoche = tareaService.findById(1L);

        // THEN

        assertThat(lavarCoche).isNotNull();
        assertThat(lavarCoche.getTitulo()).isEqualTo("Lavar coche");
    }

    @Test
    @Transactional
    public void testModificarTarea() {
        // GIVEN
        // En el application.properties se cargan los datos de prueba del fichero datos-test.sql

        Tarea tarea = tareaService.nuevaTareaUsuario(1L, "Pagar el recibo");
        Long idNuevaTarea = tarea.getId();

        // WHEN

        Tarea tareaModificada = tareaService.modificaTarea(idNuevaTarea, "Pagar la matrícula");
        Tarea tareaBD = tareaService.findById(idNuevaTarea);

        // THEN

        assertThat(tareaModificada.getTitulo()).isEqualTo("Pagar la matrícula");
        assertThat(tareaBD.getTitulo()).isEqualTo("Pagar la matrícula");
    }

    @Test
    @Transactional
    public void testBorrarTarea() {
        // GIVEN

        Tarea tarea = tareaService.nuevaTareaUsuario(1L, "Estudiar MADS");

        // WHEN

        tareaService.borraTarea(tarea.getId());

        // THEN

        assertThat(tareaService.findById(tarea.getId())).isNull();
    }

    @Test
    @Transactional
    public void testAsignarEditarPrioridadEnTarea() {
        // GIVEN

        Tarea tarea = tareaService.nuevaTareaUsuario(1L, "Estudiar MADS");

        // WHEN

        tareaService.asignarEditarPrioridad(tarea.getId(), 3);

        // THEN

        assertThat(tareaService.findById(tarea.getId()).getPrioridad()).isEqualTo(3);
    }

    @Test
    @Transactional
    public void testActualizarEstado(){
        // GIVEN
        // En el application.properties se cargan los datos de prueba del fichero datos-test.sql

        Tarea tarea = tareaService.nuevaTareaUsuario(1L, "Estudiar MADS");
        Long idNuevaTarea = tarea.getId();

        // WHEN

        Tarea tareaModificada = tareaService.actualizarEstado(idNuevaTarea, 1);
        Tarea tareaBD = tareaService.findById(idNuevaTarea);

        // THEN

        assertThat(tareaModificada.getTitulo()).isEqualTo("Estudiar MADS");
        assertThat(tareaModificada.getEstado()).isEqualTo(Tarea.EstadoTarea.ACTIVA);
        assertThat(tareaBD.getTitulo()).isEqualTo("Estudiar MADS");
        assertThat(tareaBD.getEstado()).isEqualTo(Tarea.EstadoTarea.ACTIVA);
    }
}

