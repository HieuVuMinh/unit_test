package com.example.bdscustomer.controller;

import static org.mockito.Mockito.any;
import static org.mockito.Mockito.when;

import com.example.bdscustomer.model.Customer;
import com.example.bdscustomer.service.ICustomerService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.client.MappingBuilder;
import com.github.tomakehurst.wiremock.client.ResponseDefinitionBuilder;
import com.github.tomakehurst.wiremock.client.WireMock;

import java.util.ArrayList;

import javafx.beans.binding.When;
import org.aspectj.lang.annotation.After;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.event.annotation.AfterTestClass;
import org.springframework.test.context.event.annotation.BeforeTestClass;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;


@ExtendWith(MockitoExtension.class)
@SpringBootTest
class CustomerControllerTest {

    @Autowired
    private CustomerController customerController;

    @MockBean
    private ICustomerService iCustomerService;

    private static final int PORT = 8081;
    private static final String HOST = "localhost";
    private static WireMockServer server = new WireMockServer(PORT);


    @BeforeTestClass
    public void setup() {
        server.start();
        ResponseDefinitionBuilder mockResponse = new ResponseDefinitionBuilder();
        mockResponse.withStatus(200);

        WireMock.configureFor(HOST, PORT); //http://localhost:8081
        WireMock.stubFor(
                WireMock.get("/customers")
                        .willReturn(mockResponse)
        );
    }

    @Test
    void getAll() {
    }

    @AfterTestClass()
    public void shutdown() {
        if (null != server && server.isRunning()) {
            server.shutdownServer();
        }
    }

    @Test
    void testCreate() throws Exception {
        when(this.iCustomerService.findAll()).thenReturn(new ArrayList<Customer>());

        Customer customer = new Customer();
        customer.setCustomer_name("Customer name");
        customer.setCustomer_id(1L);
        customer.setCustomer_email("jane.doe@example.org");
        customer.setCustomer_phone("4105551212");
        String content = (new ObjectMapper()).writeValueAsString(customer);
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/customers")
                .contentType(MediaType.APPLICATION_JSON)
                .content(content);
        MockMvcBuilders.standaloneSetup(this.customerController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.content().string("[]"));
    }

    @Test
    void testFindById() throws Exception {
        Customer customer = new Customer();
        customer.setCustomer_name("Customer name");
        customer.setCustomer_id(1L);
        customer.setCustomer_email("jane.doe@example.org");
        customer.setCustomer_phone("4105551212");
        when(this.iCustomerService.findByCustomerId((Long) any())).thenReturn(customer);
        MockHttpServletRequestBuilder getResult = MockMvcRequestBuilders.get("/customers/*");
        MockHttpServletRequestBuilder requestBuilder = getResult.param("id", String.valueOf(1L));
        MockMvcBuilders.standaloneSetup(this.customerController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.content()
                        .string(
                                "{\"customer_id\":1,\"customer_name\":\"Customer name\",\"customer_phone\":\"4105551212\",\"customer_email\":\"jane"
                                        + ".doe@example.org\"}"));
    }

    @Test
    void testGetAll() throws Exception {
        when(this.iCustomerService.findAll()).thenReturn(new ArrayList<Customer>());
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/customers");
        MockMvcBuilders.standaloneSetup(this.customerController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.content().string("[]"));
    }

    @Test
    void testGetAll2() throws Exception {
        when(this.iCustomerService.findAll()).thenReturn(new ArrayList<Customer>());
        MockHttpServletRequestBuilder getResult = MockMvcRequestBuilders.get("/customers");
        getResult.contentType("Not all who wander are lost");
        MockMvcBuilders.standaloneSetup(this.customerController)
                .build()
                .perform(getResult)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.content().string("[]"));
    }
}
