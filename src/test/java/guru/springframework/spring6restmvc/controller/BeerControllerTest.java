package guru.springframework.spring6restmvc.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import guru.springframework.spring6restmvc.model.Beer;
import guru.springframework.spring6restmvc.service.BeerService;
import guru.springframework.spring6restmvc.service.BeerServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import static org.mockito.BDDMockito.given;

@WebMvcTest(BeerController.class)
public class BeerControllerTest {

  @Autowired
  private BeerController controller;

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  ObjectMapper objectMapper;

  @MockitoBean
  private BeerService beerService;

  private BeerServiceImpl beerServiceImpl;

  @BeforeEach
  void setUp() throws Exception {
    beerServiceImpl = new BeerServiceImpl();
  }

  @Test
  public void getBeerById() throws Exception {
    Beer testBeer = beerServiceImpl.listBeers().get(0);

    given(beerService.getBeerById(testBeer.getId())).willReturn(testBeer);

    mockMvc.perform(get("/api/v1/beer/" + testBeer.getId())
        .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$.id", is(testBeer.getId().toString())))
        .andExpect(jsonPath("$.beerName", is(testBeer.getBeerName())))
        .andExpect(jsonPath("$.beerStyle", is(testBeer.getBeerStyle().toString())))
        .andExpect(jsonPath("$.upc", is(testBeer.getUpc())))
        .andExpect(jsonPath("$.price", is(testBeer.getPrice().doubleValue())))
        .andExpect(jsonPath("$.quantityOnHand", is(testBeer.getQuantityOnHand())));
  }

  @Test
  public void getBeerList() throws Exception{
    given(beerService.listBeers()).willReturn(beerServiceImpl.listBeers());

    mockMvc.perform(get("/api/v1/beer")
            .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$.length()", is(beerServiceImpl.listBeers().size())));
  }

  @Test
  public void createBeer() throws Exception {
    Beer testBeer = beerServiceImpl.listBeers().get(0);
    testBeer.setVersion(null);
    testBeer.setId(null);

    given(beerService.saveNewBeer(any(Beer.class))).willReturn(beerServiceImpl.listBeers().get(1));

    mockMvc.perform(post("/api/v1/beer")
        .accept(MediaType.APPLICATION_JSON)
        .contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(testBeer)))
        .andExpect(status().isCreated())
        .andExpect(header().exists("Location"));
  }

}
