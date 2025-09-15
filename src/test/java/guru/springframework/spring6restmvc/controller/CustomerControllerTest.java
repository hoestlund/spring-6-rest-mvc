package guru.springframework.spring6restmvc.controller;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

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
}
