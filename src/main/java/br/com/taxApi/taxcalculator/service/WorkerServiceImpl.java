package br.com.taxApi.taxcalculator.service;

import br.com.taxApi.taxcalculator.dto.IncomeTaxDTO;
import br.com.taxApi.taxcalculator.dto.WorkerAdmDTO;
import br.com.taxApi.taxcalculator.dto.WorkerDTO;
import br.com.taxApi.taxcalculator.model.Tax;
import br.com.taxApi.taxcalculator.model.Worker;
import br.com.taxApi.taxcalculator.repository.TaxRepository;
import br.com.taxApi.taxcalculator.repository.WorkerAdmRepository;
import br.com.taxApi.taxcalculator.repository.WorkerRepository;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Service
@Slf4j
public class WorkerServiceImpl implements WorkerService {

    private static final String PASSWORD_REGEX = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$*%^&+=])(?=\\S+$).{8,16}$";

    private static final Pattern PASSWORD_PATTERN = Pattern.compile(PASSWORD_REGEX);

    @Autowired
    private ModelMapper mapper;

    @Autowired
    private WorkerRepository workerRepository;

    @Autowired
    private WorkerAdmRepository workerAdmRepository;

    @Autowired
    private TaxRepository irrfRepository;

    @Override
    public Optional<WorkerDTO> create(WorkerDTO request) {
        if (validPassword(request.getPassword())) {
            request.setPassword(encryptPassword(request.getPassword()));

            Worker worker = mapper.map(request, Worker.class);
            WorkerDTO response = mapper.map(workerRepository.save(worker), WorkerDTO.class);
            return Optional.of(response);
        }
        return Optional.empty();
    }

    @Override
    public Optional<IncomeTaxDTO> taxCalculator(Long id) {
        Optional<Worker> worker = workerRepository.findById(id);
        if (worker.isPresent()) {
            Tax irrf = irrfRepository.findTax(worker.get().getSalary());//salary
            double incomeTax = worker.get().getSalary() * irrf.getTax(); //0.275

            IncomeTaxDTO incomeTaxDTO = new IncomeTaxDTO();
            incomeTaxDTO.setMessage(mountMessage(worker.get().getName(), worker.get().getSalary(), incomeTax));

            incomeTaxDTO.setTax((irrf.getTax() == 0) ? "ISENTO" : ((irrf.getTax() * 100) + "%"));
            return Optional.of(incomeTaxDTO);
        }
        return Optional.empty();
    }
    private String encryptPassword(String password) {
        String generatedSalt = BCrypt.gensalt();
        System.out.println("SAL GERADO --> " + generatedSalt);
        return BCrypt.hashpw(password, generatedSalt);
    }

    @Override
    public boolean autentication(WorkerAdmDTO workerAdmDTO) {
        String password = workerAdmDTO.getPassword();

        Worker worker = workerRepository.findByLogin(workerAdmDTO.getLogin());
        String encryptedPassword = worker.getPassword();

        boolean autenticator = BCrypt.checkpw(password, encryptedPassword);

        return autenticator;
    }

    @Override
    public boolean delete(Long id) {
        Optional<Worker> worker = workerRepository.findById(id);
        if (worker.isPresent()) {
            worker.get().setActive(false);
            workerRepository.save(worker.get());
            return true;
        }
        return false;
    }

    @Override
    public List<WorkerDTO> getAll() {
        return workerRepository.findAll()
                .stream()
                .map(w -> mapper.map(w, WorkerDTO.class))
                .collect(Collectors.toList());
    }

    private String mountMessage(String name, double salary, double taxValue) {
        return "Olá " + name
                + ", o valor do imposto de renda a partir do sálario informado de R$" + salary +
                " é: R$" + taxValue;
    }
    private boolean validPassword(String password) {
        if (PASSWORD_PATTERN.matcher(password).matches()) {
            log.info("Valid password");
            return true;
        } else {
            log.info("Invalid password");
            return false;
        }
    }
}