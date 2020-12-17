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
@Table(name = "proyectos")
public class Proyecto implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    private String nombre;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "equipo_id")
    private Equipo equipo;

    @OneToMany(mappedBy = "proyecto", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @OnDelete(action = OnDeleteAction.CASCADE)
    Set<Tarea> tareas = new HashSet<>();

    private Proyecto(){}

    public Proyecto(String nombre, Equipo creador){
        this.nombre = nombre;
        this.equipo = creador;
    }

    public Long getId() { return id; }

    public void setId(Long id) { this.id = id; }

    public String getNombre() { return nombre; }

    public void setNombre(String nombre) { this.nombre = nombre; }

    public Equipo getEquipo() { return this.equipo; }

    public void setEquipo(Equipo equipo) { this.equipo = equipo; }

    public Set<Tarea> getTareas() { return this.tareas; }

    public void setTareas(Set<Tarea> tareas) { this.tareas = tareas; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Proyecto proyecto = (Proyecto) o;
        if (id != null && proyecto.id != null)
            // Si tenemos los ID, comparamos por ID
            return Objects.equals(id, proyecto.id);
        // sino comparamos por campos obligatorios
        return nombre.equals(proyecto.nombre);
    }

    @Override
    public int hashCode() {
        // Generamos un hash basado en los campos obligatorios
        return Objects.hash(nombre);
    }
}
