package br.com.taxApi.taxcalculator;

import br.com.taxApi.taxcalculator.dto.WorkerDTO;
import br.com.taxApi.taxcalculator.model.Tax;
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
    @DisplayName("Should return true if query method taxCalculator is correct")
    public void shouldReturnTrueIfQueryTaxCalculatorIsCorrect() {
        Tax tax = taxRepository.findTax(request.getSalary());
        double incomeTax = request.getSalary() * tax.getTax();
        assertEquals(incomeTax, request.getSalary() * 0.275);
        assertNotNull(SecurityUtils.mountMessage(request.getName(), request.getSalary(), incomeTax));
    }

    @Test
    @DisplayName("The password should be within the requirements")
    public void shouldReturnFalseBecausePasswordIsNotWhitinTheRequirements() {
        request.setPassword("WeakPassword");
        assertFalse(SecurityUtils.validPassword(request.getPassword()));
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

        assertNotNull(response);
        assertTrue(BCrypt.checkpw(response.get().getPassword(), request.getPassword()));
    }
}
