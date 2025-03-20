package org.mobilestoreapp.repository;

import org.mobilestoreapp.model.Mobile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MobileRepository extends JpaRepository<Mobile, Long> {

    List<Mobile> findByBrandContainingIgnoreCase(String brand);
    List<Mobile> findByModelContainingIgnoreCase(String model);
    List<Mobile> findByOsTypeContainingIgnoreCase(String osType);
}
