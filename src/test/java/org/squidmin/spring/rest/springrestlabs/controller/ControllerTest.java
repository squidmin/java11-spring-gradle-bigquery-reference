package org.squidmin.spring.rest.springrestlabs.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.squidmin.spring.rest.springrestlabs.IntegrationTest;
import org.squidmin.spring.rest.springrestlabs.config.BigQueryConfig;
import org.squidmin.spring.rest.springrestlabs.logger.Logger;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = { BigQueryConfig.class })
@WebAppConfiguration
public class ControllerTest extends IntegrationTest {

    @Autowired
    private WebApplicationContext context;

    private MockMvc mockMvc;

    @BeforeEach
    public void beforeEach() {
        mockMvc = MockMvcBuilders.webAppContextSetup(context).build();
    }

    @Test
    public void lookUpById_givenClientRequest_whenCalled_thenReturn200() throws Exception {
        String id = "asdf-1234";
        String response = mockMvc.perform(get(String.format("/find-by-id?id=%s", id)))
            .andExpect(status().isOk())
            .andReturn().getResponse().getContentAsString();
        Logger.log("Response:", Logger.LogType.INFO);
        Logger.log(response, Logger.LogType.INFO);
    }

}
