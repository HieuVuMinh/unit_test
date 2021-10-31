package com.example.bdscustomer.service;

import com.example.bdscustomer.model.Customer;
import com.example.bdscustomer.repository.ICustomerRepository;
import com.google.common.base.Verify;
import org.apache.http.protocol.HTTP;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.event.annotation.BeforeTestClass;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
class CustomerServiceTest {

    @Mock
    private ICustomerRepository customerRepository;

    @Autowired
    private ICustomerService customerService;


    public static List<Customer> customers = new ArrayList<>();

    public static Customer customer = new Customer(1L,"Hieu", "0123423512", "hieu@gmail.com");;

    @BeforeEach
    void setup(){
        customerService = new CustomerService(customerRepository);
    }

    @Test
    void list_customer_should_be_not_null() {
        //given
            customerService.createCustomer(customer);
        //when
            customers = customerService.findAll();
        //then
        assertThat(customers).isNotNull();
    }

    @Test
    void can_get_all_customer() {
        customerService.findAll();
        verify(customerRepository).findAll();
    }

    @Test
    void can_create_customer() {
        customerService.createCustomer(customer);

        ArgumentCaptor<Customer> customerArgumentCaptor = ArgumentCaptor.forClass(Customer.class);
        verify(customerRepository).save(customerArgumentCaptor.capture());

        Customer customerCaptor = customerArgumentCaptor.getValue();

        assertThat(customerCaptor).isEqualTo(customer);
    }

}
