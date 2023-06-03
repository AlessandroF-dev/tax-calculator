package br.com.taxApi.taxcalculator.controller;

import br.com.taxApi.taxcalculator.dto.IncomeTaxDTO;
import br.com.taxApi.taxcalculator.dto.WorkerAdmDTO;
import br.com.taxApi.taxcalculator.dto.WorkerDTO;
import br.com.taxApi.taxcalculator.service.WorkerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/tax-api")
public class WorkerController {

    @Autowired
    private WorkerService service;

    @Autowired
    private CacheManager cacheManager;

    @PostMapping
    public ResponseEntity<WorkerDTO> create(@RequestBody WorkerDTO request) {
        Optional<WorkerDTO> response = service.create(request);
        if (response.isPresent()) {
            return new ResponseEntity<>(response.get(), HttpStatus.CREATED);
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }

    @PostMapping("/calculator")
    @Cacheable("tax")
    public ResponseEntity<IncomeTaxDTO> taxCalculator(@RequestBody WorkerAdmDTO admDTO) {
        Optional<IncomeTaxDTO> response = service.taxCalculator(admDTO);
        if (response.isPresent()) {
            return ResponseEntity.ok(response.get());
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    @GetMapping("/taxMeter")
    @Cacheable("taxMeter")
    public ResponseEntity<String> impostometro() {
        String response = service.calculatesTotalCollected();
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<List<WorkerDTO>> getAll() {
        return ResponseEntity.ok(service.getAll());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Boolean> delete(@PathVariable("id") Long id) {
        boolean response = service.delete(id);
        return ResponseEntity.ok(response);
    }
}