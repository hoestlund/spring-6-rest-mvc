package guru.springframework.spring6restmvc.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import guru.springframework.spring6restmvc.model.Customer;
import guru.springframework.spring6restmvc.service.BeerService;
import guru.springframework.spring6restmvc.service.CustomerService;
import guru.springframework.spring6restmvc.service.CustomerServiceImpl;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.is;

@WebMvcTest(CustomerController.class)
public class CustomerControllerTest {

  @Autowired
  private CustomerController controller;

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  ObjectMapper objectMapper;

  @MockitoBean
  private CustomerService customerService;

  private CustomerServiceImpl customerServiceImpl = new CustomerServiceImpl();

  @Test
  public void testGetCustomers() throws Exception {
    Customer testCustomer = customerServiceImpl.getAllCustomers().get(0);

    given(customerService.getCustomerById(testCustomer.getId())).willReturn(testCustomer);

    mockMvc.perform(get("/api/v1/customer/" + testCustomer.getId())
            .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.id",is(testCustomer.getId().toString())))
        .andExpect(jsonPath("$.name",is(testCustomer.getName())));

  }

  @Test
  public void createCustomer() throws Exception {
    Customer testCustomer = customerServiceImpl.getAllCustomers().get(0);
    testCustomer.setVersion(null);
    testCustomer.setId(null);

    given(customerService.saveNewCustomer(any(Customer.class))).willReturn(customerServiceImpl.getAllCustomers().get(1));

    mockMvc.perform(post("/api/v1/customer")
            .accept(MediaType.APPLICATION_JSON).contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(testCustomer)))
        .andExpect(status().isCreated())
        .andExpect(header().exists("Location"));
  }

  @Test
  public void updateCustomer() throws Exception {
    Customer testCustomer = customerServiceImpl.getAllCustomers().get(0);

    mockMvc.perform(put("/api/v1/customer/" + testCustomer.getId())
        .accept(MediaType.APPLICATION_JSON)
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(testCustomer)));

    verify(customerService).updateCustomerById(testCustomer.getId(), testCustomer);
  }
}
