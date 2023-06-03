package br.com.taxApi.taxcalculator.model;

import lombok.Data;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.Date;

@Entity
@Data
public class TaxCollected {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Column
    private String userEmail;
    @Column
    private double amountRaised;
}
