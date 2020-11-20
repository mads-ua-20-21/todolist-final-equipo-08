package madstodolist.service;

import madstodolist.model.Equipo;
import madstodolist.model.EquipoRepository;
import madstodolist.model.Usuario;
import madstodolist.model.UsuarioRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;


@Service
public class EquipoService {

    Logger logger = LoggerFactory.getLogger(EquipoService.class);

    @Autowired
    private UsuarioRepository usuarioRepository;

    private EquipoRepository equipoRepository;


    @Autowired
    public EquipoService(EquipoRepository equipoRepository){ this.equipoRepository = equipoRepository;}

    @Transactional(readOnly = true)
    public Equipo findById(Long equipoId) {
        return equipoRepository.findById(equipoId).orElse(null);
    }

    @Transactional(readOnly = true)
    public List<Equipo> findAllOrderedByName(){

        List<Equipo> equipos = equipoRepository.findAll();
        equipos.sort(Comparator.comparing(Equipo::getNombre));
        return equipos;
    }

    @Transactional(readOnly = true)
    public List<Usuario> usuariosEquipo(Long idEquipo){

        Equipo equipo = equipoRepository.findById(idEquipo).orElse(null);
        if (equipo == null){
            throw new EquipoServiceException("Equipo " + idEquipo + " no existe al listar equipos ");
        }
        List<Usuario> usuarios = new ArrayList(equipo.getUsuarios());
        return usuarios;
    }

    @Transactional
    public Equipo nuevoEquipo(String tituloEquipo){
        Equipo equipo = new Equipo(tituloEquipo);
        equipoRepository.save(equipo);
        return equipo;
    }

    @Transactional(readOnly = true)
    public Boolean usuarioPerteneceAEquipo(Long idUsuario, Long idEquipo){

        Equipo equipo = equipoRepository.findById(idEquipo).orElse(null);
        if (equipo == null){
            throw new EquipoServiceException("Equipo " + idEquipo + " no existe ");
        }

        Usuario usuario = usuarioRepository.findById(idUsuario).orElse(null);
        if (usuario == null) {
            throw new UsuarioServiceException("Usuario " + idUsuario + " no existe ");
        }

        List<Usuario> usuarios = usuariosEquipo(idEquipo);
         return  usuarios.contains(usuario);
    }

    @Transactional
    public Boolean anyadirUsuarioAEquipo(Long idUsuario, Long idEquipo){

        Boolean anyadido = false;


        if (!usuarioPerteneceAEquipo(idUsuario, idEquipo)){
            Equipo equipo = equipoRepository.findById(idEquipo).orElse(null);

            Usuario usuario = usuarioRepository.findById(idUsuario).orElse(null);

            equipo.getUsuarios().add(usuario);
            usuario.getEquipos().add(equipo);
            anyadido = true;
        }
        return anyadido;
    }

    @Transactional
    public Boolean eliminarUsuarioDeEquipo(Long idUsuario, Long idEquipo){

        Boolean eliminado = false;

        if (usuarioPerteneceAEquipo(idUsuario, idEquipo)){
            Equipo equipo = equipoRepository.findById(idEquipo).orElse(null);

            Usuario usuario = usuarioRepository.findById(idUsuario).orElse(null);

            equipo.getUsuarios().remove(usuario);
            usuario.getEquipos().remove(equipo);
            eliminado = true;
        }

        return eliminado;
    }

    @Transactional
    public void eliminarEquipo(Long idEquipo){

        Equipo equipo = equipoRepository.findById(idEquipo).orElse(null);
        if (equipo == null){
            throw new EquipoServiceException("Equipo " + idEquipo + " no existe ");
        }

        equipo.getUsuarios().removeAll(usuariosEquipo(idEquipo));

        equipoRepository.delete(equipo);

    }

    @Transactional
    public Equipo editarNombreEquipo(Long idEquipo, String nuevoNombre){

        Equipo equipo = equipoRepository.findById(idEquipo).orElse(null);
        if (equipo == null){
            throw new EquipoServiceException("Equipo " + idEquipo + " no existe ");
        }
        equipo.setNombre(nuevoNombre);
        equipoRepository.save(equipo);

        return equipo;
    }
}