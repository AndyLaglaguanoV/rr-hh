package ec.espe.gestorinventario.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "politicas_empresa")
public class PoliticaEmpresa {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 150)
    private String titulo;

    @Column(nullable = false, length = 60)
    private String categoria; // VACACIONES, PERMISOS, REEMBOLSOS, BENEFICIOS

    @Column(nullable = false, columnDefinition = "TEXT")
    private String contenido;

    @Column(nullable = false)
    private Boolean activo = true;

    private Integer version = 1;

    @Column(nullable = false, updatable = false)
    private java.time.LocalDateTime creadoEn;

    private java.time.LocalDateTime actualizadoEn;

    @PrePersist
    public void alCrear() {
        this.creadoEn = java.time.LocalDateTime.now();
    }

    @PreUpdate
    public void alActualizar() {
        this.actualizadoEn = java.time.LocalDateTime.now();
    }

    public PoliticaEmpresa() {}

    public PoliticaEmpresa(String titulo, String categoria, String contenido) {
        this.titulo = titulo;
        this.categoria = categoria;
        this.contenido = contenido;
        this.activo = true;
        this.version = 1;
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

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public String getContenido() {
        return contenido;
    }

    public void setContenido(String contenido) {
        this.contenido = contenido;
    }

    public Boolean getActivo() {
        return activo;
    }

    public void setActivo(Boolean activo) {
        this.activo = activo;
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    public java.time.LocalDateTime getCreadoEn() {
        return creadoEn;
    }

    public void setCreadoEn(java.time.LocalDateTime creadoEn) {
        this.creadoEn = creadoEn;
    }

    public java.time.LocalDateTime getActualizadoEn() {
        return actualizadoEn;
    }

    public void setActualizadoEn(java.time.LocalDateTime actualizadoEn) {
        this.actualizadoEn = actualizadoEn;
    }
}
