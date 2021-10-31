package com.example.bdscustomer.controller;

import com.example.bdscustomer.model.Customer;
import com.example.bdscustomer.service.ICustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/customers")
public class CustomerController {

    @Autowired
    private ICustomerService customerService;

    @PostMapping
    public Customer create(@RequestBody Customer customer) {
        return customerService.createCustomer(customer);
    }

    @GetMapping
    public List<Customer> getAll() {
        return customerService.findAll();
    }

    @GetMapping("{id}")
    public Customer findById(@RequestParam("id") Long id){
        return customerService.findByCustomerId(id);
    }
}
