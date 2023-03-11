package br.com.taxApi.taxcalculator.controller;

import br.com.taxApi.taxcalculator.dto.IncomeTaxDTO;
import br.com.taxApi.taxcalculator.dto.WorkerAdmDTO;
import br.com.taxApi.taxcalculator.dto.WorkerDTO;
import br.com.taxApi.taxcalculator.model.WorkerAdm;
import br.com.taxApi.taxcalculator.service.WorkerServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/tax-calculator")
public class WorkerController {

    @Autowired
    private WorkerServiceImpl service;

    @PostMapping
    public ResponseEntity<WorkerDTO> create(@RequestBody WorkerDTO request) {
        Optional<WorkerDTO> response = service.create(request);
        if (response.isPresent()) {
            return new ResponseEntity<>(response.get(), HttpStatus.CREATED);
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }

    @GetMapping("/login")
    public ResponseEntity<Boolean> loginValidation(@RequestBody WorkerAdmDTO workerAdmDTO) {
        if (service.autentication(workerAdmDTO)) {
            return ResponseEntity.ok(service.autentication(workerAdmDTO));
        }
        return new ResponseEntity<>(service.autentication(workerAdmDTO), HttpStatus.BAD_REQUEST);
    }

    @GetMapping("/{id}")
    public ResponseEntity<IncomeTaxDTO> taxCalculator(@PathVariable("id") Long id) {
        Optional<IncomeTaxDTO> response = service.taxCalculator(id);
        if (response.isPresent()) {
            return ResponseEntity.ok(response.get());
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Boolean> delete(@PathVariable("id") Long id) {
        boolean response = service.delete(id);
        return ResponseEntity.ok(response);
    }
}
