package bill.provider.repo;

import bill.provider.domain.Invoice;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface InvoiceRepo extends PagingAndSortingRepository<Invoice, Integer> {
    @Query("SELECT c FROM Invoice c " +
            "WHERE upper(c.number) like concat('%', upper(?1), '%')")
    List<Invoice> findAllByNumber(String number, Pageable pageable);

    List<Invoice> findAllByInvoiceDate(LocalDate date, Pageable pageable);

    Invoice findById(Long id);
}
