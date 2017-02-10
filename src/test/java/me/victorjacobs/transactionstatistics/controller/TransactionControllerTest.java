package me.victorjacobs.transactionstatistics.controller;

import me.victorjacobs.transactionstatistics.service.StatisticsStore;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.mock;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup;

/**
 * Test the TransactionController. Only tests here are end-to-end, actual correctness are tested in the StatisticsStoreTest
 * Created by Victor on 10/02/2017.
 */
public class TransactionControllerTest {
    private MockMvc mockMvc;
    private StatisticsStore statisticsStore;

    @Before
    public void setup() {
        statisticsStore = mock(StatisticsStore.class);
        mockMvc = standaloneSetup(new TransactionController(statisticsStore)).build();
    }

    @Test
    public void postTransaction() throws Exception {
        mockMvc.perform(post("/transactions")
                .content("{\"timestamp\": 11111111, \"amount\": 12.1}")
                .contentType(MediaType.parseMediaType("application/json;charset=UTF-8")))
            .andExpect(status().isCreated());
    }

    @Test
    public void postTransaction_invalidJson() throws Exception {
        mockMvc.perform(post("/transactions")
                .content("foobar")
                .contentType(MediaType.parseMediaType("application/json;charset=UTF-8")))
                .andExpect(status().isBadRequest());
    }
}
