package madstodolist.model;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "tareas")
public class Tarea implements Serializable {

    private static final long serialVersionUID = 1L;
    public enum EstadoTarea {PENDIENTE, ACTIVA, TERMINADA;}

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotNull
    private String titulo;

    private Integer prioridad;


    @NotNull
    @Enumerated(EnumType.ORDINAL)
    private EstadoTarea estado;

    @NotNull
    // Relación muchos-a-uno entre tareas y usuario
    @ManyToOne
    // Nombre de la columna en la BD que guarda físicamente
    // el ID del usuario con el que está asociado una tarea
    @JoinColumn(name = "usuario_id")
    private Usuario usuario;

    @ManyToMany(mappedBy = "tareas", fetch = FetchType.EAGER)
    Set<Categoria> categorias = new HashSet<>();

    @ManyToOne
    @JoinColumn(name = "proyecto_id")
    @OnDelete( action = OnDeleteAction.CASCADE )
    private Proyecto proyecto;

    @OneToMany(mappedBy = "tarea", fetch = FetchType.EAGER)
    @OnDelete( action = OnDeleteAction.CASCADE )
    Set<Comentario> comentarios = new HashSet<>();


    // Constructor vacío necesario para JPA/Hibernate.
    // Lo hacemos privado para que no se pueda usar desde el código de la aplicación. Para crear un
    // usuario en la aplicación habrá que llamar al constructor público. Hibernate sí que lo puede usar, a pesar
    // de ser privado.
    private Tarea() {}

    // Al crear una tarea la asociamos automáticamente a un
    // usuario. Actualizamos por tanto la lista de tareas del
    // usuario.
    public Tarea(Usuario usuario, String titulo) {
        this.usuario = usuario;
        this.titulo = titulo;
        this.estado = EstadoTarea.PENDIENTE;
        usuario.getTareas().add(this);
    }

    public Tarea(Usuario usuario, String titulo, Integer prioridad) {
        this.usuario = usuario;
        this.titulo = titulo;
        this.prioridad = prioridad;
        this.estado = EstadoTarea.PENDIENTE;
        usuario.getTareas().add(this);
    }

    public Tarea(Usuario usuario, Proyecto proyecto, String titulo, Integer prioridad) {
        this.usuario = usuario;
        this.titulo = titulo;
        this.prioridad = prioridad;
        this.proyecto = proyecto;
        this.estado = EstadoTarea.PENDIENTE;
        usuario.getTareas().add(this);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

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

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public Set<Categoria> getCategoria() {
        return categorias;
    }

    public void setCategoria(Set<Categoria> categoria) {
        this.categorias = categoria;
    }

    public void eliminarCategoria(Categoria categoria) { this.getCategoria().remove(categoria); }

    public void setEstado(EstadoTarea estado) { this.estado = estado; }

    public EstadoTarea getEstado() { return estado; }

    public Proyecto getProyecto(){ return proyecto; }

    public void setProyecto(Proyecto proyecto) { this.proyecto = proyecto; }

    public Set<Comentario> getComentarios() {return this.comentarios;}

    public void setComentarios (Set<Comentario> comentarios){this.comentarios = comentarios;}

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Tarea tarea = (Tarea) o;
        if (id != null && tarea.id != null)
            // Si tenemos los ID, comparamos por ID
            return Objects.equals(id, tarea.id);
        // sino comparamos por campos obligatorios
        return titulo.equals(tarea.titulo) &&
                usuario.equals(tarea.usuario);
    }

    @Override
    public int hashCode() {
        return Objects.hash(titulo, usuario);
    }
}
