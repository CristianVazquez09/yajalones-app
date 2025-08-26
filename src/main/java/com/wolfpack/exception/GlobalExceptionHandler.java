package com.wolfpack.exception;

import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolationException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.*;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import java.net.URI;
import java.time.Instant;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    // ---------- Helpers ----------

    private ProblemDetail problem(HttpStatus status, String title, String detail, HttpServletRequest req) {
        ProblemDetail pd = ProblemDetail.forStatusAndDetail(status, detail);
        pd.setTitle(title);
        pd.setType(URI.create("https://api.revolucion-atletica.com/errors/" + status.value()));
        pd.setProperty("timestamp", Instant.now());
        if (req != null) {
            pd.setProperty("path", req.getRequestURI());
            pd.setProperty("method", req.getMethod()); // String
        }
        return pd;
    }

    private ResponseEntity<Object> wrap(ProblemDetail pd) {
        return ResponseEntity.status(HttpStatus.valueOf(pd.getStatus())).body(pd);
    }

    // ---------- 404 – Recurso estático / ruta no encontrada (Spring 6.1+) ----------

    @Override
    protected ResponseEntity<Object> handleNoResourceFoundException(
            NoResourceFoundException ex,
            HttpHeaders headers,
            HttpStatusCode status,
            WebRequest request) {

        HttpServletRequest httpReq = ((ServletWebRequest) request).getRequest();

        ProblemDetail pd = ProblemDetail.forStatus(HttpStatus.NOT_FOUND);
        pd.setTitle("Endpoint no encontrado");
        pd.setDetail("La ruta solicitada no existe: " + httpReq.getRequestURI());
        pd.setType(URI.create("https://api.revolucion-atletica.com/errors/404"));
        pd.setProperty("method", httpReq.getMethod()); // String seguro para Jackson
        pd.setProperty("timestamp", Instant.now());

        return wrap(pd);
    }

    @Override
    protected ResponseEntity<Object> handleNoHandlerFoundException(
            NoHandlerFoundException ex,
            HttpHeaders headers,
            HttpStatusCode status,
            WebRequest request) {

        HttpServletRequest httpReq = ((ServletWebRequest) request).getRequest();

        ProblemDetail pd = ProblemDetail.forStatus(HttpStatus.NOT_FOUND);
        pd.setTitle("Endpoint no encontrado");
        pd.setDetail("La ruta solicitada no existe: " + ex.getRequestURL());
        pd.setType(URI.create("https://api.revolucion-atletica.com/errors/404"));
        pd.setProperty("method", httpReq.getMethod());
        pd.setProperty("timestamp", Instant.now());
        return wrap(pd);
    }

    // ---------- 400 – JSON mal formado / tipos ----------

    @Override
    protected ResponseEntity<Object> handleHttpMessageNotReadable(
            HttpMessageNotReadableException ex,
            HttpHeaders headers,
            HttpStatusCode status,
            WebRequest request) {

        ProblemDetail pd = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, "Cuerpo de la petición inválido.");
        pd.setTitle("Malformed JSON");
        pd.setProperty("hint", "Verifica el JSON y los tipos de datos.");
        pd.setProperty("timestamp", Instant.now());
        return wrap(pd);
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<Object> handleTypeMismatch(MethodArgumentTypeMismatchException ex, HttpServletRequest req) {
        ProblemDetail pd = problem(
                HttpStatus.BAD_REQUEST,
                "Tipo de parámetro inválido",
                "El parámetro '" + ex.getName() + "' no es del tipo esperado.",
                req
        );
        pd.setProperty("expectedType", ex.getRequiredType() != null ? ex.getRequiredType().getSimpleName() : "desconocido");
        pd.setProperty("value", String.valueOf(ex.getValue()));
        return wrap(pd);
    }

    // ---------- 400/422 – Validación Bean Validation ----------

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
            MethodArgumentNotValidException ex,
            HttpHeaders headers,
            HttpStatusCode status,
            WebRequest request) {

        HttpStatus httpStatus = HttpStatus.BAD_REQUEST; // o UNPROCESSABLE_ENTITY si prefieres 422

        List<Map<String, Object>> fields = ex.getBindingResult().getFieldErrors().stream()
                .map(fe -> {
                    Map<String, Object> m = new LinkedHashMap<>();
                    m.put("field", fe.getField());
                    m.put("message", fe.getDefaultMessage());
                    m.put("rejected", fe.getRejectedValue());
                    return m;
                }).toList();

        ProblemDetail pd = ProblemDetail.forStatus(httpStatus);
        pd.setTitle("Validation Error");
        pd.setDetail("Uno o más campos no son válidos.");
        pd.setProperty("errors", fields);
        pd.setProperty("timestamp", Instant.now());
        return wrap(pd);
    }

    @ExceptionHandler(ConstraintViolationException.class) // para @RequestParam/@PathVariable
    public ResponseEntity<Object> handleConstraintViolation(ConstraintViolationException ex, HttpServletRequest req) {
        List<Map<String, Object>> violations = ex.getConstraintViolations().stream().map(cv -> {
            Map<String, Object> m = new LinkedHashMap<>();
            m.put("property", cv.getPropertyPath().toString());
            m.put("message", cv.getMessage());
            m.put("invalid", cv.getInvalidValue());
            return m;
        }).toList();

        ProblemDetail pd = problem(HttpStatus.BAD_REQUEST, "Constraint Violation", "Parámetros inválidos.", req);
        pd.setProperty("errors", violations);
        return wrap(pd);
    }

    // ---------- 405 / 415 ----------

    @Override
    protected ResponseEntity<Object> handleHttpRequestMethodNotSupported(
            HttpRequestMethodNotSupportedException ex,
            HttpHeaders headers,
            HttpStatusCode status,
            WebRequest request) {

        HttpServletRequest httpReq = ((ServletWebRequest) request).getRequest();

        ProblemDetail pd = ProblemDetail.forStatus(HttpStatus.METHOD_NOT_ALLOWED);
        pd.setTitle("Método no permitido");
        pd.setDetail("El método '" + ex.getMethod() + "' no está permitido en " + httpReq.getRequestURI());
        pd.setType(URI.create("https://api.revolucion-atletica.com/errors/405"));

        // ¡Clave! convertir a List<String> para evitar el error de Jackson con HttpMethod
        List<String> supported = ex.getSupportedHttpMethods() == null
                ? List.of()
                : ex.getSupportedHttpMethods().stream().map(HttpMethod::name).toList();
        pd.setProperty("supportedMethods", supported);

        pd.setProperty("method", httpReq.getMethod());
        pd.setProperty("timestamp", Instant.now());
        return wrap(pd);
    }

    @Override
    protected ResponseEntity<Object> handleHttpMediaTypeNotSupported(
            HttpMediaTypeNotSupportedException ex,
            HttpHeaders headers,
            HttpStatusCode status,
            WebRequest request) {

        ProblemDetail pd = ProblemDetail.forStatus(HttpStatus.UNSUPPORTED_MEDIA_TYPE);
        pd.setTitle("Media type no soportado");
        pd.setDetail("Content-Type no soportado: " + ex.getContentType());

        // Convertir a List<String> para evitar tipos raros en JSON
        List<String> supported = ex.getSupportedMediaTypes() == null
                ? List.of()
                : ex.getSupportedMediaTypes().stream().map(MediaType::toString).collect(Collectors.toList());
        pd.setProperty("supported", supported);

        pd.setProperty("timestamp", Instant.now());
        return wrap(pd);
    }

    // ---------- 409 – Integridad de datos (únicos/foránea) ----------

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<Object> handleDataIntegrity(DataIntegrityViolationException ex, HttpServletRequest req) {
        ProblemDetail pd = problem(
                HttpStatus.CONFLICT,
                "Conflicto de integridad",
                "La operación viola restricciones de datos (único/foránea/etc.).",
                req
        );
        return wrap(pd);
    }

    // ---------- 500 – Fallback general ----------

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> handleAll(Exception ex, HttpServletRequest req) {
        ProblemDetail pd = problem(
                HttpStatus.INTERNAL_SERVER_ERROR,
                "Error interno",
                ex.getLocalizedMessage(),
                req
        );
        pd.setType(URI.create("https://api.los-yajalones.com/errors/500"));
        return wrap(pd);
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<Object> handleEntityNotFoundException(EntityNotFoundException ex, HttpServletRequest req) {
        ProblemDetail pd = problem(
                HttpStatus.NOT_FOUND,
                "Entidad no encontrada",
                ex.getMessage(),
                req
        );
        return wrap(pd);
    }
}
