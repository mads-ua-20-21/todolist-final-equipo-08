package madstodolist.controller;

public class TareaData {
    private String titulo;
    private Integer prioridad;
    private Integer estado;

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public Integer getPrioridad() {
        return prioridad;
    }

    public void setPrioridad(Integer prioridad) {
        this.prioridad = prioridad;
    }

    public Integer getEstado() { return estado; }

    public void setEstado(Integer estado) { this.estado = estado; }
}
