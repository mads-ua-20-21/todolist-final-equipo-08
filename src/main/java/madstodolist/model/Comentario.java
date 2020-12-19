package madstodolist.model;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

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
public class Comentario implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    private String mensaje;

    @NotNull
    private Date fechaHora;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "tarea_id")
    private Tarea tarea;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "usuario")
    private Usuario usuario;


    private Comentario() {
    }

    public Comentario(Tarea tarea, Usuario usuario, String mensaje) {
        this.tarea = tarea;
        this.usuario = usuario;
        this.fechaHora = new Date(System.currentTimeMillis());
    }

    public Long getId() { return id; }

    public void setId(Long id) { this.id = id; }

    public String getMensaje() { return mensaje; }

    public void setMensaje(String mensaje) { this.mensaje = mensaje; }

    public String getFechaHoraString() {
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        return formatter.format(fechaHora.getTime());
    }

    public Date getFechaHora() { return fechaHora; }

    public Tarea getTareas() { return tarea; }

    public void setTareas(Tarea tarea) { this.tarea = tarea; }

    public Usuario getUsuario() { return usuario; }

    public void setUsuario(Usuario usuario) { this.usuario = usuario; }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Comentario comentario = (Comentario) o;
        if (id != null && comentario.id != null)
            // Si tenemos los ID, comparamos por ID
            return Objects.equals(id, comentario.id);
        // sino comparamos por campos obligatorios
        return comentario.equals(comentario.mensaje) &&
                comentario.equals(comentario.fechaHora);
    }

    @Override
    public int hashCode() {
        // Generamos un hash basado en los campos obligatorios
        return Objects.hash(mensaje);
    }
}
