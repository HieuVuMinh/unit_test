package com.example.bdscustomer.service;

import com.example.bdscustomer.model.Customer;

import java.util.List;

public interface ICustomerService {

    Customer createCustomer (Customer customer);

    Customer findByCustomerId (Long id);

    List<Customer> findAll ();

}
