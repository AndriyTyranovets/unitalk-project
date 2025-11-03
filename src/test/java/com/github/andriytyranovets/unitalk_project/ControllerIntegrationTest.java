package com.github.andriytyranovets.unitalk_project;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.MOCK,
        classes = UnitalkProjectApplication.class)
@AutoConfigureMockMvc
class ControllerIntegrationTest {

    @Autowired
    private MockMvc mvc;

    @Test
    public void acceptBet_ok()
            throws Exception {
        mvc.perform(MockMvcRequestBuilders.post("/bet")
                        .param("car", "BMW")
                        .param("amount", "1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().string("Bet accepted"));
    }

    @Test
    public void acceptBet_carNotFound()
            throws Exception {
        mvc.perform(MockMvcRequestBuilders.post("/bet")
                        .param("car", "ABC")
                        .param("amount", "1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().string("Error: Car not found"));
    }

    @Test
    public void acceptBet_amountTooLow()
            throws Exception {
        mvc.perform(MockMvcRequestBuilders.post("/bet")
                        .param("car", "BMW")
                        .param("amount", "-1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().string("Error: Amount must be above 0"));
    }

    @Test
    public void acceptBet_amountTooHigh()
            throws Exception {
        mvc.perform(MockMvcRequestBuilders.post("/bet")
                        .param("car", "BMW")
                        .param("amount", "9000000")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().string("Error: bet is too large for online processing"));
    }

    @Test
    public void getAllBets_ok()
            throws Exception {
        mvc.perform(MockMvcRequestBuilders.post("/bet")
                .param("car", "BMW")
                .param("amount", "100")
                .contentType(MediaType.APPLICATION_JSON));

        mvc.perform(MockMvcRequestBuilders.get("/bet")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$").isArray());
    }

    @Test
    public void getBetsOnCar_ok()
            throws Exception {
        mvc.perform(MockMvcRequestBuilders.post("/bet")
                .param("car", "BMW")
                .param("amount", "100")
                .contentType(MediaType.APPLICATION_JSON));

        mvc.perform(MockMvcRequestBuilders.get("/bet")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$").isArray());
    }
}