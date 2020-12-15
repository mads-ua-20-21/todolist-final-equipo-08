package madstodolist.model;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Objects;

@Entity
@Table(name = "proyectos")
public class Proyecto implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    private String nombre;

    @ManyToOne
    @JoinColumn(name = "equipo_id")
    private Equipo equipo;

    private Proyecto(){}

    public Proyecto(String nombre){
        this.nombre = nombre;
    }

    public Long getId() { return id; }

    public void setId(Long id) { this.id = id; }

    public String getNombre() { return nombre; }

    public void setNombre(String nombre) { this.nombre = nombre; }

    public Equipo getEquipo() { return this.equipo; }

    public void setEquipo(Equipo equipo) { this.equipo = equipo; }

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
