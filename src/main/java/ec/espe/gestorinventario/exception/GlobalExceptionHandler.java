package ec.espe.gestorinventario.exception;

import ec.espe.gestorinventario.dto.response.ErrorResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import java.time.LocalDateTime;
import java.util.List;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(RecursoNotFoundException.class)
    public ResponseEntity<ErrorResponse> noEncontrado(RecursoNotFoundException ex, HttpServletRequest req) {
        return build(HttpStatus.NOT_FOUND, ex.getMessage(), req, List.of());
    }

    @ExceptionHandler(NoResourceFoundException.class)
    public ResponseEntity<ErrorResponse> rutaNoEncontrada(NoResourceFoundException ex, HttpServletRequest req) {
        return build(HttpStatus.NOT_FOUND, "Recurso no encontrado: " + ex.getResourcePath(), req, List.of());
    }

    @ExceptionHandler(ConflictoException.class)
    public ResponseEntity<ErrorResponse> conflicto(ConflictoException ex, HttpServletRequest req) {
        return build(HttpStatus.CONFLICT, ex.getMessage(), req, List.of());
    }

    @ExceptionHandler(ReglaNegocioException.class)
    public ResponseEntity<ErrorResponse> regla(ReglaNegocioException ex, HttpServletRequest req) {
        return build(HttpStatus.UNPROCESSABLE_ENTITY, ex.getMessage(), req, List.of());
    }

    @ExceptionHandler(AccesoDenegadoException.class)
    public ResponseEntity<ErrorResponse> accesoDenegado(AccesoDenegadoException ex, HttpServletRequest req) {
        return build(HttpStatus.FORBIDDEN, ex.getMessage(), req, List.of());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> validacion(MethodArgumentNotValidException ex, HttpServletRequest req) {
        List<String> campos = ex.getBindingResult().getFieldErrors().stream()
                .map(f -> f.getField() + ": " + f.getDefaultMessage())
                .toList();
        return build(HttpStatus.BAD_REQUEST, "La solicitud contiene datos inválidos", req, campos);
    }

    @ExceptionHandler({
            HttpMessageNotReadableException.class,
            MethodArgumentTypeMismatchException.class,
            MissingServletRequestParameterException.class,
            IllegalArgumentException.class
    })
    public ResponseEntity<ErrorResponse> formato(Exception ex, HttpServletRequest req) {
        return build(HttpStatus.BAD_REQUEST, "Formato de la solicitud o del parámetro incorrecto: " + ex.getMessage(), req, List.of());
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<ErrorResponse> metodo(HttpRequestMethodNotSupportedException ex, HttpServletRequest req) {
        return build(HttpStatus.METHOD_NOT_ALLOWED, ex.getMessage(), req, List.of());
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ErrorResponse> integridad(DataIntegrityViolationException ex, HttpServletRequest req) {
        return build(HttpStatus.CONFLICT, "La operación viola una restricción de la base de datos", req, List.of());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> general(Exception ex, HttpServletRequest req) {
        log.error("Error no controlado", ex);
        return build(HttpStatus.INTERNAL_SERVER_ERROR, "Error interno del servidor", req, List.of());
    }

    private ResponseEntity<ErrorResponse> build(HttpStatus s, String msg, HttpServletRequest req, List<String> errores) {
        return ResponseEntity.status(s).body(new ErrorResponse(
                LocalDateTime.now(),
                s.value(),
                s.getReasonPhrase(),
                msg,
                req.getRequestURI(),
                errores
        ));
    }
}
