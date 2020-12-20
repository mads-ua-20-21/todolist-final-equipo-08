package madstodolist.service;

import madstodolist.model.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;


@Service
public class CategoriaService {

    Logger logger = LoggerFactory.getLogger(EquipoService.class);

    private UsuarioRepository usuarioRepository;

    private CategoriaRepository categoriaRepository;

    private TareaRepository tareaRepository;


    @Autowired
    public CategoriaService(CategoriaRepository categoriaRepository, TareaRepository tareaRepository,
                            UsuarioRepository usuarioRepository) {
        this.categoriaRepository = categoriaRepository;
        this.tareaRepository = tareaRepository;
        this.usuarioRepository = usuarioRepository;
    }

    @Transactional
    public Categoria nuevaCategoriaUsuario(Long idUsuario, String tituloCategoria) {
        Usuario usuario = usuarioRepository.findById(idUsuario).orElse(null);
        if (usuario == null) {
            throw new TareaServiceException("Usuario " + idUsuario + " no existe al crear categoria " + tituloCategoria);
        }
        Categoria categoria = new Categoria(tituloCategoria, usuario);
        categoriaRepository.save(categoria);
        return categoria;
    }

    @Transactional(readOnly = true)
    public List<Categoria> allCategoriasUsuario(Long idUsuario) {
        Usuario usuario = usuarioRepository.findById(idUsuario).orElse(null);
        if (usuario == null) {
            throw new TareaServiceException("Usuario " + idUsuario + " no existe al listar tareas ");
        }
        List<Categoria> categorias = new ArrayList(usuario.getCategorias());
        //Collections.sort(tareas, (a, b) -> a.getId() < b.getId() ? -1 : a.getId() == b.getId() ? 0 : 1);
        return categorias;
    }


    @Transactional(readOnly = true)
    public Categoria findById(Long categoriaId) {
        return categoriaRepository.findById(categoriaId).orElse(null);
    }

    @Transactional(readOnly = true)
    public List<Categoria> findAllOrderedByTitulo() {

        List<Categoria> categorias = categoriaRepository.findAll();
        categorias.sort(Comparator.comparing(Categoria::getTitulo));
        return categorias;
    }

    @Transactional(readOnly = true)
    public Tarea comprobarIdTarea(Long idTarea){
        Tarea tarea = tareaRepository.findById(idTarea).orElse(null);
        if (tarea == null){
            throw new TareaServiceException("Tarea " + idTarea + " no existe ");
        }
        return  tarea;
    }

    @Transactional(readOnly = true)
    public Categoria comprobarIdCategoria(Long idCategoria){
        Categoria categoria = categoriaRepository.findById(idCategoria).orElse(null);
        if (categoria == null){
            throw new CategoriaServiceException("Categoria " + idCategoria + " no existe ");
        }
        return  categoria;
    }

    @Transactional(readOnly = true)
    public Boolean tareaTieneCategoria(Long idTarea){

        Tarea tarea = comprobarIdTarea(idTarea);
        //if (tarea.getCategoria() != null) {
        //    return true;
        //}
        return false;
    }

    @Transactional
    public Boolean anyadirCategoriaATarea(Long idCategoria, Long idTarea){

        Boolean anyadido = false;


        if (!tareaTieneCategoria(idTarea)){
            Categoria categoria = categoriaRepository.findById(idCategoria).orElse(null);

            Tarea tarea = tareaRepository.findById(idTarea).orElse(null);

            //categoria.getTareas().add(tarea);
            //tarea.setCategoria(categoria);
            anyadido = true;
        }
        return anyadido;
    }

}

/*
* @Transactional(readOnly = true)
    public List<Usuario> usuariosEquipo(Long idEquipo) {

        Equipo equipo = comprobarIdEquipo(idEquipo);
        List<Usuario> usuarios = new ArrayList(equipo.getUsuarios());
        return usuarios;
    }

    @Transactional
    public Equipo nuevoEquipoConAdmin(String tituloEquipo, Usuario admin) {
        Equipo equipo = new Equipo(tituloEquipo, admin);
        equipoRepository.save(equipo);

        return equipo;
    }
* */