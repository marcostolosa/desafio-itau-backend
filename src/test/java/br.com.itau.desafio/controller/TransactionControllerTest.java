package br.com.itau.desafio.controller;

import br.com.itau.desafio.model.Transaction;
import br.com.itau.desafio.service.TransactionService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.OffsetDateTime;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.mockito.Mockito.*;

@WebMvcTest(TransactionController.class)
class TransactionControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TransactionService transactionService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void shouldReturnCreatedWhenTransactionIsValid() throws Exception {
        Transaction transaction = new Transaction(100.0, OffsetDateTime.now());
        when(transactionService.addTransaction(any())).thenReturn(true);

        mockMvc.perform(post("/api/transacao")
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(transaction)))
                .andExpect(status().isCreated());
    }

    @Test
    void shouldReturnUnprocessableEntityWhenTransactionInvalid() throws Exception {
        Transaction transaction = new Transaction(100.0, OffsetDateTime.now().plusMinutes(5));
        when(transactionService.addTransaction(any())).thenReturn(false);

        mockMvc.perform(post("/api/transacao")
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(transaction)))
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    void shouldReturnStatistics() throws Exception {
        mockMvc.perform(get("/api/estatistica"))
                .andExpect(status().isOk());
    }

    @Test
    void shouldDeleteTransactions() throws Exception {
        mockMvc.perform(delete("/api/transacao"))
                .andExpect(status().isOk());
    }
}
