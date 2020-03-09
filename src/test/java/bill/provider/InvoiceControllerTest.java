package bill.provider;

import bill.provider.domain.Invoice;
import bill.provider.repo.InvoiceRepo;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.Order;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@RunWith(SpringRunner.class)
@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.MOCK,
        classes = ProviderApplication.class
)
@AutoConfigureMockMvc
@TestPropertySource(locations = "classpath:application-test.properties")
public class InvoiceControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private InvoiceRepo invoiceRepo;
    @Before
    public void fillDb(){
        Invoice invoice = new Invoice();
        invoice.setId(1L);
        invoice.setNumber("first test");
        invoice.setInvoiceDate(LocalDate.parse("05-05-2020", DateTimeFormatter.ofPattern("dd-MM-yyyy")));
        invoiceRepo.save(invoice);
        invoice.setId(2L);
        invoice.setNumber("second test");
        invoice.setInvoiceDate(LocalDate.parse("06-06-2020", DateTimeFormatter.ofPattern("dd-MM-yyyy")));
        invoiceRepo.save(invoice);
        invoice.setId(3L);
        invoice.setNumber("third test");
        invoice.setInvoiceDate(LocalDate.parse("05-05-2020", DateTimeFormatter.ofPattern("dd-MM-yyyy")));
        invoiceRepo.save(invoice);
        invoice.setId(4L);
        invoice.setNumber("fourth test");
        invoice.setInvoiceDate(LocalDate.parse("28-08-2020", DateTimeFormatter.ofPattern("dd-MM-yyyy")));
        invoiceRepo.save(invoice);
    }

    @Test
    public void checkIfExistsTest() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/invoice")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content()
                        .contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.content").exists());
    }

    @Test
    public void createTest() throws Exception{
        mockMvc.perform(MockMvcRequestBuilders.post("/invoice")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"number\": \"post request test\",\"invoiceDate\": \"25-05-2020\"}"))
                .andExpect(status().isOk());
        mockMvc.perform(MockMvcRequestBuilders.get("/invoice")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content()
                        .contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.content[0].id").value(5))
                .andExpect(jsonPath("$.content[0].number").value("post request test"))
                .andExpect(jsonPath("$.content[0].invoiceDate").value("25-05-2020"))
                .andExpect(jsonPath("$.content[0].createdAt").isNotEmpty());
    }

    @Test
    public void getOneTest() throws Exception{
        mockMvc.perform(MockMvcRequestBuilders.get("/invoice/3")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content()
                        .contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(3))
                .andExpect(jsonPath("$.number").value("third test"))
                .andExpect(jsonPath("$.invoiceDate").value("05-05-2020"));
    }

    @Test
    public void updateTest() throws Exception{
        mockMvc.perform(MockMvcRequestBuilders.put("/invoice/2")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"number\": \"changed invoice with PUT method\",\"invoiceDate\": \"30-05-2020\"}"))
                .andExpect(status().isOk());
        mockMvc.perform(MockMvcRequestBuilders.get("/invoice/2")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content()
                        .contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(2))
                .andExpect(jsonPath("$.number").value("changed invoice with PUT method"))
                .andExpect(jsonPath("$.invoiceDate").value("30-05-2020"))
                .andExpect(jsonPath("$.updatedAt").isNotEmpty());
    }

    @Test
    public void deleteTest() throws Exception{
        mockMvc.perform(MockMvcRequestBuilders.delete("/invoice/4")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
        mockMvc.perform(MockMvcRequestBuilders.get("/invoice")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content()
                        .contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$..[?(@.id == '4')]").doesNotExist());
    }

    @Test
    public void filterTest() throws Exception{
        mockMvc.perform(MockMvcRequestBuilders.get("/invoice")
                .param("number", "first")
                .param("invoice_date", "2020-05-05")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content()
                        .contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$..number").value("first test"))
                .andExpect(jsonPath("$..invoiceDate").value("05-05-2020"));
        mockMvc.perform(MockMvcRequestBuilders.get("/invoice")
                .param("invoice_date", "2020-05-05")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content()
                        .contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.length()").value(2));
        mockMvc.perform(MockMvcRequestBuilders.get("/invoice")
                .param("number", "th")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content()
                        .contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.length()").value(2));

    }

    @Test
    public void pageTest() throws Exception{
        mockMvc.perform(MockMvcRequestBuilders.get("/invoice")
                .param("size", "2")
                .param("page", "1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content()
                        .contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.content.length()").value(2))
                .andExpect(jsonPath("$.pageable.pageSize").value(2))
                .andExpect(jsonPath("$.pageable.pageNumber").value(1));
    }
}
