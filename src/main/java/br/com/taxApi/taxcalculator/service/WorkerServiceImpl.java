package br.com.taxApi.taxcalculator.service;

import br.com.taxApi.taxcalculator.dto.UtiliterCalculator;
import br.com.taxApi.taxcalculator.dto.WorkerDTO;
import br.com.taxApi.taxcalculator.model.Worker;
import br.com.taxApi.taxcalculator.repository.WorkerRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
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
    public Optional<UtiliterCalculator> taxCalculator(Long id) {
        UtiliterCalculator utiliterTax = new UtiliterCalculator();
        double tax;
        Optional<Worker> worker = repository.findById(id);
        if (worker.isPresent()) {
            double salary = worker.get().getSalary();
            if (salary <= 1903.98) {
                utiliterTax.setMessage("Olá " + worker.get().getName() + " pelo sálario informado você está isento de pagar o imposdo de renda.");
                return Optional.of(utiliterTax);
            } else if (salary >= 1903.99 && salary <= 2826.65) {
                tax = (salary * 0.075);
                utiliterTax.setTax(tax);
                utiliterTax.setMessage("Olá " + worker.get().getName() + " o valor do imposto de renda a ser pago é: " + tax);
                return Optional.of(utiliterTax);
            } else if (salary >= 2826.66 && salary <= 3751.05) {
                tax = (salary * 0.15);
                utiliterTax.setTax(tax);
                utiliterTax.setMessage("Olá " + worker.get().getName() + " o valor do imposto de renda a ser pago é: " + tax);
                return Optional.of(utiliterTax);
            } else if (salary >= 3751.06 && salary <= 4664.68) {
                tax = (salary * 0.225);
                utiliterTax.setTax(tax);
                utiliterTax.setMessage("Olá " + worker.get().getName() + " o valor do imposto de renda a ser pago é: " + tax);
                return Optional.of(utiliterTax);
            } else {
                tax = (salary * 0.225);
                utiliterTax.setTax(tax);
                utiliterTax.setMessage("Olá " + worker.get().getName() + " o valor do imposto de renda a ser pago é: " + tax);
                return Optional.of(utiliterTax);
            }
        }
        return Optional.empty();
    }

    @Override
    public boolean delete(Long id) {
        Optional<Worker> worker = repository.findById(id);
        if (worker.isPresent()) {
            repository.delete(worker.get());
        }
        return false;
    }
}
