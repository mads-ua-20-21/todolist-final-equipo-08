package madstodolist.service;

import madstodolist.model.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.List;

@Service
public class ComentarioService {
    Logger logger = LoggerFactory.getLogger(EquipoService.class);

    private UsuarioRepository usuarioRepository;

    private TareaRepository tareaRepository;

    private ComentarioRepository comentarioRepository;

    @Autowired
    public ComentarioService(ComentarioRepository comentarioRepository, TareaRepository tareaRepository, UsuarioRepository usuarioRepository){
        this.comentarioRepository = comentarioRepository;
        this.tareaRepository = tareaRepository;
        this.usuarioRepository = usuarioRepository;
    }

    @Transactional(readOnly = true)
    public Comentario findById(Long comentarioId) {
        return comentarioRepository.findById(comentarioId).orElse(null);
    }


    @Transactional(readOnly = true)
    public List<Comentario> findAllOrderedByName(){

        List<Comentario> comentarios = comentarioRepository.findAll();
        comentarios.sort(Comparator.comparing(Comentario::getFechaHoraString));
        return comentarios;
    }

    @Transactional
    public Comentario nuevoComentario(String mensaje, Usuario usuario, Tarea tarea){
        Comentario comentario = new Comentario(tarea, usuario, mensaje);
        comentarioRepository.save(comentario);

        return comentario;
    }

    @Transactional (readOnly = true)
    public Comentario existeComentario(Long idComentario) {
        Comentario comentario = comentarioRepository.findById(idComentario).orElse(null);
        if (comentario == null) {
            throw new UsuarioServiceException("Comentario " + idComentario + " no existe ");
        }
        return comentario;
    }

    @Transactional
    public void eliminarComentario(Long idComentario){
        Comentario comentario = existeComentario(idComentario);
        comentarioRepository.delete(comentario);
    }
}
