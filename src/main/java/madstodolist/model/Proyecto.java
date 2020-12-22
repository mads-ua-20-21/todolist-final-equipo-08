package madstodolist.model;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
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
    @OnDelete( action = OnDeleteAction.CASCADE )
    private Equipo equipo;

    @OneToMany(mappedBy = "proyecto", fetch = FetchType.EAGER)
    @OnDelete( action = OnDeleteAction.CASCADE )
    Set<Tarea> tareas = new HashSet<>();

    @Temporal(TemporalType.DATE)
    private Date fechalimite;

    private String descripcion;

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

    public Date getFechaLimite() { return fechalimite; }

    public String getStringFechaLimite(){
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
        return formatter.format(fechalimite.getTime());
    }

    public void setFechaLimite(Date fechaLimite) { this.fechalimite = fechaLimite; }

    public String getDescripcion() { return descripcion; }

    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }

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
