package madstodolist.model;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "categorias")
public class Categoria implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    private String titulo;

 /*
    //@OneToMany(mappedBy = "tarea", fetch = FetchType.EAGER)
    //Set<Tarea> tareas = new HashSet<>();
    @NotNull
    @ManyToOne
    @JoinColumn(name = "tarea_id")
    private Tarea tarea;


    @OneToMany(mappedBy = "categoria", fetch = FetchType.EAGER)
    Set<Tarea> tarea = new HashSet<>();

  */

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "categoria_tarea",
            joinColumns = { @JoinColumn(name = "fk_categoria") },
            inverseJoinColumns = {@JoinColumn(name = "fk_tarea")})
    Set<Tarea> tareas = new HashSet<>();

    @NotNull
    @ManyToOne
    @JoinColumn(name = "usuario_id")
    private Usuario usuario;

    // Constructor vacío necesario para JPA/Hibernate.
    // Lo hacemos privado para que no se pueda usar desde el código de la aplicación. Para crear un
    // usuario en la aplicación habrá que llamar al constructor público. Hibernate sí que lo puede usar, a pesar
    // de ser privado.
    private Categoria() {}

    // Al crear una tarea la asociamos automáticamente a un
    // usuario. Actualizamos por tanto la lista de tareas del
    // usuario.
    public Categoria(String titulo, Usuario usuario) {
        this.usuario = usuario;
        this.titulo = titulo;
        usuario.getCategorias().add(this);
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

    public Usuario getUsuario() { return usuario; }

    public void setUsuario(Usuario usuario) { this.usuario = usuario; }

    public Set<Tarea> getTareas() {
        return tareas;
    }
/*
    public void setTareas(Set<Tarea> tareas) {
        this.tarea = tareas;
    }
*/

    //public Tarea getTarea() { return tarea; }

    //public void setTarea(Tarea tarea) { this.tarea = tarea; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Categoria categoria = (Categoria) o;
        if (id != null && categoria.id != null)
            // Si tenemos los ID, comparamos por ID
            return Objects.equals(id, categoria.id);
        // sino comparamos por campos obligatorios
        return titulo.equals(categoria.titulo) &&
                usuario.equals(categoria.usuario);
    }

    @Override
    public int hashCode() {
        return Objects.hash(titulo);
    }
}
