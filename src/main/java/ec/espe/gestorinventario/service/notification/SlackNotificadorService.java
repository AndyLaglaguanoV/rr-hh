package ec.espe.gestorinventario.service.notification;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service("slackNotificadorService")
public class SlackNotificadorService implements NotificadorService {

    private static final Logger log = LoggerFactory.getLogger(SlackNotificadorService.class);

    @Override
    public void notificar(String destinatario, String asunto, String mensaje, Long solicitudId) {
        log.info("[SLACK NOTIFIER] Canal/User: '{}' | Header: '{}' | Mensaje: '{}' | Solicitud #{}",
                destinatario, asunto, mensaje, solicitudId);
    }
}
