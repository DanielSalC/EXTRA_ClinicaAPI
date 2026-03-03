package es.daw.clinicaapi.exception;

import es.daw.clinicaapi.dto.error.ApiErrorResponse;
import es.daw.clinicaapi.dto.error.ApiValidationErrorResponse;
import es.daw.clinicaapi.dto.error.FieldValidationError;
import org.springframework.http.*;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.util.List;
import java.util.stream.Collectors;

@RestControllerAdvice
public class ApiExceptionHandler {

    // 404 Not Found
    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ApiErrorResponse> handleNotFound(NotFoundException ex) {
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(new ApiErrorResponse(ex.getMessage()));
    }

    // 422 Unprocessable Entity
    @ExceptionHandler(BusinessRuleException.class)
    public ResponseEntity<ApiErrorResponse> handleBusiness(BusinessRuleException ex) {
        return ResponseEntity
                .status(HttpStatus.UNPROCESSABLE_ENTITY)
                .body(new ApiErrorResponse(ex.getMessage()));
    }

    // 400 Bad Request
    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<ApiErrorResponse> handleBadRequest(BadRequestException ex) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(new ApiErrorResponse(ex.getMessage()));
    }

    // 409 Conflict
    @ExceptionHandler(ConflictException.class)
    public ResponseEntity<ApiErrorResponse> handleConflict(ConflictException ex) {
        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(new ApiErrorResponse(ex.getMessage()));
    }

    // 400 - Validaciones JSR-380
    // MEJORA EXTRAORDINARIA: usar un dto de error más complejo para tener un array de errores con sus mensajes...
    @ExceptionHandler(MethodArgumentNotValidException.class)
    //public ResponseEntity<ApiErrorResponse> handleValidation(MethodArgumentNotValidException ex) {
    public ResponseEntity<ApiValidationErrorResponse> handleValidation(MethodArgumentNotValidException ex) {

//        String message = ex.getBindingResult()
//                .getFieldErrors()
//                .stream()
//                .map(fe -> fe.getField() + ": " + fe.getDefaultMessage())
//                .collect(Collectors.joining("; "));
//
//        return ResponseEntity
//                .badRequest()
//                .body(new ApiErrorResponse(ex.getMessage()));

        List<FieldValidationError> errors = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(fe -> new FieldValidationError(
                        fe.getField(),
                        fe.getRejectedValue(),
                        fe.getDefaultMessage()
                ))
                .collect(Collectors.toList());

        ApiValidationErrorResponse response = new ApiValidationErrorResponse("Errores de validación",errors);

        return ResponseEntity.badRequest().body(response);


    }

    // PENDIENTE MEJORA EXTRAORDINARIA: MissingServletRequestParameterException

    // 400 - Conversión de parámetros inválida
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ApiErrorResponse> handleTypeMismatch(MethodArgumentTypeMismatchException ex) {
        String message = String.format("Parámetro '%s' inválido: '%s'. Se esperaba %s",
                ex.getName(),
                ex.getValue(),
                ex.getRequiredType().getSimpleName());

        return ResponseEntity
                .badRequest()
                .body(new ApiErrorResponse(message));
    }
}
