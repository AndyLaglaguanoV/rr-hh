package ec.espe.gestorinventario.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "auditoria_logs")
public class AuditoriaLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 120)
    private String usuarioEmail;

    @Column(nullable = false, length = 15)
    private String metodoHttp;

    @Column(nullable = false, length = 255)
    private String uriAccedida;

    @Column(length = 60)
    private String ipOrigen;

    private Integer codigoEstadoHttp;

    @Column(nullable = false)
    private LocalDateTime timestamp;

    @Column(columnDefinition = "TEXT")
    private String metadata;

    public AuditoriaLog() {}

    public AuditoriaLog(String usuarioEmail, String metodoHttp, String uriAccedida, String ipOrigen,
                        Integer codigoEstadoHttp, LocalDateTime timestamp, String metadata) {
        this.usuarioEmail = usuarioEmail;
        this.metodoHttp = metodoHttp;
        this.uriAccedida = uriAccedida;
        this.ipOrigen = ipOrigen;
        this.codigoEstadoHttp = codigoEstadoHttp;
        this.timestamp = timestamp;
        this.metadata = metadata;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsuarioEmail() {
        return usuarioEmail;
    }

    public void setUsuarioEmail(String usuarioEmail) {
        this.usuarioEmail = usuarioEmail;
    }

    public String getMetodoHttp() {
        return metodoHttp;
    }

    public void setMetodoHttp(String metodoHttp) {
        this.metodoHttp = metodoHttp;
    }

    public String getUriAccedida() {
        return uriAccedida;
    }

    public void setUriAccedida(String uriAccedida) {
        this.uriAccedida = uriAccedida;
    }

    public String getIpOrigen() {
        return ipOrigen;
    }

    public void setIpOrigen(String ipOrigen) {
        this.ipOrigen = ipOrigen;
    }

    public Integer getCodigoEstadoHttp() {
        return codigoEstadoHttp;
    }

    public void setCodigoEstadoHttp(Integer codigoEstadoHttp) {
        this.codigoEstadoHttp = codigoEstadoHttp;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public String getMetadata() {
        return metadata;
    }

    public void setMetadata(String metadata) {
        this.metadata = metadata;
    }
}
