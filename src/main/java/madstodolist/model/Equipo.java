package madstodolist.model;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;
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

    @OneToMany(mappedBy = "equipo", fetch = FetchType.EAGER)
    Set<Usuario> usuarios = new HashSet<>();

    public Equipo(String nombre){ this.nombre = nombre;}

    public Long getId() { return id; }

    public String getNombre() { return nombre; }
}