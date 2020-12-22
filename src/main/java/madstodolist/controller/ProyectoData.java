package madstodolist.controller;

import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

public class ProyectoData {

    private String nombre;
    private String descripcion;

    @DateTimeFormat(pattern = "dd-MM-yyyy")
    private Date fechaLimite;

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Date getFechaLimite() {
        return fechaLimite;
    }

    public void setFechaLimite(Date fechaLimite) {
        this.fechaLimite = fechaLimite;
    }


}
