package ec.espe.gestorinventario.service.notification;

public interface NotificadorService {
    void notificar(String destinatario, String asunto, String mensaje, Long solicitudId);
}
