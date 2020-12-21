package madstodolist.controller;

public class TareaData {
    private String titulo;
    private Integer prioridad;
    private Long categoria;
    private Boolean borrarCategorias;

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

    public Long getCategoria() {
        return categoria;
    }

    public void setCategoria(Long categoria) {
        this.categoria = categoria;
    }

    public Boolean getBorrarCategorias() { return borrarCategorias; }

    public void setBorrarCategorias(Boolean borrar) { borrarCategorias = borrar; }
}
