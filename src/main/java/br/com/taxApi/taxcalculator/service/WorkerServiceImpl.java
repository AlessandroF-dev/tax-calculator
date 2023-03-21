package br.com.taxApi.taxcalculator.service;

import br.com.taxApi.taxcalculator.dto.IncomeTaxDTO;
import br.com.taxApi.taxcalculator.dto.WorkerAdmDTO;
import br.com.taxApi.taxcalculator.dto.WorkerDTO;
import br.com.taxApi.taxcalculator.model.Tax;
import br.com.taxApi.taxcalculator.model.Worker;
import br.com.taxApi.taxcalculator.repository.TaxRepository;
import br.com.taxApi.taxcalculator.repository.WorkerRepository;
import br.com.taxApi.taxcalculator.utils.SecurityUtils;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
public class WorkerServiceImpl implements WorkerService {

    @Autowired
    private ModelMapper mapper;

    @Autowired
    private WorkerRepository repository;

    @Autowired
    private TaxRepository taxRepository;

    @Override
    public Optional<WorkerDTO> create(WorkerDTO request) {
        if (SecurityUtils.validPassword((request.getPassword()))) {
            request.setPassword(SecurityUtils.encryptPassword(request.getPassword()));
            Worker worker = mapper.map(request, Worker.class);
            WorkerDTO response = mapper.map(repository.save(worker), WorkerDTO.class);
            log.info("Successfully registered");

            return Optional.of(response);
        }
        log.error("Invalid password! Please check that the password matches the requirements");
        return Optional.empty();
    }

    public Optional<IncomeTaxDTO> taxCalculator(WorkerAdmDTO admDTO) {
        log.info("Authenticating worker");
        if (authenticate(admDTO)) {
            Optional<Worker> worker = repository.findByEmail(admDTO.getEmail());
            log.info("Authenticated with sucess");
            if (worker.isPresent()) {
                log.info("Calculating tax...");
                Tax irrf = taxRepository.findTax(worker.get().getSalary());
                double incomeTax = worker.get().getSalary() * irrf.getTax();
                IncomeTaxDTO incomeTaxDTO = new IncomeTaxDTO();
                incomeTaxDTO.setMessage(SecurityUtils.mountMessage(worker.get().getName(), worker.get().getSalary(), incomeTax));

                incomeTaxDTO.setTax((irrf.getTax() == 0) ? "ISENTO" : ((irrf.getTax() * 100) + "%"));
                return Optional.of(incomeTaxDTO);
            }
        }
        log.error("Incorrect login or password. Please check and try again");
        return Optional.empty();
    }

    @Override
    public boolean delete(Long id) {
        Optional<Worker> worker = repository.findById(id);
        if (worker.isPresent()) {
            worker.get().setActive(false);
            repository.save(worker.get());
            log.info("Deleting record");
            return true;
        }
        log.error("This record isn't exists");
        return false;
    }

    @Override
    public List<WorkerDTO> getAll() {
        log.info("Searching all...");
        return repository.findAll()
                .stream()
                .map(w -> mapper.map(w, WorkerDTO.class))
                .collect(Collectors.toList());
    }

    private boolean authenticate(WorkerAdmDTO admDTO) {
        Optional<Worker> worker = repository.findByEmail(admDTO.getEmail());
        return worker.filter(value -> BCrypt.checkpw(admDTO.getPassword(), value.getPassword())).isPresent();
    }
}