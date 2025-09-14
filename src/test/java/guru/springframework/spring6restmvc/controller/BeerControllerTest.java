package guru.springframework.spring6restmvc.controller;

import guru.springframework.spring6restmvc.service.BeerService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static java.util.UUID.randomUUID;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(BeerController.class)
public class BeerControllerTest {

  @Autowired
  private BeerController controller;

  @Autowired
  private MockMvc mockMvc;

  @MockitoBean
  private BeerService beerService;

  @Test
  public void getBeerById() throws Exception {
    mockMvc.perform(get("/api/v1/beer/" + randomUUID())
        .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk());
  }

}
