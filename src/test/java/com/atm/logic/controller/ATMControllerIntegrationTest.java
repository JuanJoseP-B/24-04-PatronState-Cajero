package com.atm.logic.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class ATMControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void testGetStatus() throws Exception {
        mockMvc.perform(get("/api/atm/status"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.currentState").value("IdleState"))
                .andExpect(jsonPath("$.success").value(true));
    }

    @Test
    void testInsertCard() throws Exception {
        mockMvc.perform(post("/api/atm/insert-card"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.currentState").value("CardInsertedState"))
                .andExpect(jsonPath("$.success").value(true));
    }

    @Test
    void testEnterPin() throws Exception {
        // Insert card first
        mockMvc.perform(post("/api/atm/insert-card"));

        mockMvc.perform(post("/api/atm/enter-pin")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"pin\": \"1234\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.currentState").value("TransactionState"))
                .andExpect(jsonPath("$.success").value(true));
    }
}
