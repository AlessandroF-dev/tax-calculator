package br.com.taxApi.taxcalculator.repository;

import br.com.taxApi.taxcalculator.model.Worker;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface WorkerRepository extends JpaRepository<Worker, Long> {

    Optional<Worker> findByEmail(String email);

    boolean existsByEmail(String email);
}
