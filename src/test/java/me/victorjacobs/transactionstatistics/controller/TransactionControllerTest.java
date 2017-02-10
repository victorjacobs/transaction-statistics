package me.victorjacobs.transactionstatistics.controller;

import me.victorjacobs.transactionstatistics.Application;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;

import java.nio.charset.Charset;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

/**
 * Test the TransactionController. Only tests here are end-to-end, actual correctness are tested in the StatisticsStoreTest
 * Created by Victor on 10/02/2017.
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
@WebAppConfiguration
public class TransactionControllerTest {
    @Autowired
    private WebApplicationContext webApplicationContext;
    private MockMvc mockMvc;
    private MediaType contentType = new MediaType(MediaType.APPLICATION_JSON.getType(),
            MediaType.APPLICATION_JSON.getSubtype(),
            Charset.forName("utf8"));

    @Before
    public void setup() {
        mockMvc = webAppContextSetup(webApplicationContext).build();
    }

    @Test
    public void postTransaction() throws Exception {
        mockMvc.perform(post("/transactions")
                .content("{\"timestamp\": 11111111, \"amount\": 12.1}")
                .contentType(contentType))
            .andExpect(status().isCreated());
    }

    @Test
    public void postTransaction_invalidJson() throws Exception {
        mockMvc.perform(post("/transactions")
                .content("foobar")
                .contentType(contentType))
                .andExpect(status().isBadRequest());
    }
}
