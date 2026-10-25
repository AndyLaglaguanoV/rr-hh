package ec.espe.gestorinventario.service.notification;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

@Service("emailNotificadorService")
@Primary
public class EmailNotificadorService implements NotificadorService {

    private static final Logger log = LoggerFactory.getLogger(EmailNotificadorService.class);

    @Override
    public void notificar(String destinatario, String asunto, String mensaje, Long solicitudId) {
        log.info("[EMAIL NOTIFIER] A: '{}' | Asunto: '{}' | Mensaje: '{}' | Solicitud #{}",
                destinatario, asunto, mensaje, solicitudId);
    }
}
