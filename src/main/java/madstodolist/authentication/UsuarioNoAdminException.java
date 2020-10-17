package madstodolist.authentication;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.UNAUTHORIZED, reason="Usuario no administrador")
public class UsuarioNoAdminException extends RuntimeException {
}
