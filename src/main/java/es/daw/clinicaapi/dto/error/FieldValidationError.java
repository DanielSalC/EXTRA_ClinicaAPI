package es.daw.clinicaapi.dto.error;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FieldValidationError {
    private String field;
    private Object rejectedValue;
    private String message;
}