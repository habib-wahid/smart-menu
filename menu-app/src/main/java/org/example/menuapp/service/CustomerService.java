package org.example.menuapp.service;

import lombok.RequiredArgsConstructor;
import org.example.menuapp.config.AppConstants;
import org.example.menuapp.dto.request.CustomerCreateRequest;
import org.example.menuapp.entity.Customer;
import org.example.menuapp.repository.CustomerRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CustomerService {

    private final CustomerRepository customerRepository;

    @Transactional
    public Customer saveCustomer(CustomerCreateRequest customerCreateRequest) {
        return customerRepository.findByPhone(customerCreateRequest.phone())
                .orElseGet(() -> {
                    Customer customer = new Customer();
                    customer.setUsername(generateUsername(customerCreateRequest.phone()));
                    customer.setPhone(customerCreateRequest.phone());
                    customer.setFirstName(customerCreateRequest.firstName());
                    customer.setLastName(customerCreateRequest.lastName());
                    return customerRepository.save(customer);
                });
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
