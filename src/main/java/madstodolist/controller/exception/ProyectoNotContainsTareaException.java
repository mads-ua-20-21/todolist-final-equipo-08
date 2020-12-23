package madstodolist.controller.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST, reason="El proyecto no contiene esta tarea")
public class ProyectoNotContainsTareaException extends RuntimeException{
}
