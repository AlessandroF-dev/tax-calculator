package br.com.taxApi.taxcalculator.repository;

import br.com.taxApi.taxcalculator.model.IncomeTax;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IncomeTaxRepository extends JpaRepository <IncomeTax, Long> {

}
