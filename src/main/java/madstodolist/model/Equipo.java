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
@Table(name = "equipos")
public class Equipo implements Serializable{

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotNull
    private String nombre;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "equipo_usuario",
            joinColumns = { @JoinColumn(name = "fk_equipo") },
            inverseJoinColumns = {@JoinColumn(name = "fk_usuario")})
    Set<Usuario> usuarios = new HashSet<>();

    @NotNull
    @ManyToOne
    // Nombre de la columna en la BD que guarda físicamente
    // el ID del usuario asociado como administrador del equipo
    @JoinColumn(name = "administrador_id")
    private Usuario usuarioAdministrador;

    @OneToMany(mappedBy = "equipo", fetch = FetchType.EAGER,cascade = CascadeType.ALL)
    @OnDelete(action = OnDeleteAction.CASCADE)
    Set<Proyecto> proyectos = new HashSet<>();

    private Equipo(){}

    public Equipo(String nombre, Usuario administrador){
        this.nombre = nombre;
        this.usuarioAdministrador = administrador;
    }

    public Long getId() { return id; }

    public void setId(Long id) { this.id = id; }

    public String getNombre() { return nombre; }

    public void setNombre(String nombre) { this.nombre = nombre; }

    public void setUsuarioAdministrador(Usuario usuarioAdministrador) { this.usuarioAdministrador = usuarioAdministrador; }

    public Usuario getUsuarioAdministrador() { return usuarioAdministrador; }

    public Set<Usuario> getUsuarios() { return usuarios; }

    public Set<Proyecto> getProyectos() { return proyectos; }

    public void setProyectos(Set<Proyecto> proyectos) { this.proyectos = proyectos; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Equipo equipo = (Equipo) o;
        if (id != null && equipo.id != null)
            // Si tenemos los ID, comparamos por ID
            return Objects.equals(id, equipo.id);
        // sino comparamos por campos obligatorios
        return nombre.equals(equipo.nombre);
    }

    @Override
    public int hashCode() {
        // Generamos un hash basado en los campos obligatorios
        return Objects.hash(nombre);
    }
}