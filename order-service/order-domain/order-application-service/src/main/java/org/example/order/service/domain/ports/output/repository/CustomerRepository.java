package org.example.order.service.domain.ports.output.repository;

import org.example.order.service.domain.entiry.Customer;

import java.util.Optional;
import java.util.UUID;

public interface CustomerRepository {

    Optional<Customer> findCustomer(UUID customerId);
}
