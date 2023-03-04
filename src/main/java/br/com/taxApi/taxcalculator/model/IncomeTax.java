package br.com.taxApi.taxcalculator.model;

import lombok.Data;

import javax.persistence.*;

@Entity
@Data
public class IncomeTax {
    @Id
    @GeneratedValue (strategy = GenerationType.AUTO)
    @Column (name = "id")
    private Long id;
    @Column (name = "tax")
    private double tax;
    @Column (name = "message")
    private String message;
}
