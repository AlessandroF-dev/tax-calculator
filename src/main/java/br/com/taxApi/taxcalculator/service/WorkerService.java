package br.com.taxApi.taxcalculator.service;

import br.com.taxApi.taxcalculator.dto.IncomeTaxDTO;
import br.com.taxApi.taxcalculator.dto.WorkerAdmDTO;
import br.com.taxApi.taxcalculator.dto.WorkerDTO;

import java.util.List;
import java.util.Optional;

public interface WorkerService {

    Optional<WorkerDTO> create(WorkerDTO workerDTO);

    Optional<IncomeTaxDTO> taxCalculator(WorkerAdmDTO admDTO);

    List<WorkerDTO> getAll();

    boolean delete(Long id);

    String calculatesTotalCollected();
}
