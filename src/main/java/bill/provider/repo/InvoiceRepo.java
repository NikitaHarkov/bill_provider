package bill.provider.repo;

import bill.provider.domain.Invoice;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.time.LocalDate;
import java.util.List;

public interface InvoiceRepo extends PagingAndSortingRepository<Invoice, Integer> {

    List<Invoice> findAllByNumberContainingIgnoreCase(String number, Pageable pageable);

    @Query("SELECT c FROM Invoice c " +
            "WHERE upper(c.number) like concat('%', upper(?1), '%') and " +
            "c.invoiceDate = (?2)")
    List<Invoice> findAllByDateAndNumber(String number,LocalDate date, Pageable pageable);

    List<Invoice> findAllByInvoiceDate(LocalDate invoiceDate, Pageable pageable);

    Invoice findById(Long id);
}
