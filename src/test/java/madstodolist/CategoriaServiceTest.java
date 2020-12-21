package madstodolist;


import madstodolist.model.Categoria;
import madstodolist.model.Tarea;
import madstodolist.model.Usuario;
import madstodolist.service.CategoriaService;
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
public class CategoriaServiceTest {

    @Autowired
    CategoriaService categoriaService;

    @Autowired
    TareaService tareaService;

    @Autowired
    UsuarioService usuarioService;


    @Test
    @Transactional
    public void testNuevaCategoria() {
        // GIVEN
        // En el application.properties se cargan los datos de prueba del fichero datos-test.sql

        // WHEN
        Usuario usuario = usuarioService.findById(1L);

        Categoria categoria = categoriaService.nuevaCategoriaUsuario(usuario.getId(), "Casa");

        // THEN
        assertThat(categoria.getTitulo()).contains("Casa");
    }
/*
    @Test
    @Transactional
    public void testAnyadirCategoriaATarea() {
        // GIVEN
        // En el application.properties se cargan los datos de prueba del fichero datos-test.sql

        // WHEN
        Usuario usuario = usuarioService.findById(1L);

        Tarea tarea = tareaService.nuevaTareaUsuario(1L, "Práctica 1 de MADS");

        Categoria categoria = categoriaService.nuevaCategoriaUsuario(usuario.getId(), "Casa");
        categoriaService.anyadirCategoriaATarea(categoria.getId(), tarea.getId());

        // THEN
        assertThat(categoria.getTitulo()).contains("Casa");
        assertThat(tarea.getCategoria()).isNotNull();
    }

    @Test
    @Transactional
    public void testEliminarCategoriaATarea() {
        // GIVEN
        // En el application.properties se cargan los datos de prueba del fichero datos-test.sql

        // WHEN
        Usuario usuario = usuarioService.findById(1L);

        Tarea tarea = tareaService.nuevaTareaUsuario(1L, "Práctica 1 de MADS");

        Categoria categoria = categoriaService.nuevaCategoriaUsuario(usuario.getId(), "Casa");
        categoriaService.anyadirCategoriaATarea(categoria.getId(), tarea.getId());
        categoriaService.eliminarCategoriaATarea(categoria.getId(), tarea.getId());

        // THEN
        assertThat(categoria.getTitulo()).contains("Casa");
        //assertThat(tareaService.findById(tarea.getId()).getCategoria()).isNull();
    }
 */
}