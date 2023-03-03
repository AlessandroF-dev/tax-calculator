package br.com.taxApi.taxcalculator.service;

import br.com.taxApi.taxcalculator.dto.UtiliterCalculator;
import br.com.taxApi.taxcalculator.dto.WorkerDTO;

import java.util.Optional;

public interface WorkerService {

    Optional<WorkerDTO> create(WorkerDTO workerDTO);

    Optional<UtiliterCalculator> taxCalculator(Long id);

    boolean delete(Long id);
}
