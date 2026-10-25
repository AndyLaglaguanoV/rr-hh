package ec.espe.gestorinventario.exception;

public class RecursoNotFoundException extends RuntimeException {
    public RecursoNotFoundException(String mensaje) {
        super(mensaje);
    }

    public RecursoNotFoundException(Long id) {
        super("No existe el recurso con id " + id);
    }
}
