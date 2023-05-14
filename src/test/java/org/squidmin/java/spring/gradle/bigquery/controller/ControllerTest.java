package org.squidmin.java.spring.gradle.bigquery.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.squidmin.java.spring.gradle.bigquery.IntegrationTest;
import org.squidmin.java.spring.gradle.bigquery.config.BigQueryConfig;
import org.squidmin.java.spring.gradle.bigquery.dto.ExampleRequest;
import org.squidmin.java.spring.gradle.bigquery.dto.ExampleResponse;
import org.squidmin.java.spring.gradle.bigquery.fixture.BigQueryIntegrationTestFixture;
import org.squidmin.java.spring.gradle.bigquery.logger.Logger;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {BigQueryConfig.class})
@WebAppConfiguration
public class ControllerTest extends IntegrationTest {

    @Autowired
    private WebApplicationContext context;

    private MockMvc mockMvc;

    private final ObjectMapper mapper = new ObjectMapper();

    @BeforeEach
    public void beforeEach() {
        mockMvc = MockMvcBuilders.webAppContextSetup(context).build();
    }

    @Test
    public void query_givenClientRequest_whenCalled_thenReturnOkResponse() throws Exception {
        ExampleResponse response = mapper.readValue(callQueryApi(BigQueryIntegrationTestFixture.validRequest()).getContentAsString(), ExampleResponse.class);
        Logger.log("Response:", Logger.LogType.INFO);
        Logger.log(mapper.writeValueAsString(response), Logger.LogType.INFO);
    }

    private MockHttpServletResponse callQueryApi(ExampleRequest request) throws Exception {
        return mockMvc.perform(
            MockMvcRequestBuilders
                .get("/bigquery/query")
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(mapper.writeValueAsString(request))
        ).andReturn().getResponse();
    }

}
