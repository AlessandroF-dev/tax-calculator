package br.com.taxApi.taxcalculator;

import br.com.taxApi.taxcalculator.dto.IncomeTaxDTO;
import br.com.taxApi.taxcalculator.dto.WorkerAdmDTO;
import br.com.taxApi.taxcalculator.dto.WorkerDTO;
import br.com.taxApi.taxcalculator.model.Tax;
import br.com.taxApi.taxcalculator.repository.TaxRepository;
import br.com.taxApi.taxcalculator.repository.WorkerRepository;
import br.com.taxApi.taxcalculator.service.WorkerService;
import br.com.taxApi.taxcalculator.utils.SecurityUtils;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCrypt;

import java.util.List;
import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class TaxIncomeTaxApplicationTests {

    @Autowired
    private WorkerService service;
    @Autowired
    private WorkerRepository repository;
    @Autowired
    private TaxRepository taxRepository;

    private WorkerDTO request;
    private WorkerAdmDTO admRequest;

    @BeforeEach
    public void setUp() {
        request = new WorkerDTO();
        request.setId(1L);
        request.setName("Alessandro");
        request.setPassword("Alessandro123**");
        request.setEmail("alessandro@teste.com");
        request.setOffice("Dev");
        request.setSalary(8000.00);
        request.setAge(23);
        request.setActive(true);

        admRequest = new WorkerAdmDTO();
        admRequest.setPassword(request.getPassword());
        admRequest.setEmail(request.getEmail());
    }


    @Test
    @DisplayName("Should create worker")
    public void shouldCreateWorker() {
        Optional<WorkerDTO> response = service.create(request);

        Assertions.assertNotNull(response.get());

        assertEquals(request.getName(), response.get().getName());
        assertEquals(request.getEmail(), response.get().getEmail());
        assertEquals(request.getOffice(), response.get().getOffice());
        assertEquals(request.getSalary(), response.get().getSalary());
        assertEquals(request.getAge(), response.get().getAge());
        assertTrue(response.get().isActive());
    }

    @Test
    @DisplayName("AdmDTO is empty because password is not equals comparing the registered")
    public void shouldReturnTrueIfIsEmpty() {
        admRequest.setPassword("InvalidPassword");
        assertTrue(service.taxCalculator(admRequest).isEmpty());
    }

    @Test
    @DisplayName("Should return true if admDTO is present")
    public void shouldReturnTrueIfIsPresent() {
        assertTrue(service.taxCalculator(admRequest).isPresent());
    }

    @Test
    @DisplayName("Should return true and inactivate worker if is present")
    public void shouldInativateWorker() {
        boolean isInactive = service.delete(request.getId());
        assertTrue(isInactive);
    }

    @Test
    @DisplayName("Should return false if worker is not present")
    public void shouldReturnFalseBecauseWorkerIsNotPresent() {
        assertFalse(service.delete(3L));
    }

    @Test
    @DisplayName("Should return false if worker is not present")
    public void shouldReturnAllRecords() {
        List<WorkerDTO> workerDTOS = service.getAll();

        assertNotNull(workerDTOS);
        assertThat(workerDTOS.size(), Matchers.greaterThan(0));
    }

    @Test
    @DisplayName("Should return true if query method taxCalculator is correct")
    public void shouldReturnTrueIfQueryTaxCalculatorIsCorrect() {
        Tax tax = taxRepository.findTax(request.getSalary());
        Optional<IncomeTaxDTO> incomeTaxDTO = service.taxCalculator(admRequest);
        String taxFormattedToString = String.valueOf(tax.getTax() * 100).substring(0, 4);
        assertEquals(taxFormattedToString, incomeTaxDTO.get().getTax().substring(0, 4)); //27.5

//        double taxFormattedToDouble = Double.parseDouble(incomeTaxDTO.get().getTax().substring(0, 4));
//        assertEquals(taxFormattedToDouble / 100, tax.getTax()); //0.275
//        assertFalse(SecurityUtils.mountMessage(request.getName(), request.getSalary(), request.getSalary() * tax.getTax(), ).isEmpty());
//        assertEquals(String.valueOf(taxFormattedToDouble).substring(0,4), taxFormattedToString.substring(0,4)); //27.5
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
