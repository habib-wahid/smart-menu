package org.example.menuapp.controller;

import lombok.RequiredArgsConstructor;
import org.example.menuapp.dto.request.CustomerCreateRequest;
import org.example.menuapp.dto.response.ApiResponse;
import org.example.menuapp.entity.Customer;
import org.example.menuapp.service.CustomerService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class CustomerController {

    private final CustomerService customerService;

    @PostMapping("/customer")
    public ApiResponse<Customer> createCustomer(@RequestBody CustomerCreateRequest customerCreateRequest){
        return ApiResponse.success("Customer Created Successfully", customerService.saveCustomer(customerCreateRequest));
    }
}
