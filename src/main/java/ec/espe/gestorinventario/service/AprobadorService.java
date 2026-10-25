package ec.espe.gestorinventario.service;

import ec.espe.gestorinventario.entity.Rol;
import ec.espe.gestorinventario.entity.Solicitud;
import ec.espe.gestorinventario.entity.Usuario;
import ec.espe.gestorinventario.repository.UsuarioRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AprobadorService {

    private final UsuarioRepository usuarioRepository;

    public AprobadorService(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    public Usuario determinarSiguienteAprobador(Solicitud solicitud) {
        Usuario empleado = solicitud.getEmpleado();

        // Si el trámite requiere jefe directo y el empleado lo tiene asignado
        if (Boolean.TRUE.equals(solicitud.getTipoSolicitud().getRequiereAprobacionJefe())
                && empleado.getJefeDirecto() != null) {
            return empleado.getJefeDirecto();
        }

        // Si no tiene jefe o no lo requiere, asignar a un Admin de RR.HH.
        List<Usuario> todos = usuarioRepository.findAll();
        for (Usuario u : todos) {
            for (Rol r : u.getRoles()) {
                if ("ROLE_ADMIN_RRHH".equals(r.getNombre())) {
                    return u;
                }
            }
        }

        // Fallback: retornar al mismo empleado si no hay admin configurado
        return empleado;
    }
}
