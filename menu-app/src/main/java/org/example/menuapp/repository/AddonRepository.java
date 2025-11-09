package org.example.menuapp.repository;

import org.example.menuapp.entity.Addon;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AddonRepository extends JpaRepository<Addon, Long> {
}
