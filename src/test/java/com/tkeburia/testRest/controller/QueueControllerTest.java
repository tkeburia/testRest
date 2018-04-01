package com.tkeburia.testRest.controller;

import com.google.common.collect.ImmutableMap;
import com.tkeburia.testRest.queues.producer.ProducerService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.Mockito.verify;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@TestPropertySource(properties = {"schema.file.directory=./src/test/resources", "activemq.connections.enabled=true"},  locations = "classpath:test.properties")
public class QueueControllerTest {

    private MockMvc testServer;

    @Mock
    ProducerService producerService;

    @Before
    public void setup() {
        testServer = MockMvcBuilders.standaloneSetup(new QueueController(producerService)).build();
    }

    @Test
    public void shouldInvokeProducerServiceWithCorrectArguments() throws Exception {
        testServer
                .perform(
                        post("/test-rest/queues?brokerName=testQueue")
                        .content("{ \"key\" : \"value\"}")
                        .contentType(APPLICATION_JSON)
                )
                .andExpect(status().isOk());

        verify(producerService).sendToQueue("testQueue", ImmutableMap.of("key", "value"));
    }

}