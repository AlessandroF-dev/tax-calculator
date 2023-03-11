package br.com.taxApi.taxcalculator.service;

import br.com.taxApi.taxcalculator.dto.IncomeTaxDTO;
import br.com.taxApi.taxcalculator.dto.WorkerAdmDTO;
import br.com.taxApi.taxcalculator.dto.WorkerDTO;
import br.com.taxApi.taxcalculator.model.IncomeTax;
import br.com.taxApi.taxcalculator.model.Worker;
import br.com.taxApi.taxcalculator.repository.IncomeTaxRepository;
import br.com.taxApi.taxcalculator.repository.WorkerAdmRepository;
import br.com.taxApi.taxcalculator.repository.WorkerRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.regex.Pattern;

@Service
public class WorkerServiceImpl implements WorkerService {

    private static final String PASSWORD_REGEX = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,16}$";

    private static final Pattern PASSWORD_PATTERN = Pattern.compile(PASSWORD_REGEX);

    @Autowired
    private ModelMapper mapper;

    @Autowired
    private WorkerRepository workerRepository;

    @Autowired
    private WorkerAdmRepository workerAdmRepository;

    @Autowired
    private IncomeTaxRepository taxRepository;

    @Override
    public boolean validPassword(Worker worker) {
        if (PASSWORD_PATTERN.matcher(worker.getPassword()).matches()) {
            System.out.print("The Password " + worker.getPassword() + " is valid");
            return true;
        } else {
            System.out.print("The Password " + worker.getPassword() + " isn't valid");
            return false;
        }
    }

    @Override
    public Optional<WorkerDTO> create(WorkerDTO request) {
        Worker worker = mapper.map(request, Worker.class);
        worker.setActive(true);
        WorkerDTO response = mapper.map(workerRepository.save(worker), WorkerDTO.class);

        if (validPassword(worker)) {
            return Optional.of(response);
        }
        return Optional.empty();
    }

    @Override
    public Optional<IncomeTaxDTO> taxCalculator(Long id) {
        IncomeTax incomeTax = new IncomeTax();
        IncomeTaxDTO incomeTaxDTO = mapper.map(incomeTax, IncomeTaxDTO.class);

        Optional<Worker> worker = workerRepository.findById(id);

        if (worker.isPresent()) {
            double salary = worker.get().getSalary();
            if (salary <= 1903.98) {
                incomeTaxDTO.setMessage("Olá " + worker.get().getName() + ", pelo sálario informado de R$" + worker.get().getSalary() + " você está isento de pagar o imposdo de renda.");
                mapper.map(incomeTaxDTO, taxRepository.saveAndFlush(incomeTax));
                return Optional.of(incomeTaxDTO);
            } else if (salary >= 1903.99 && salary <= 2826.65) {
                incomeTaxDTO.setTax(0.075);
                double taxValue = worker.get().getSalary() * incomeTaxDTO.getTax();
                incomeTaxDTO.setMessage("Olá " + worker.get().getName()
                        + ", o valor do imposto de renda a partir do sálario informado de R$" + worker.get().getSalary() + " é: R$" + taxValue);
                mapper.map(incomeTaxDTO, taxRepository.saveAndFlush(incomeTax));
                return Optional.of(incomeTaxDTO);
            } else if (salary >= 2826.66 && salary <= 3751.05) {
                incomeTaxDTO.setTax(0.15);
                double taxValue = worker.get().getSalary() * incomeTaxDTO.getTax();
                incomeTaxDTO.setMessage("Olá " + worker.get().getName()
                        + ", o valor do imposto de renda a partir do sálario informado de R$" + worker.get().getSalary() + " é: R$" + taxValue);
                mapper.map(incomeTaxDTO, taxRepository.saveAndFlush(incomeTax));
                return Optional.of(incomeTaxDTO);
            } else if (salary >= 3751.06 && salary <= 4664.68) {
                incomeTaxDTO.setTax(0.225);
                double taxValue = worker.get().getSalary() * incomeTaxDTO.getTax();
                incomeTaxDTO.setMessage("Olá " + worker.get().getName()
                        + ", o valor do imposto de renda a partir do sálario informado de R$" + worker.get().getSalary() + " é: R$" + taxValue);
                mapper.map(incomeTaxDTO, taxRepository.saveAndFlush(incomeTax));
                return Optional.of(incomeTaxDTO);
            } else {
                incomeTaxDTO.setTax(0.275);
                double taxValue = worker.get().getSalary() * incomeTaxDTO.getTax();
                incomeTaxDTO.setMessage("Olá " + worker.get().getName()
                        + ", o valor do imposto de renda a partir do sálario informado de R$" + worker.get().getSalary() + " é: R$" + taxValue);
                mapper.map(incomeTaxDTO, taxRepository.saveAndFlush(incomeTax));
                return Optional.of(incomeTaxDTO);
            }
        }
        return Optional.empty();
    }

    @Override
    public void encryptWorker(Worker worker) {
        String generatedSalt = BCrypt.gensalt();
        System.out.println("SAL GERADO --> "+generatedSalt);
        String encryptedPassword = BCrypt.hashpw(worker.getPassword(), generatedSalt);

        worker.setPassword(encryptedPassword);
        workerRepository.saveAndFlush(worker);
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
}
