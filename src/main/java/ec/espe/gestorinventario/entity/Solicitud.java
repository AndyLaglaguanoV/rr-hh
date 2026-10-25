package ec.espe.gestorinventario.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "solicitudes")
public class Solicitud {

    public enum EstadoSolicitud {
        PENDIENTE,
        APROBADA_JEFE,
        APROBADA_RRHH,
        RECHAZADA
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "empleado_id", nullable = false)
    @JsonIgnoreProperties({"passwordHash", "jefeDirecto", "roles"})
    private Usuario empleado;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "tipo_solicitud_id", nullable = false)
    private TipoSolicitud tipoSolicitud;

    @Column(nullable = false)
    private LocalDate fechaInicio;

    private LocalDate fechaFin;

    @Column(nullable = false)
    private Integer diasSolicitados = 1;

    @Column(nullable = false, length = 500)
    private String motivo;

    private BigDecimal montoReembolso;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private EstadoSolicitud estado = EstadoSolicitud.PENDIENTE;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "aprobador_actual_id")
    @JsonIgnoreProperties({"passwordHash", "jefeDirecto", "roles"})
    private Usuario aprobadorActual;

    @Column(length = 500)
    private String observacionAprobador;

    @Column(nullable = false, updatable = false)
    private LocalDateTime fechaCreacion;

    @PrePersist
    public void prePersist() {
        this.fechaCreacion = LocalDateTime.now();
    }

    public Solicitud() {}

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Usuario getEmpleado() {
        return empleado;
    }

    public void setEmpleado(Usuario empleado) {
        this.empleado = empleado;
    }

    public TipoSolicitud getTipoSolicitud() {
        return tipoSolicitud;
    }

    public void setTipoSolicitud(TipoSolicitud tipoSolicitud) {
        this.tipoSolicitud = tipoSolicitud;
    }

    public LocalDate getFechaInicio() {
        return fechaInicio;
    }

    public void setFechaInicio(LocalDate fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    public LocalDate getFechaFin() {
        return fechaFin;
    }

    public void setFechaFin(LocalDate fechaFin) {
        this.fechaFin = fechaFin;
    }

    public Integer getDiasSolicitados() {
        return diasSolicitados;
    }

    public void setDiasSolicitados(Integer diasSolicitados) {
        this.diasSolicitados = diasSolicitados;
    }

    public String getMotivo() {
        return motivo;
    }

    public void setMotivo(String motivo) {
        this.motivo = motivo;
    }

    public BigDecimal getMontoReembolso() {
        return montoReembolso;
    }

    public void setMontoReembolso(BigDecimal montoReembolso) {
        this.montoReembolso = montoReembolso;
    }

    public EstadoSolicitud getEstado() {
        return estado;
    }

    public void setEstado(EstadoSolicitud estado) {
        this.estado = estado;
    }

    public Usuario getAprobadorActual() {
        return aprobadorActual;
    }

    public void setAprobadorActual(Usuario aprobadorActual) {
        this.aprobadorActual = aprobadorActual;
    }

    public String getObservacionAprobador() {
        return observacionAprobador;
    }

    public void setObservacionAprobador(String observacionAprobador) {
        this.observacionAprobador = observacionAprobador;
    }

    public LocalDateTime getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(LocalDateTime fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }
}
