package br.com.taxApi.taxcalculator.repository;

import br.com.taxApi.taxcalculator.model.TaxCollected;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TaxMeterRepository extends JpaRepository<TaxCollected, Long> {

}