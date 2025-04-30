package org.example.menuapp.repository;

import org.example.menuapp.entity.OrderAddon;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderAddonRepository extends JpaRepository<OrderAddon, Long> {
}
