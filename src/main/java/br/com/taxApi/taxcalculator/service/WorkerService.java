package br.com.taxApi.taxcalculator.service;

import br.com.taxApi.taxcalculator.dto.IncomeTaxDTO;
import br.com.taxApi.taxcalculator.dto.WorkerAdmDTO;
import br.com.taxApi.taxcalculator.dto.WorkerDTO;
import br.com.taxApi.taxcalculator.model.Worker;

import java.util.List;
import java.util.Optional;

public interface WorkerService {

    Optional<WorkerDTO> create(WorkerDTO workerDTO);

    Optional<IncomeTaxDTO> taxCalculator(Long id);

    boolean autentication(WorkerAdmDTO workerAdmDTO);

    boolean delete(Long id);

    List<WorkerDTO> getAll();
}
