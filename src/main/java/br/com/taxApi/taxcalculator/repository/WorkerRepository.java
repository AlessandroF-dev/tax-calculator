package br.com.taxApi.taxcalculator.repository;

import br.com.taxApi.taxcalculator.model.Worker;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WorkerRepository extends JpaRepository <Worker, Long> {

    Worker findByLogin (String login);

}
