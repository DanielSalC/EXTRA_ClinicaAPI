package es.daw.clinicaapi.service;


import es.daw.clinicaapi.dto.report.TopServiceReport;
import es.daw.clinicaapi.enums.InvoiceStatus;
import es.daw.clinicaapi.exception.BadRequestException;
import es.daw.clinicaapi.repository.InvoiceLineRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cglib.core.Local;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ReportService {

    @Value("${report.pagination.min-size}")
    private int minSize;
    @Value("${report.pagination.max-size}")
    private int maxSize;

    private final InvoiceLineRepository invoiceLineRepository;

    public List<TopServiceReport> getTopServices(LocalDateTime from,
                                                 LocalDateTime to,
                                                 InvoiceStatus status,
                                                 Pageable pageable) {

        // 1. VALIDACIONES Y REGLAS DE NEGOCIO
        // Mejora: el rango que se pueda configurar en daw.properties
        if (from.isBefore(LocalDateTime.now()) && to.isAfter(LocalDateTime.now())) {
            if (pageable.getPageSize() < minSize || pageable.getPageSize() > maxSize)
                throw new BadRequestException("El tamaño de página debe estar entre "+minSize+" y "+maxSize);
        }
        else
            throw new BadRequestException("mal las fechas el to debe posterior al from !!!!!");

        // 2. LLAMADA AL REPOSITORIO
        return invoiceLineRepository.topServicesByIssuedAt(from, to, status, pageable);

    }

}
