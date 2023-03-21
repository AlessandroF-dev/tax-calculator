package br.com.taxcalculator.taxcalculator;

import br.com.taxApi.taxcalculator.dto.WorkerDTO;
import br.com.taxApi.taxcalculator.repository.WorkerRepository;
import br.com.taxApi.taxcalculator.service.WorkerService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
class TaxIncomeTaxApplicationTests {

    @Autowired
    private WorkerService service;

    @Autowired
    private WorkerRepository repository;

    @Test
    void contextLoads() {
    }
//
//	@BeforeEach
//	public void setUp (){
//		Worker worker = new Worker();
//		worker.setName("Alessandro");
//		worker.setPassword("Alessandro123**");
//		worker.setEmail("alessandro@teste.com");
//		worker.setOffice("Dev");
//		worker.setSalary(8000.00);
//		worker.setAge(23);
//		worker.setActive(true);
//	}

    @Test
    public void shouldCreateWorker() {
        WorkerDTO request = new WorkerDTO();
        request.setName("Alessandro");
        request.setEmail("alessandro@teste.com");
        request.setPassword("Alessandro123**");
        request.setOffice("Dev");
        request.setSalary(8000.00);
        request.setAge(23);
        request.setActive(true);
        Optional<WorkerDTO> response = service.create(request);
        Assertions.assertNotNull(response);

        assertEquals(request.getName(), response.get().getName());
        assertEquals(request.getEmail(), response.get().getEmail());
        assertEquals(request.getPassword(), response.get().getPassword());
        assertEquals(request.getOffice(), response.get().getOffice());
        assertEquals(request.getSalary(), response.get().getSalary());
        assertEquals(request.getAge(), response.get().getAge());
        assertTrue(response.get().isActive());
    }
}
