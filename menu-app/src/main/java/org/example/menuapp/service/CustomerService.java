package org.example.menuapp.service;

import lombok.RequiredArgsConstructor;
import org.example.menuapp.config.AppConstants;
import org.example.menuapp.dto.request.CustomerCreateRequest;
import org.example.menuapp.entity.Customer;
import org.example.menuapp.error.custom_exceptions.SmResourceNotFoundException;
import org.example.menuapp.error.messages.ExceptionMessages;
import org.example.menuapp.repository.CustomerRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CustomerService {

    private final CustomerRepository customerRepository;

    public Customer getCustomerById(Long id) {
        return customerRepository.findById(id)
                .orElseThrow(() -> new SmResourceNotFoundException(String.format(ExceptionMessages.RESOURCE_NOT_FOUND, "Customer", id)));
    }

    @Transactional
    public Customer saveCustomer(CustomerCreateRequest customerCreateRequest) {
        return getCustomerByPhone(customerCreateRequest.phone())
                .orElseGet(() -> {
                    Customer customer = new Customer();
                    customer.setUsername(generateUsername(customerCreateRequest.phone()));
                    customer.setPhone(customerCreateRequest.phone());
                    customer.setFirstName(customerCreateRequest.firstName());
                    customer.setLastName(customerCreateRequest.lastName());
                    return customerRepository.save(customer);
                });
    }

    public Optional<Customer> getCustomerByPhone(String phone) {
        return customerRepository.findByPhone(phone);
    }

    private String generateUsername(String phoneNumber) {
        String cleanPhone = phoneNumber.replaceAll("[^0-9]", "");
        int hash = cleanPhone.hashCode();

        StringBuilder username = new StringBuilder();
        username.append((char) ('a' + (Math.abs(hash) % 26)));

        for (int i = 1; i < 5; i++) {
            hash = hash * 31 + cleanPhone.charAt(i % cleanPhone.length());
            username.append(AppConstants.ALPHA_NUMERIC.charAt(Math.abs(hash) % AppConstants.ALPHA_NUMERIC.length()));
        }

        return username.toString();
    }

}
