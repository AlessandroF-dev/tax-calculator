package br.com.taxApi.taxcalculator.service;

import br.com.taxApi.taxcalculator.dto.IncomeTaxDTO;
import br.com.taxApi.taxcalculator.dto.WorkerDTO;
import br.com.taxApi.taxcalculator.model.IncomeTax;
import br.com.taxApi.taxcalculator.model.Worker;
import br.com.taxApi.taxcalculator.repository.WorkerRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class WorkerServiceImpl implements WorkerService {

    @Autowired
    private ModelMapper mapper;

    @Autowired
    private WorkerRepository repository;

    @Override
    public Optional<WorkerDTO> create(WorkerDTO request) {
        Worker worker = mapper.map(request, Worker.class);
        WorkerDTO response = mapper.map(repository.saveAndFlush(worker), WorkerDTO.class);
        return Optional.of(response);
    }

    @Override
    public Optional<IncomeTaxDTO> taxCalculator(Long id) {
        IncomeTax incomeTax = new IncomeTax();
        IncomeTaxDTO incomeTaxDTO = mapper.map(incomeTax, IncomeTaxDTO.class);

        Optional<Worker> worker = repository.findById(id);
        if (worker.isPresent()) {
            double salary = worker.get().getSalary();
            if (salary <= 1903.98) {
                incomeTaxDTO.setMessage("Olá " + worker.get().getName() + ", pelo sálario informado de R$" + worker.get().getSalary() + " você está isento de pagar o imposdo de renda.");
                return Optional.of(incomeTaxDTO);
            } else if (salary >= 1903.99 && salary <= 2826.65) {
                incomeTaxDTO.setTax(0.075);
                double taxValue = worker.get().getSalary() * incomeTaxDTO.getTax();
                incomeTaxDTO.setMessage("Olá " + worker.get().getName()
                        + ", o valor do imposto de renda a partir do sálario informado de R$" + worker.get().getSalary() + " é: R$" + taxValue);
                return Optional.of(incomeTaxDTO);
            } else if (salary >= 2826.66 && salary <= 3751.05) {
                incomeTaxDTO.setTax(0.15);
                double taxValue = worker.get().getSalary() * incomeTaxDTO.getTax();
                incomeTaxDTO.setMessage("Olá " + worker.get().getName()
                        + ", o valor do imposto de renda a partir do sálario informado de R$" + worker.get().getSalary() + " é: R$" + taxValue);
                return Optional.of(incomeTaxDTO);
            } else if (salary >= 3751.06 && salary <= 4664.68) {
                incomeTaxDTO.setTax(0.225);
                double taxValue = worker.get().getSalary() * incomeTaxDTO.getTax();
                incomeTaxDTO.setMessage("Olá " + worker.get().getName()
                        + ", o valor do imposto de renda a partir do sálario informado de R$" + worker.get().getSalary() + " é: R$" + taxValue);
                return Optional.of(incomeTaxDTO);
            } else {
                incomeTaxDTO.setTax(0.275);
                double taxValue = worker.get().getSalary() * incomeTaxDTO.getTax();
                incomeTaxDTO.setMessage("Olá " + worker.get().getName()
                        + ", o valor do imposto de renda a partir do sálario informado de R$" + worker.get().getSalary() + " é: R$" + taxValue);
                return Optional.of(incomeTaxDTO);
            }
        }
        return Optional.empty();
    }

    @Override
    public void encryptWorker(Worker worker) {
        String generatedSalt = BCrypt.gensalt();

        String encryptedPassword = BCrypt.hashpw(worker.getPassword(), generatedSalt);

        worker.setPassword(encryptedPassword);
        repository.saveAndFlush(worker);
    }

    @Override
    public boolean autentication(WorkerDTO workerDTO) {
        String password = workerDTO.getPassword();

        Worker worker = repository.findByLogin(workerDTO.getLogin());
        String encryptedPassword = worker.getPassword();

        boolean autenticator = BCrypt.checkpw(password, encryptedPassword);

        return autenticator;
    }

    @Override
    public boolean delete(Long id) {
        Optional<Worker> worker = repository.findById(id);
        if (worker.isPresent()) {
            worker.get().setActive(false);
            repository.save(worker.get());
            return true;
        }
        return false;
    }
}
