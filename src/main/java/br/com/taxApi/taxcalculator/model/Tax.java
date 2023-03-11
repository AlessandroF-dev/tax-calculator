package br.com.taxApi.taxcalculator.model;

import lombok.Data;

import javax.persistence.*;

@Entity
@Data
public class Tax {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Column
    private double calculatorTierBegin;

    @Column
    private double calculatorTierEnd;

    @Column
    private double tax;

}
