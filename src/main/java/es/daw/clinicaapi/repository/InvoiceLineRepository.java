package es.daw.clinicaapi.repository;

import es.daw.clinicaapi.dto.report.TopServiceReport;
import es.daw.clinicaapi.entity.InvoiceLine;
import es.daw.clinicaapi.enums.InvoiceStatus;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface InvoiceLineRepository extends JpaRepository<InvoiceLine, Long> {

//    @Query("""
//    select l
//    from InvoiceLine l
//    """)
//    List<TopServiceReport> topServicesByIssuedAt(
//            LocalDateTime from,
//            LocalDateTime to,
//            InvoiceStatus status,
//            Pageable pageable
//    );

    @Query("""
        select new es.daw.clinicaapi.dto.report.TopServiceReport(
            s.id,
            s.name,
            count(l),
            sum(l.quantity),
            sum(l.lineTotal)
            )
                from InvoiceLine l
                    join l.invoice i
                    join l.service s
                where i.issuedAt is not null
                and i.issuedAt >= :from and i.issuedAt <= :to
                and (:status is null or i.status = :status)
            group by s.id, s.name
            order by sum(l.lineTotal) desc
                
    """)
    List<TopServiceReport> topServicesByIssuedAt(
            @Param("from") LocalDateTime from,
            @Param("to") LocalDateTime to,
            @Param("status") InvoiceStatus status,
            Pageable pageable
    );


}