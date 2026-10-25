package ec.espe.gestorinventario.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "usuarios")
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 120)
    private String email;

    @JsonIgnore
    @Column(nullable = false)
    private String passwordHash;

    @Column(nullable = false, length = 150)
    private String nombreCompleto;

    @Column(nullable = false)
    private Integer diasVacacionesDisponibles = 15;

    @Column(nullable = false)
    private Boolean activo = true;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "jefe_id")
    @JsonIgnore
    private Usuario jefeDirecto;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "usuario_roles",
            joinColumns = @JoinColumn(name = "usuario_id"),
            inverseJoinColumns = @JoinColumn(name = "rol_id")
    )
    private Set<Rol> roles = new HashSet<>();

    public Usuario() {}

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }

    public String getNombreCompleto() {
        return nombreCompleto;
    }

    public void setNombreCompleto(String nombreCompleto) {
        this.nombreCompleto = nombreCompleto;
    }

    public Integer getDiasVacacionesDisponibles() {
        return diasVacacionesDisponibles;
    }

    public void setDiasVacacionesDisponibles(Integer diasVacacionesDisponibles) {
        this.diasVacacionesDisponibles = diasVacacionesDisponibles;
    }

    public Boolean getActivo() {
        return activo;
    }

    public void setActivo(Boolean activo) {
        this.activo = activo;
    }

    public Usuario getJefeDirecto() {
        return jefeDirecto;
    }

    public void setJefeDirecto(Usuario jefeDirecto) {
        this.jefeDirecto = jefeDirecto;
    }

    public Set<Rol> getRoles() {
        return roles;
    }

    public void setRoles(Set<Rol> roles) {
        this.roles = roles;
    }
}
