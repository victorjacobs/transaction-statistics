package me.victorjacobs.transactionstatistics.controller;

import me.victorjacobs.transactionstatistics.model.Statistic;
import me.victorjacobs.transactionstatistics.service.StatisticsStore;
import org.junit.Before;
import org.junit.Test;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup;

/**
 * Test the StatisticsController. Only end-to-end test to see whether everything is wired up correctly. Correctness is
 * ensured in the StatisticsStoreTest
 * Created by Victor on 10/02/2017.
 */
public class StatisticsControllerTest {
    private MockMvc mockMvc;
    private StatisticsStore statisticsStore;

    @Before
    public void setup() {
        statisticsStore = mock(StatisticsStore.class);
        when(statisticsStore.getStatistic()).thenReturn(new Statistic());
        mockMvc = standaloneSetup(new StatisticsController(statisticsStore)).build();
    }

    @Test
    public void getStatistics() throws Exception {
        mockMvc.perform(get("/statistics"))
                .andExpect(status().isOk());
    }
}
