package br.com.taxApi.taxcalculator.repository;

import br.com.taxApi.taxcalculator.model.TaxCollected;
import br.com.taxApi.taxcalculator.model.Worker;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TaxMeterRepository extends JpaRepository<TaxCollected, Long> {

    boolean existsByUserEmail(String userEmail);
}