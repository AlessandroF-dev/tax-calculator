package br.com.taxApi.taxcalculator.service;

import br.com.taxApi.taxcalculator.dto.IncomeTaxDTO;
import br.com.taxApi.taxcalculator.dto.WorkerDTO;
import br.com.taxApi.taxcalculator.model.Worker;

import java.util.Optional;

public interface WorkerService {

    boolean validPassword (Worker worker);

    Optional<WorkerDTO> create(WorkerDTO workerDTO);

    Optional<IncomeTaxDTO> taxCalculator(Long id);

    void encryptWorker(Worker worker);

    boolean autentication(WorkerDTO workerDTO);

    boolean delete(Long id);
}
