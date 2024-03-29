package br.com.taxApi.taxcalculator.service;

import br.com.taxApi.taxcalculator.dto.IncomeTaxDTO;
import br.com.taxApi.taxcalculator.dto.WorkerAdmDTO;
import br.com.taxApi.taxcalculator.dto.WorkerDTO;
import br.com.taxApi.taxcalculator.model.Tax;
import br.com.taxApi.taxcalculator.model.TaxCollected;
import br.com.taxApi.taxcalculator.model.Worker;
import br.com.taxApi.taxcalculator.repository.TaxMeterRepository;
import br.com.taxApi.taxcalculator.repository.TaxRepository;
import br.com.taxApi.taxcalculator.repository.WorkerRepository;
import br.com.taxApi.taxcalculator.utils.SecurityUtils;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;

import java.text.DecimalFormat;
import java.time.LocalDate;
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
    private TaxMeterRepository taxMeterRepository;
    @Autowired
    private TaxRepository taxRepository;
    @Autowired
    private CacheManager cacheManager;

    @Override
    public Optional<WorkerDTO> create(WorkerDTO request) {
        if (!repository.existsByEmail(request.getEmail())) {
            if (SecurityUtils.validPassword((request.getPassword()))) {
                request.setPassword(SecurityUtils.encryptPassword(request.getPassword()));
                Worker worker = mapper.map(request, Worker.class);
                WorkerDTO response = mapper.map(repository.save(worker), WorkerDTO.class);

                log.info("Successfully registered");

                return Optional.of(response);
            } else {
                log.error("Invalid password! Please check that the password matches the requirements");
            }
        }
        log.error("There is already a user with the registered email");
        return Optional.empty();
    }

    public Optional<IncomeTaxDTO> taxCalculator(WorkerAdmDTO admDTO) {
        IncomeTaxDTO incomeTaxDTO = new IncomeTaxDTO();
        log.info("Authenticating worker");
        if (authenticate(admDTO)) {
            Optional<Worker> worker = repository.findByEmail(admDTO.getEmail());
            log.info("Authenticated with sucess");
            if (worker.isPresent()) {
                log.info("Calculating tax...");
                Tax irrf = taxRepository.findTax(worker.get().getSalary());
                if (irrf.getTax() == 0.0) {
                    incomeTaxDTO.setMessage(SecurityUtils.mountMessageIsento(worker.get().getName(), worker.get().getSalary()));
                    incomeTaxDTO.setTax("ISENTO (A)");
                    return Optional.of(incomeTaxDTO);
                }
                double incomeTax = worker.get().getSalary() * irrf.getTax();
                incomeTaxDTO.setMessage(SecurityUtils.mountMessage(worker.get().getName(), worker.get().getSalary(), incomeTax, String.valueOf(irrf.getTax() * 100).substring(0, 5)));
                incomeTaxDTO.setTax(String.valueOf(irrf.getTax() * 100).substring(0, 5));

                if (admDTO.isWantToPay()) {
                    if (!taxMeterRepository.existsByUserEmail(admDTO.getEmail())) {
                        log.info("Creating a new record in taxMeter...");
                        TaxCollected taxCollected = new TaxCollected();
                        taxCollected.setUserEmail(admDTO.getEmail());
                        taxCollected.setAmountRaised(incomeTax);

                        taxMeterRepository.save(taxCollected);
                    } else {
                        log.info("Record already exist");
                    }
                }
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
    public String calculatesTotalCollected() {
        double value = 0;
        log.info("Searching total value of tax meter...");
        DecimalFormat formatter = new DecimalFormat();

        for (TaxCollected taxCollected : taxMeterRepository.findAll()) {
            value += taxCollected.getAmountRaised();
        }
        return "Valor total arrecadado pelo imposto de renda R$" + formatter.format(value) + " " + LocalDate.now();
    }

    @Scheduled(cron = "0 */2 * ? * *")
    private void invalidCache() {
        cacheManager.getCache("taxMeter").clear();
        log.info("Clearing cache taxMeter");
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