package ec.espe.gestorinventario.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "tipos_solicitud")
public class TipoSolicitud {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 50)
    private String codigo; // VACACIONES, PERMISO_MEDICO, REEMBOLSO, CERTIFICADO_LABORAL

    @Column(nullable = false, length = 100)
    private String nombre;

    @Column(nullable = false)
    private Boolean requiereAprobacionJefe = true;

    @Column(nullable = false)
    private Integer diasMaximos = 30;

    public TipoSolicitud() {}

    public TipoSolicitud(String codigo, String nombre, Boolean requiereAprobacionJefe, Integer diasMaximos) {
        this.codigo = codigo;
        this.nombre = nombre;
        this.requiereAprobacionJefe = requiereAprobacionJefe;
        this.diasMaximos = diasMaximos;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Boolean getRequiereAprobacionJefe() {
        return requiereAprobacionJefe;
    }

    public void setRequiereAprobacionJefe(Boolean requiereAprobacionJefe) {
        this.requiereAprobacionJefe = requiereAprobacionJefe;
    }

    public Integer getDiasMaximos() {
        return diasMaximos;
    }

    public void setDiasMaximos(Integer diasMaximos) {
        this.diasMaximos = diasMaximos;
    }
}
