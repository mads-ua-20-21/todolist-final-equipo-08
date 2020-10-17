package madstodolist.authentication;

import org.springframework.stereotype.Component;

import javax.servlet.http.HttpSession;

@Component
public class ManagerUserSesion {

    // Añadimos el id de usuario en la sesión HTTP para hacer
    // una autorización sencilla. En los métodos de controllers
    // comprobamos si el id del usuario logeado coincide con el obtenido
    // desde la URL
    public void logearUsuario(HttpSession session, Long idUsuario) {
        session.setAttribute("idUsuarioLogeado", idUsuario);
    }

    public void comprobarUsuarioLogeado(HttpSession session, Long idUsuario) {
        Long idUsuarioLogeado = (Long) session.getAttribute("idUsuarioLogeado");
        if (!idUsuario.equals(idUsuarioLogeado))
            throw new UsuarioNoLogeadoException();
    }

    public void usuarioLogueado(HttpSession session){
        if(session.getAttribute("idUsuarioLogeado") == null){
            throw new UsuarioNoLogeadoException();
        }
    }

    public void logearAdmin(HttpSession session, Boolean administrador){
        session.setAttribute("administrador", administrador);
    }

    public void comprobarUsuarioAdministrador(HttpSession session, Boolean administrador){
        if (!(Boolean) session.getAttribute("administrador")){
            throw new UsuarioNoAdminException();
        }
    }
}
