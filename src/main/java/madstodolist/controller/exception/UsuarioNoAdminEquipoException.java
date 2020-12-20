package madstodolist.controller.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.UNAUTHORIZED, reason="El usuario no es administrador del equipo")
public class UsuarioNoAdminEquipoException extends RuntimeException{
}
