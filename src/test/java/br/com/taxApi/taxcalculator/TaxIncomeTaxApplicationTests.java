package br.com.taxApi.taxcalculator;

import br.com.taxApi.taxcalculator.dto.WorkerAdmDTO;
import br.com.taxApi.taxcalculator.dto.WorkerDTO;
import br.com.taxApi.taxcalculator.model.Tax;
import br.com.taxApi.taxcalculator.model.Worker;
import br.com.taxApi.taxcalculator.repository.TaxRepository;
import br.com.taxApi.taxcalculator.repository.WorkerRepository;
import br.com.taxApi.taxcalculator.service.WorkerService;
import br.com.taxApi.taxcalculator.utils.SecurityUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCrypt;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class TaxIncomeTaxApplicationTests {

    @Autowired
    private WorkerService service;
    @Autowired
    WorkerRepository repository;
    @Autowired
    private TaxRepository taxRepository;

    WorkerDTO request;

    @BeforeEach
    public void setUpWorker() {
        request = new WorkerDTO();
        request.setId(1L);
        request.setName("Alessandro");
        request.setPassword("Alessandro123**");
        request.setEmail("alessandro@teste.com");
        request.setOffice("Dev");
        request.setSalary(8000.00);
        request.setAge(23);
        request.setActive(true);
    }

    @Test
    @DisplayName("Should create worker")
    public void shouldCreateWorker() {
        Optional<WorkerDTO> response = service.create(request);

        Assertions.assertNotNull(response);

        assertEquals(request.getName(), response.get().getName());
        assertEquals(request.getEmail(), response.get().getEmail());
        assertEquals(request.getOffice(), response.get().getOffice());
        assertEquals(request.getSalary(), response.get().getSalary());
        assertEquals(request.getAge(), response.get().getAge());

        assertNotNull(request.getPassword(), response.get().getPassword());
        assertTrue(response.get().isActive());
    }

    @Test
    @DisplayName("Adam DTO is empty because password is not equals did registered")
    public void shouldReturnTrueIfIsEmpty() {
        WorkerAdmDTO admDTO = new WorkerAdmDTO();

        admDTO.setPassword("PasswordNotEquals");
        admDTO.setEmail(request.getEmail());

        assertTrue(service.taxCalculator(admDTO).isEmpty());
    }

    @Test
    @DisplayName("Should return true if admDTO is present")
    public void shouldReturnTrueIfIsPresent() {
        WorkerAdmDTO admDTO = new WorkerAdmDTO();

        admDTO.setPassword(request.getPassword());
        admDTO.setEmail(request.getEmail());

        assertTrue(service.taxCalculator(admDTO).isPresent());
    }

    @Test
    @DisplayName("Should return true and inactivate worker if is present")
    public void shouldInativateWorker() {
        Optional<Worker> response = repository.findById(request.getId());
        service.delete(request.getId());

        assertNotNull(response);
        assertTrue(response.get().isActive());
    }

    @Test
    @DisplayName("Should return false if worker is not present")
    public void shouldReturnFalseBecauseWorkerIsNotPresent() {
        request.setId(3L);

        assertFalse(service.delete(request.getId()));
    }

    @Test
    @DisplayName("Should return false if worker is not present")
    public void shouldReturnAllRecords() {
        List<WorkerDTO> workerDTOS = service.getAll();

        assertNotNull(workerDTOS);
        assertTrue(workerDTOS.size() >= 1);
    }

    @Test
    @DisplayName("Should return true if query method taxCalculator is correct")
    public void shouldReturnTrueIfQueryTaxCalculatorIsCorrect() {
        Tax tax = taxRepository.findTax(request.getSalary());
        double incomeTax = request.getSalary() * tax.getTax();
        assertEquals(incomeTax, request.getSalary() * 0.275);
        assertFalse(SecurityUtils.mountMessage(request.getName(), request.getSalary(), incomeTax).isEmpty());
    }

    @Test
    @DisplayName("The password not within the requirements")
    public void shouldReturnFalseBecausePasswordIsNotWhitinTheRequirements() {
        request.setPassword("WeakPassword");
        Optional<WorkerDTO> response = service.create(request);

        assertFalse(SecurityUtils.validPassword(request.getPassword()));
        assertTrue(response.isEmpty());
    }

    @Test
    @DisplayName("The password is within the requirements")
    public void shouldReturnTrueBecausePasswordIsWithinTheRequirements() {
        request.setPassword("StrongPassword123#*");
        assertTrue(SecurityUtils.validPassword(request.getPassword()));
    }

    @Test
    @DisplayName("The password don't match")
    public void shouldReturnFalseIfPasswordDontMatchBecausePasswordIsEncrypted() {
        Optional<WorkerDTO> response = service.create(request);
        assertEquals(request.getPassword(), response.get().getPassword());
    }

    @Test
    @DisplayName("The password don't match with request password")
    public void shouldReturnFalseIfPasswordDontMatchRequestPassword() {
        Optional<WorkerDTO> response = service.create(request);
        response.get().setPassword("NoMatchPassword1**");

        assertNotNull(response);
        assertFalse(BCrypt.checkpw(response.get().getPassword(), request.getPassword()));
    }

    @Test
    @DisplayName("The password match")
    public void shouldReturnTrueIfPasswordMatchWithEncryptedPassword() {
        Optional<WorkerDTO> response = service.create(request);
        response.get().setPassword("Alessandro123**");

        assertNotNull(taxRepository.findTax(request.getSalary()));
        assertNotNull(response);
        assertTrue(BCrypt.checkpw(response.get().getPassword(), request.getPassword()));
    }

    @Test
    @DisplayName("The tax was successfully found in database")
    public void shouldReturnTrueIfDataInDataBaseIsPresentAndEquals() {
        Tax tax = new Tax();
        tax.setId(1L);
        tax.setTax(0.0);
        tax.setCalculatorTierBegin(0);
        tax.setCalculatorTierEnd(1903.98);

        assertNotNull(taxRepository.findTax(1000));

        assertEquals(tax.getTax(), taxRepository.findTax(1000).getTax());
        assertEquals(tax.getCalculatorTierBegin(), taxRepository.findTax(1000).getCalculatorTierBegin());
        assertEquals(tax.getCalculatorTierEnd(), taxRepository.findTax(1000).getCalculatorTierEnd());
        assertEquals(tax.getId(), taxRepository.findTax(1000).getId());
    }

    @Test
    @DisplayName("The tax was not found in database")
    public void shouldReturnTrueIfDataInDataBaseIsPresentButNotEquals() {
        Tax tax = new Tax();
        tax.setId(2L);
        tax.setTax(0.075);
        tax.setCalculatorTierBegin(1903.99);
        tax.setCalculatorTierEnd(2826.65);

        assertNotNull(taxRepository.findTax(1000));

        assertNotEquals(tax.getTax(), taxRepository.findTax(1000).getTax());
        assertNotEquals(tax.getCalculatorTierBegin(), taxRepository.findTax(1000).getCalculatorTierBegin());
        assertNotEquals(tax.getCalculatorTierEnd(), taxRepository.findTax(1000).getCalculatorTierEnd());
        assertNotEquals(tax.getId(), taxRepository.findTax(1000).getId());
    }
}
