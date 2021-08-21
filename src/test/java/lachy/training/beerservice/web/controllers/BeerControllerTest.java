package lachy.training.beerservice.web.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import lachy.training.beerservice.bootstrap.BeerLoader;
import lachy.training.beerservice.services.BeerService;
import lachy.training.beerservice.web.models.BeerDto;
import lachy.training.beerservice.web.models.BeerStyleEnum;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.math.BigDecimal;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(BeerController.class)
class BeerControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    BeerService beerService;

    @Autowired
    ObjectMapper objectMapper;

    private static final String endPoint = "/api/v1/beer/";

    @Test
    void getTest() throws Exception {

        given(beerService.getBeerById(any())).willReturn(getValidBeerDto());

        ResultActions resultActions = mockMvc.perform(
                get(String.format("%s%s", endPoint, UUID.randomUUID()))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void saveTest() throws Exception {
        BeerDto beerDto = getValidBeerDto();
        String beerDtoJson = objectMapper.writeValueAsString(beerDto);

        given(beerService.saveNewBeer(any())).willReturn(getValidBeerDto());

        mockMvc.perform( post(endPoint)
                .contentType(MediaType.APPLICATION_JSON)
                .content(beerDtoJson)
        ).andExpect(status().isCreated());
    }

    @Test
    void updateTest() throws Exception {
        given(beerService.updateBeer(any(), any())).willReturn(getValidBeerDto());

        BeerDto beerDto = getValidBeerDto();
        String beerToJson = objectMapper.writeValueAsString(beerDto);

        mockMvc.perform( put(String.format("%s%s", endPoint, UUID.randomUUID()))
                .contentType(MediaType.APPLICATION_JSON)
                .content(beerToJson)
        ).andExpect(status().isNoContent());
    }

     private BeerDto getValidBeerDto(){
        return BeerDto.builder()
                .name("Lachy's Beer")
                .style(BeerStyleEnum.ALE)
                .price(new BigDecimal("2.99"))
                .upc(BeerLoader.BEER_1_UPC)
                .build();
    }
}