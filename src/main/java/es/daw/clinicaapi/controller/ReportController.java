package es.daw.clinicaapi.controller;

import es.daw.clinicaapi.dto.report.TopServiceReport;
import es.daw.clinicaapi.enums.InvoiceStatus;
import es.daw.clinicaapi.service.ReportService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/reports")
@RequiredArgsConstructor
public class ReportController {

    private final ReportService reportService;

    @GetMapping("/top-services")
    @PreAuthorize("hasRole('BILLING')")
    // @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    // Fecha incorrecta lanzaría MethodArgumentTypeMismatchException
    // Sin la T falla: fecha correcta 2026-05-01T10:00:00

    // Indicar explícitamente a Spring cómo parsear el texto del request a LocalDateTime
    // Spring ya intenta usar ISO-8601 para LocalDateTime
    // pero es buena práctica indicarlo explícitamente porque evita ambigüedades

    public ResponseEntity<List<TopServiceReport>> topServices(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime from,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime to,
            @RequestParam(required = false)InvoiceStatus status,
            @PageableDefault(page=0, size = 3) Pageable pageable
            ) {

        // MEJORA: si no mando un parámetro obligatorio salta esta excepción:
        // [org.springframework.web.bind.MissingServletRequestParameterException:
        // Required request parameter 'to' for method parameter type LocalDateTime is not present]
//        {
//            "message": "falta el parámetro xxxxxx"
//        }


        // MEJORA: estado incorrecto
        // MethodArgumentTypeMismatchException: Method parameter 'status':
        // Failed to convert value of type 'java.lang.String' to required
        // type 'es.daw.clinicaapi.enums.InvoiceStatus';

        List<TopServiceReport> result = reportService.getTopServices(from, to, status, pageable);

        return ResponseEntity.ok(result);

    }

//
//    // ENDPOINT PÚBLICO!!!!
//    @GetMapping("/top-services2")
//    // Otra solución para valor de status incorrecto... no se ajusta a nuestra forma de trabajar en clase
//    public ResponseEntity<List<TopServiceReport>> topServices2(
//            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime from,
//            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime to,
//            @RequestParam(required = false) String status,
//            @PageableDefault(page=0, size = 3) Pageable pageable
//    ) {
//
//        if (status != null && !status.isBlank()) {
//            try {
//                es.daw.clinicaapi.enums.InvoiceStatus status = InvoiceStatus.valueOf(status.trim().toUpperCase());
//            } catch (IllegalArgumentException e) {
//                return ResponseEntity
//                        .badRequest()
//                        .body("Valor inválido para 'status': " + status);
//            }
//        }
//    }
}


