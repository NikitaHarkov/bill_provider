package bill.provider.controller;

import bill.provider.domain.Invoice;
import bill.provider.repo.InvoiceRepo;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@RestController
@RequestMapping("/invoice")
public class InvoiceController {
    private final InvoiceRepo invoiceRepo;

    public InvoiceController(InvoiceRepo invoiceRepo) {
        this.invoiceRepo = invoiceRepo;
    }

    @GetMapping
    public Iterable<Invoice> list(
            @RequestParam(name = "number", required = false) String number,
            @RequestParam(name = "invoice_date", required = false)
                @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
            @PageableDefault(sort = {"id"}, direction = Sort.Direction.DESC) Pageable pageable) {
        if (!StringUtils.isEmpty(date) && !StringUtils.isEmpty(number)) {
            return invoiceRepo.findAllByDateAndNumber(number,date, pageable);
        }else if (!StringUtils.isEmpty(number) && StringUtils.isEmpty(date)) {
            return invoiceRepo.findAllByNumberContainingIgnoreCase(number, pageable);
        }else if(!StringUtils.isEmpty(date) && StringUtils.isEmpty(number)){
            return invoiceRepo.findAllByInvoiceDate(date,pageable);
        }
        return invoiceRepo.findAll(pageable);

    }

    @PostMapping
    public Invoice create(@RequestBody Invoice invoice) {
        invoice.setCreatedAt(LocalDateTime.now());
        return invoiceRepo.save(invoice);
    }

    @GetMapping("{id}")
    public Invoice getOne(@PathVariable("id") Long id) {
        return invoiceRepo.findById(id);

    }

    @PutMapping("{id}")
    public Invoice update(
            @PathVariable("id") Long id,
            @RequestBody Invoice invoice) {
        Invoice invoiceFromDb = invoiceRepo.findById(id);
        invoice.setUpdatedAt(LocalDateTime.now());
        BeanUtils.copyProperties(invoice, invoiceFromDb, "id");
        return invoiceRepo.save(invoiceFromDb);
    }

    @DeleteMapping("{id}")
    public void delete(@PathVariable("id") Long id) {
        invoiceRepo.delete(invoiceRepo.findById(id));
    }


}
