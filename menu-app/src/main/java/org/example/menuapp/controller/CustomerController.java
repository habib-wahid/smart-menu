package org.example.menuapp.controller;

import lombok.RequiredArgsConstructor;
import org.example.menuapp.dto.request.CustomerCreateRequest;
import org.example.menuapp.dto.response.ApiResponse;
import org.example.menuapp.entity.Customer;
import org.example.menuapp.service.CustomerService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequiredArgsConstructor
public class CustomerController {

    private final CustomerService customerService;

    @PostMapping("/customer")
    public ApiResponse<Customer> createCustomer(@RequestBody CustomerCreateRequest customerCreateRequest){
        return ApiResponse.success("Customer Created Successfully", customerService.saveCustomer(customerCreateRequest));
    }

    @GetMapping("/customer/{id}")
    public ApiResponse<Customer> getCustomerById(@PathVariable Long id){
        return ApiResponse.success("Customer fetched Successfully", customerService.getCustomerById(id));
    }

    @GetMapping("/customer/phone/{phone}")
    public ApiResponse<Customer> getAllCustomerByPhone(@PathVariable String phone){
        Optional<Customer> customer = customerService.getCustomerByPhone(phone);
        return customer.map(value -> ApiResponse.success(String.format("Found Customer with Phone Number: %s", phone), value))
                .orElseGet(() -> ApiResponse.success("No Customer Found with Phone Number: " + phone));
    }
}
