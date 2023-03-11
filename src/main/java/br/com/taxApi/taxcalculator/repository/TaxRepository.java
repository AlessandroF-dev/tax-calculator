package br.com.taxApi.taxcalculator.repository;

import br.com.taxApi.taxcalculator.model.Tax;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface TaxRepository extends JpaRepository<Tax, Long> {


    //salary >= começo E salario <= fim
    //começo <= salary

    @Query(value = "SELECT * FROM tax WHERE calculator_tier_begin <= :salary AND calculator_tier_end >= :salary", nativeQuery = true)
    Tax findTax(double salary);
}
//