package bill.provider;

import bill.provider.repo.InvoiceRepo;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.MOCK,
        classes = ProviderApplication.class
)
@AutoConfigureMockMvc
//@TestPropertySource(locations = "classpath:application-test.properties")
public class InvoiceControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private InvoiceRepo invoiceRepo;

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
        //jsonPath("$.error[?(@.errorMessage=='Fixed Error Message')]").exists()
    }

    @Test
    public void updateTest() throws Exception{

    }

    @Test
    public void deleteTest() throws Exception{

    }

    @Test
    public void filterTest() throws Exception{

    }

    @Test
    public void pageTest() throws Exception{

    }






}
